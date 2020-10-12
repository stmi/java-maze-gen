package eu.wiktordolecki.maze;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Accessors(fluent = true)
@Getter
class GridNode {

    private final int id;
    private final int x;
    private final int y;

    private final Set<Integer> neighbours = new HashSet<>();

    GridNode(int x, int y, int id) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "GridNode{" +
                "x=" + x +
                ", y=" + y +
                ", id=" + id +
                ", neighbours=" + neighbours +
                '}';
    }

    public void connect(GridNode other) {
        if (other == null
                || other == this
                || other.id == this.id) {
            return;
        }

        this.neighbours.add(other.id);
        other.neighbours.add(this.id);
    }

    public boolean isConnected(GridNode other) {
        if (other == null
                || other == this
                || other.id == this.id) {
            return false;
        }
        return this.neighbours.contains(other.id);
    }
}
