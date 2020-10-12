package eu.wiktordolecki.maze;

import net.jqwik.api.arbitraries.IntegerArbitrary;

import net.jqwik.api.*;
import net.jqwik.api.constraints.Positive;

import static org.assertj.core.api.Assertions.assertThat;

class GridNodeTest {

    @Provide
    Arbitrary<GridNode> gridNode() {
        IntegerArbitrary x = Arbitraries.integers().between(0, 255);
        IntegerArbitrary y = Arbitraries.integers().between(0, 255);
        IntegerArbitrary id = Arbitraries.integers().between(0, 65535);
        return Combinators.combine(x, y, id).as(GridNode::new);
    }

    @Property
    void anyNewNodeHasEmptyNeighbors(
            @ForAll("gridNode") GridNode node) {
        assertThat(node.neighbours()).isEmpty();
    }

    @Property
    void anyTwoNewNodesAreNotConnected(
            @ForAll("gridNode") GridNode node1,
            @ForAll("gridNode") GridNode node2) {
        assertThat(node1.isConnected(node2)).isFalse();
        assertThat(node2.isConnected(node1)).isFalse();
    }

    @Property
    void anyNodeIsNotConnectedToNull(
            @ForAll("gridNode") GridNode node) {
        assertThat(node.isConnected(null)).isFalse();
    }

    @Property
    void anyNodeIsNotConnectedToSelf(
            @ForAll("gridNode") GridNode node) {
        assertThat(node.isConnected(node)).isFalse();
    }

    @Property
    void anyNodeCannotConnectToSelf(
            @ForAll("gridNode") GridNode node) {
        node.connect(node);

        assertThat(node.isConnected(node)).isFalse();
        assertThat(node.neighbours()).isEmpty();
    }

    @Property
    void anyNodeCannotConnectToNull(
            @ForAll("gridNode") GridNode node) {
        node.connect(null);

        assertThat(node.isConnected(null)).isFalse();
        assertThat(node.neighbours()).isEmpty();
    }

    @Example
    void nodesWithSameIdCannotConnect() {
        GridNode node1 = new GridNode(0, 0, 1);
        GridNode node2 = new GridNode(1, 1, 1);

        node1.connect(node2);

        assertThat(node1.isConnected(node2)).isFalse();
        assertThat(node2.isConnected(node1)).isFalse();

        assertThat(node1.neighbours()).isEmpty();
        assertThat(node2.neighbours()).isEmpty();
    }

    @Property
    void connectionIsReflectiveForNodesWithDifferentId(
            @ForAll("gridNode") GridNode node1,
            @ForAll("gridNode") GridNode node2) {
        Assume.that(node1.id() != node2.id());

        node1.connect(node2);

        assertThat(node1.isConnected(node2)).isTrue();
        assertThat(node2.isConnected(node1)).isTrue();
    }

    @Example
    void toStringIsProvidingExpectedOutput() {
        GridNode node = new GridNode(0, 0, 0);

        String expected = "GridNode{x=0, y=0, id=0, neighbours=[]}";

        assertThat(node.toString()).isEqualTo(expected);
    }

    @Property
    void accessorsReturnProvidedValues(
            @ForAll @Positive int x,
            @ForAll @Positive int y,
            @ForAll @Positive int id) {
        GridNode node = new GridNode(x, y, id);

        assertThat(node.x()).isEqualTo(x);
        assertThat(node.y()).isEqualTo(y);
        assertThat(node.id()).isEqualTo(id);
    }
}