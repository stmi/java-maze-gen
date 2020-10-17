package eu.wiktordolecki.maze;

import lombok.Getter;

import java.util.stream.Stream;

import static java.util.stream.Stream.*;

public class SquareGrid {

    @Getter
    private final int width;
    @Getter
    private final int height;

    private final GridNode[][] grid;

    public SquareGrid(int width, int height) {
        if (width <= 0) {
            throw new IllegalArgumentException("SquareGrid width has to be positive integer");
        }
        if (width <= 0) {
            throw new IllegalArgumentException("SquareGrid height has to be positive integer");
        }
        this.width = width;
        this.height = height;
        this.grid = new GridNode[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new GridNode(x, y, x * width + y);
            }
        }
    }

    @Override
    public String toString() {
        return "SquareGrid{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }

    public GridNode up(GridNode node) {
        if (node.y() == 0) {
            return null;
        } else {
            return this.grid[node.x()][node.y() - 1];
        }
    }

    public GridNode down(GridNode node) {
        if (node.y() == height - 1) {
            return null;
        } else {
            return this.grid[node.x()][node.y() + 1];
        }
    }

    public GridNode left(GridNode node) {
        if (node.x() == 0) {
            return null;
        } else {
            return this.grid[node.x() - 1][node.y()];
        }
    }

    public GridNode right(GridNode node) {
        if (node.x() == width - 1) {
            return null;
        } else {
            return this.grid[node.x() + 1][node.y()];
        }
    }

    public Stream<GridNode> around(GridNode node) {
        return of(up(node), down(node), left(node), right(node));
    }

    public GridNode at(int x, int y) {
        return grid[x][y];
    }
}
