package com.dijkstra.dijkstrabackend.controller;

import com.dijkstra.dijkstrabackend.dto.PathfindingRequest;
import com.dijkstra.dijkstrabackend.dto.PathfindingResponse;
import com.dijkstra.dijkstrabackend.model.GridCell;
import com.dijkstra.dijkstrabackend.service.DijkstraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//REST Controller for handling pathfinding requests.Exposes an API endpoint for the frontend to trigger Dijkstra's algorithm.

@RestController
@RequestMapping("/api")
public class PathfindingController {

    private final DijkstraService dijkstraService;

    @Autowired
    public PathfindingController(DijkstraService dijkstraService) {
        this.dijkstraService = dijkstraService;
    }

    //Handles POST requests to find the shortest path using Dijkstra's algorithm.
    @PostMapping("/find-path")
    public ResponseEntity<PathfindingResponse> findPath(@RequestBody PathfindingRequest request) {
        try {
            // Basic input validation
            if (request.getGrid() == null || request.getGrid().isEmpty() ||
                    request.getStart() == null || request.getEnd() == null) {
                return ResponseEntity.badRequest().body(new PathfindingResponse(
                        null, null, "Invalid request: Grid, start, or end not provided.", false
                ));
            }

            // Call the Dijkstra service to find the path and get visualization data
            Map<String, Object> result = dijkstraService.findShortestPath(
                    request.getGrid(),
                    request.getStart(),
                    request.getEnd()
            );

            // Extract results from the service response map
            List<GridCell> shortestPath = (List<GridCell>) result.get("shortestPath");
            List<com.dijkstra.dijkstrabackend.model.VisualizationStep> visualizationSteps =
                    (List<com.dijkstra.dijkstrabackend.model.VisualizationStep>) result.get("visualizationSteps");
            boolean pathFound = (boolean) result.get("pathFound");

            String message = pathFound ? "Path found successfully!" : "No path found.";

            // Return the response to the frontend
            return ResponseEntity.ok(new PathfindingResponse(shortestPath, visualizationSteps, message, pathFound));

        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new PathfindingResponse(
                    null, null, "An error occurred: " + e.getMessage(), false
            ));
        }
    }
}