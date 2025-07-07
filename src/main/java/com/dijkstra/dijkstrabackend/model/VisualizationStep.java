package com.dijkstra.dijkstrabackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

//Represents a single observable step in Dijkstra's algorithm for frontend visualization.Contains information about nodes visited or distances updated in that specific step.

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisualizationStep {
    private List<GridCell> visitedNodes;
    private Map<GridCell, Double> updatedDistances;
    private String action;
}