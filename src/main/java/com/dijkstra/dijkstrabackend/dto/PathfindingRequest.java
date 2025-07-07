package com.dijkstra.dijkstrabackend.dto;

import com.dijkstra.dijkstrabackend.model.GridCell;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//Data Transfer Object (DTO) for incoming pathfinding requests from the frontend.Contains the grid layout, start node, and end node.

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PathfindingRequest {
    private List<List<GridCell>> grid; // The 2D grid representation
    private GridCell start;             // The start node
    private GridCell end;               // The end node
}