package com.dijkstra.dijkstrabackend.service;

import com.dijkstra.dijkstrabackend.model.GridCell;
import com.dijkstra.dijkstrabackend.model.NodeState;
import com.dijkstra.dijkstrabackend.model.VisualizationStep;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

// Service responsible for executing Dijkstra's algorithm on a grid.It calculates the shortest path and generates a sequence of visualization steps.

@Service
public class DijkstraService {


    public Map<String, Object> findShortestPath(List<List<GridCell>> initialGrid, GridCell startNode, GridCell endNode) {
        List<VisualizationStep> visualizationSteps = new ArrayList<>();
        List<GridCell> shortestPath = new ArrayList<>();
        boolean pathFound = false;

        // Grid Initialization & Setup for Dijkstra
        // Create a mutable 2D array from the immutable initialGrid for algorithm execution.This ensures Dijkstra's properties (distance, isVisited, previousNode) can be modified.
        GridCell[][] grid = new GridCell[initialGrid.size()][initialGrid.get(0).size()];
        for (int r = 0; r < initialGrid.size(); r++) {
            for (int c = 0; c < initialGrid.get(r).size(); c++) {
                GridCell originalCell = initialGrid.get(r).get(c);
                // Create new GridCell instances to prevent modifying the DTO directly
                GridCell newCell = new GridCell(originalCell.getRow(), originalCell.getCol(), originalCell.getType());
                newCell.setDistance(Double.POSITIVE_INFINITY); // All distances initially infinite
                newCell.setVisited(false); // No nodes visited initially
                newCell.setPreviousNode(null);
                grid[r][c] = newCell;
            }
        }

        // Get the actual start and end node instances from our mutable grid.
        // This is important because algorithm logic will update these specific objects.
        GridCell actualStartNode = grid[startNode.getRow()][startNode.getCol()];
        GridCell actualEndNode = grid[endNode.getRow()][endNode.getCol()];

        // Set the distance of the start node to 0.
        actualStartNode.setDistance(0);

        // Priority queue to store nodes to visit, ordered by their current shortest distance.
        // A min-heap, crucial for Dijkstra's to always explore the unvisited node with the smallest distance.
        PriorityQueue<GridCell> pq = new PriorityQueue<>();
        pq.add(actualStartNode);

        // Define possible movements (up, down, left, right)
        int[] dr = {-1, 1, 0, 0}; // delta row
        int[] dc = {0, 0, -1, 1}; // delta col



        // --- Dijkstra's Algorithm Main Loop --
        while (!pq.isEmpty()) {
            GridCell current = pq.poll(); // Extract the node with the smallest known distance

            // Skip if this node has already been finalized (visited via a shorter path)
            if (current.isVisited()) {
                continue;
            }

            // Mark the current node as visited (finalized)
            current.setVisited(true);

            // Record this step for frontend visualization: This node has just been visited.
            // We create a new GridCell to send only essential info and avoid circular references.
            visualizationSteps.add(new VisualizationStep(
                    List.of(new GridCell(current.getRow(), current.getCol(), NodeState.VISITED)),
                    new HashMap<>(), // No distance updates for this action
                    "visiting"
            ));

            // If the end node is reached, reconstruct the path and terminate.
            if (current.equals(actualEndNode)) {
                shortestPath = reconstructPath(actualEndNode);
                pathFound = true;
                // Add a final visualization step to highlight the shortest path.
                visualizationSteps.add(new VisualizationStep(
                        shortestPath.stream()
                                .map(node -> new GridCell(node.getRow(), node.getCol(), NodeState.PATH))
                                .collect(Collectors.toList()), // Ensure it's a mutable list
                        new HashMap<>(),
                        "path_found"
                ));
                break;
            }



            // --- Explore Neighbors (Relaxation Step) ---
            for (int i = 0; i < 4; i++) { // Iterate through 4 possible directions
                int newR = current.getRow() + dr[i];
                int newC = current.getCol() + dc[i];

                // Check if neighbor is within grid bounds
                if (newR < 0 || newR >= grid.length || newC < 0 || newC >= grid[0].length) {
                    continue;
                }

                GridCell neighbor = grid[newR][newC];

                // Skip if neighbor is an obstacle or already visited (finalized)
                if (neighbor.getType() == NodeState.OBSTACLE || neighbor.isVisited()) {
                    continue;
                }

                // Calculate the new distance to the neighbor through the current node (assuming cost of 1 per step)
                double newDistance = current.getDistance() + 1;

                // If a shorter path to the neighbor is found (relaxation)
                if (newDistance < neighbor.getDistance()) {
                    neighbor.setDistance(newDistance); // Update neighbor's distance
                    neighbor.setPreviousNode(current); // Set current node as neighbor's predecessor
                    pq.add(neighbor); // Add/re-add neighbor to priority queue to re-evaluate its path

                    // Record distance update for visualization
                    Map<GridCell, Double> updatedDistancesMap = new HashMap<>();
                    updatedDistancesMap.put(new GridCell(neighbor.getRow(), neighbor.getCol(), neighbor.getType()), newDistance);
                    visualizationSteps.add(new VisualizationStep(
                            new ArrayList<>(), // No new nodes visited in this specific action
                            updatedDistancesMap,
                            "updating_distance"
                    ));
                }
            }
        }

        // Prepare the result map to return
        Map<String, Object> result = new HashMap<>();
        result.put("shortestPath", shortestPath);
        result.put("visualizationSteps", visualizationSteps);
        result.put("pathFound", pathFound);
        return result;
    }

    //Reconstructs the shortest path from the end node back to the start nodeusing the `previousNode` pointers set during Dijkstra's execution.

    private List<GridCell> reconstructPath(GridCell endNode) {
        List<GridCell> path = new ArrayList<>();
        GridCell current = endNode;
        // Traverse backwards from endNode using previousNode pointers
        while (current != null) {
            // Create a new GridCell to avoid circular references in JSON serialization
            // and only include essential coordinates and type for the path.
            path.add(0, new GridCell(current.getRow(), current.getCol(), current.getType()));
            current = current.getPreviousNode();
        }
        // Handle cases where the start node might be duplicated if reconstruction logic
        // adds it twice (once from path.add(0, ...) and once as current becomes null).
        if (path.size() > 1 && path.get(0).equals(path.get(1))) {
            path.remove(0); // Remove the duplicate start node
        }
        return path;
    }
}