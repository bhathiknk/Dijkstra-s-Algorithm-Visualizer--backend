package com.dijkstra.dijkstrabackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single cell/node in the grid for pathfinding.
 * It holds its coordinates, type, and properties used by Dijkstra's algorithm.
 */
@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all arguments
public class GridCell implements Comparable<GridCell> {
    private int row;
    private int col;
    private NodeState type; // e.g., EMPTY, OBSTACLE, START, END, VISITED, PATH

    // Dijkstra's algorithm specific properties
    private double distance = Double.POSITIVE_INFINITY; // Distance from the start node
    private boolean isVisited = false; // Whether this node has been processed
    private GridCell previousNode = null; // To reconstruct the shortest path

    /**
     * Constructor used when receiving data from the frontend.
     * The frontend only sends row, col, and type.
     */
    public GridCell(int row, int col, NodeState type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }

    /**
     * Compares GridCell objects based on their distance for use in a PriorityQueue.
     * Nodes with smaller distances have higher priority.
     */
    @Override
    public int compareTo(GridCell other) {
        return Double.compare(this.distance, other.distance);
    }

    /**
     * Custom equals and hashCode for proper comparison in data structures like HashMap/HashSet
     * based on their coordinates, not their Dijkstra-specific properties.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GridCell gridCell = (GridCell) o;
        return row == gridCell.row && col == gridCell.col;
    }

    @Override
    public int hashCode() {
        // Simple hash based on row and col
        return 31 * row + col;
    }
}