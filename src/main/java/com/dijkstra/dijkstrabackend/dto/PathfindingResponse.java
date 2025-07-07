package com.dijkstra.dijkstrabackend.dto;

import com.dijkstra.dijkstrabackend.model.GridCell;
import com.dijkstra.dijkstrabackend.model.VisualizationStep;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//Data Transfer Object (DTO) for responses sent back to the frontend.Includes the shortest path (if found), and visualization steps for animating the algorithm.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PathfindingResponse {
    private List<GridCell> shortestPath;        // Nodes forming the shortest path
    private List<VisualizationStep> visualizationSteps; // Steps to animate the algorithm's execution
    private String message;                     // Informational message
    private boolean pathFound;                  // True if a path was successfully found
}