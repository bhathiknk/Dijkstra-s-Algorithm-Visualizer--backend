package com.dijkstra.dijkstrabackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Represents a single cell/node in the grid for pathfinding.Includes coordinates, type, and Dijkstra's algorithm specific properties.

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GridCell implements Comparable<GridCell> {
    private int row;
    private int col;
    private NodeState type;

    // Properties specific to Dijkstra's algorithm calculations
    private double distance = Double.POSITIVE_INFINITY; // Current shortest distance from start
    private boolean isVisited = false; // Whether this node has been finalized/processed
    private GridCell previousNode = null; // The predecessor node on the shortest path from start


    //Constructor for creating a GridCell when receiving basic data from the frontend.Dijkstra-specific fields will be initialized to defaults.
    public GridCell(int row, int col, NodeState type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }

    //Compares GridCell objects based on their distance.Essential for PriorityQueue to prioritize nodes with shorter distances.

    @Override
    public int compareTo(GridCell other) {
        return Double.compare(this.distance, other.distance);
    }

    //Defines equality based solely on row and column, ignoring Dijkstra-specific properties.Important for correct comparisons in data structures.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GridCell gridCell = (GridCell) o;
        return row == gridCell.row && col == gridCell.col;
    }

    //Generates a hash code based solely on row and column.Consistent with `equals` method, crucial for proper functioning in hash-based collections.

    @Override
    public int hashCode() {
        return 31 * row + col;
    }
}