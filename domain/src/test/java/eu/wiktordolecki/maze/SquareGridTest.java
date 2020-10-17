package eu.wiktordolecki.maze;

import net.jqwik.api.*;
import net.jqwik.api.Tuple.Tuple2;
import net.jqwik.api.arbitraries.IntegerArbitrary;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SquareGridTest {

    @Provide
    Arbitrary<SquareGrid> arbitrarySquareGrid() {
        IntegerArbitrary width = Arbitraries.integers().between(2, 256);
        IntegerArbitrary height = Arbitraries.integers().between(2, 256);

        return Combinators.combine(width, height).as(SquareGrid::new);
    }

    @Provide
    Arbitrary<Tuple2<SquareGrid, GridNode>> gridNodePair() {
        return arbitrarySquareGrid().flatMap(grid -> {
            IntegerArbitrary x = Arbitraries.integers().between(0, grid.width() - 1);
            IntegerArbitrary y = Arbitraries.integers().between(0, grid.height() - 1);

            return Combinators.combine(x, y).as(grid::at).map(node -> Tuple.of(grid, node));
        });
    }

    @Provide
    Arbitrary<Tuple2<SquareGrid, GridNode>> gridTopNodePair() {
        return arbitrarySquareGrid().flatMap(grid -> {
            IntegerArbitrary x = Arbitraries.integers().between(0, grid.width() - 1);
            IntegerArbitrary y = Arbitraries.integers().between(0, 0);

            return Combinators.combine(x, y).as(grid::at).map(node -> Tuple.of(grid, node));
        });
    }

    @Provide
    Arbitrary<Tuple2<SquareGrid, GridNode>> gridBottomNodePair() {
        return arbitrarySquareGrid().flatMap(grid -> {
            IntegerArbitrary x = Arbitraries.integers().between(0, grid.width() - 1);
            IntegerArbitrary y = Arbitraries.integers().between(grid.height() - 1, grid.height() - 1);

            return Combinators.combine(x, y).as(grid::at).map(node -> Tuple.of(grid, node));
        });
    }

    @Provide
    Arbitrary<Tuple2<SquareGrid, GridNode>> gridLeftNodePair() {
        return arbitrarySquareGrid().flatMap(grid -> {
            IntegerArbitrary x = Arbitraries.integers().between(0, 0);
            IntegerArbitrary y = Arbitraries.integers().between(0, grid.height() - 1);

            return Combinators.combine(x, y).as(grid::at).map(node -> Tuple.of(grid, node));
        });
    }

    @Provide
    Arbitrary<Tuple2<SquareGrid, GridNode>> gridRightNodePair() {
        return arbitrarySquareGrid().flatMap(grid -> {
            IntegerArbitrary x = Arbitraries.integers().between(grid.width() - 1, grid.width() - 1);
            IntegerArbitrary y = Arbitraries.integers().between(0, grid.height() - 1);

            return Combinators.combine(x, y).as(grid::at).map(node -> Tuple.of(grid, node));
        });
    }

    @Property
    void upAndDownAreReversibleBellowTop(@ForAll("gridNodePair") Tuple2<SquareGrid, GridNode> tuple) {
        SquareGrid grid = tuple.get1();
        GridNode node = tuple.get2();
        Assume.that(node.y() > 0);

        GridNode up = grid.up(node);
        assertThat(grid.down(up)).isSameAs(node);
    }

    @Property
    void upOnTopRowMovesOffGrid(@ForAll("gridTopNodePair") Tuple2<SquareGrid, GridNode> tuple) {
        SquareGrid grid = tuple.get1();
        GridNode node = tuple.get2();
        Assume.that(node.y() == 0);

        assertThat(grid.up(node)).isNull();
    }

    @Property
    void downAndUpAreReversibleAboveBottom(@ForAll("gridNodePair") Tuple2<SquareGrid, GridNode> tuple) {
        SquareGrid grid = tuple.get1();
        GridNode node = tuple.get2();
        Assume.that(node.y() < grid.height() - 1);

        GridNode down = grid.down(node);
        assertThat(grid.up(down)).isSameAs(node);
    }

    @Property
    void downOnBottomRowMovesOffGrid(@ForAll("gridBottomNodePair") Tuple2<SquareGrid, GridNode> tuple) {
        SquareGrid grid = tuple.get1();
        GridNode node = tuple.get2();

        assertThat(grid.down(node)).isNull();
    }

    @Property
    void leftAndRightAreReversiblePastLeftMargin(@ForAll("gridNodePair") Tuple2<SquareGrid, GridNode> tuple) {
        SquareGrid grid = tuple.get1();
        GridNode node = tuple.get2();
        Assume.that(node.x() > 0);

        GridNode left = grid.left(node);
        assertThat(grid.right(left)).isSameAs(node);
    }

    @Property
    void leftOnLeftMarginMovesOffGrid(@ForAll("gridLeftNodePair") Tuple2<SquareGrid, GridNode> tuple) {
        SquareGrid grid = tuple.get1();
        GridNode node = tuple.get2();

        assertThat(grid.left(node)).isNull();
    }

    @Property
    void rightAndLeftAreReversibleBeforeRightMargin(@ForAll("gridNodePair") Tuple2<SquareGrid, GridNode> tuple) {
        SquareGrid grid = tuple.get1();
        GridNode node = tuple.get2();
        Assume.that(node.x() < grid.width() - 1);

        GridNode right = grid.right(node);
        assertThat(grid.left(right)).isSameAs(node);
    }

    @Property
    void rightOnRightMarginMovesOffGrid(@ForAll("gridRightNodePair") Tuple2<SquareGrid, GridNode> tuple) {
        SquareGrid grid = tuple.get1();
        GridNode node = tuple.get2();

        assertThat(grid.right(node)).isNull();
    }

    @Example
    void movingInAnyDirectionOnGridSizeOneMovesOffGrid() {
        SquareGrid grid = new SquareGrid(1, 1);
        GridNode node = grid.at(0, 0);

        assertThat(grid.up(node)).isNull();
        assertThat(grid.down(node)).isNull();
        assertThat(grid.left(node)).isNull();
        assertThat(grid.right(node)).isNull();
    }
}