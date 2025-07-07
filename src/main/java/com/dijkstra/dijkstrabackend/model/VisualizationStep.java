package com.dijkstra.dijkstrabackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Represents a single step in the Dijkstra's algorithm visualization.
 * It contains information about which nodes were visited, whose distances were updated,
 * and the overall state of the grid at that point.
 */
@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all arguments
public class VisualizationStep {
    // List of nodes that were just visited in this step
    private List<GridCell> visitedNodes;

    // Map of nodes whose distances were updated in this step (key: node, value: new distance)
    private Map<GridCell, Double> updatedDistances;

    // The type of action that occurred in this step (e.g., "visiting", "updating_distance", "path_found")
    private String action;
}