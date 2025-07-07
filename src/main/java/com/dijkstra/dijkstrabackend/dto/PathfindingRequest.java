package com.dijkstra.dijkstrabackend.dto;

import com.dijkstra.dijkstrabackend.model.GridCell;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for the request payload from the frontend to the backend.
 * Contains the grid configuration, start node, and end node.
 */
@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all arguments
public class PathfindingRequest {
    private List<List<GridCell>> grid; // The 2D grid from the frontend
    private GridCell start; // The start node coordinates
    private GridCell end;   // The end node coordinates
}