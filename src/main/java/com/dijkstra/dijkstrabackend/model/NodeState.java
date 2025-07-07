package com.dijkstra.dijkstrabackend.model;

/**
 * Enum representing the state or type of a grid cell.
 */
public enum NodeState {
    EMPTY,      // Default traversable cell
    OBSTACLE,   // Impassable cell
    START,      // Starting point of the path
    END,        // Ending point of the path
    VISITED,    // Cell visited during algorithm execution
    PATH        // Cell part of the final shortest path
}