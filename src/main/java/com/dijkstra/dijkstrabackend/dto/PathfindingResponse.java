package com.dijkstra.dijkstrabackend.dto;

import com.dijkstra.dijkstrabackend.model.GridCell;
import com.dijkstra.dijkstrabackend.model.VisualizationStep;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for the response payload from the backend to the frontend.
 * Contains the shortest path found and the sequence of visualization steps.
 */
@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all arguments
public class PathfindingResponse {
    private List<GridCell> shortestPath; // The list of nodes forming the shortest path
    private List<VisualizationStep> visualizationSteps; // Steps to animate the algorithm
    private String message; // Any message (e.g., "Path found!", "No path found!")
    private boolean pathFound; // Indicates if a path was successfully found
}