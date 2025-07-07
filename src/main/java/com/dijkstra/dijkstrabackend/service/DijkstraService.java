package com.dijkstra.dijkstrabackend.service;

import com.dijkstra.dijkstrabackend.model.GridCell;
import com.dijkstra.dijkstrabackend.model.NodeState;
import com.dijkstra.dijkstrabackend.model.VisualizationStep;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class that implements Dijkstra's algorithm for pathfinding on a grid.
 * It also records the steps of the algorithm for visualization on the frontend.
 */
@Service
public class DijkstraService {

    /**
     * Finds the shortest path from a start node to an end node on a grid
     * using Dijkstra's algorithm and records visualization steps.
     *
     * @param initialGrid The 2D list representing the grid with obstacles, start, and end nodes.
     * @param startNode   The starting GridCell.
     * @param endNode     The ending GridCell.
     * @return A map containing:
     * - "shortestPath": A list of GridCell objects forming the shortest path.
     * - "visualizationSteps": A list of VisualizationStep objects detailing the algorithm's execution.
     * - "pathFound": A boolean indicating if a path was found.
     */
    public Map<String, Object> findShortestPath(List<List<GridCell>> initialGrid, GridCell startNode, GridCell endNode) {
        List<VisualizationStep> visualizationSteps = new ArrayList<>();
        List<GridCell> shortestPath = new ArrayList<>();
        boolean pathFound = false;

        // Deep copy the initial grid to work on, preserving original state for multiple runs
        // Also, convert the List<List<GridCell>> to a 2D array for easier access and mutable properties
        GridCell[][] grid = new GridCell[initialGrid.size()][initialGrid.get(0).size()];
        for (int r = 0; r < initialGrid.size(); r++) {
            for (int c = 0; c < initialGrid.get(r).size(); c++) {
                // Create a new GridCell object to avoid modifying the original request object
                // and to correctly initialize Dijkstra-specific fields for the algorithm run
                GridCell originalCell = initialGrid.get(r).get(c);
                grid[r][c] = new GridCell(originalCell.getRow(), originalCell.getCol(), originalCell.getType());
                grid[r][c].setDistance(Double.POSITIVE_INFINITY); // Reset distance
                grid[r][c].setVisited(false); // Reset visited status
                grid[r][c].setPreviousNode(null); // Reset previous node
            }
        }

        // Find the actual start and end nodes in the copied grid
        // This is crucial because `equals` and `hashCode` for GridCell are based on coordinates only,
        // but we need to work with the *specific instances* in our `grid` array
        // that will have their `distance`, `isVisited`, `previousNode` fields updated.
        GridCell actualStartNode = grid[startNode.getRow()][startNode.getCol()];
        GridCell actualEndNode = grid[endNode.getRow()][endNode.getCol()];

        // Initialize distances: start node is 0, others are infinity
        actualStartNode.setDistance(0);

        // Priority queue to store nodes to visit, ordered by their current shortest distance
        // Using Java's built-in PriorityQueue which is a min-heap
        PriorityQueue<GridCell> pq = new PriorityQueue<>();
        pq.add(actualStartNode);

        // Directions for moving (up, down, left, right)
        int[] dr = {-1, 1, 0, 0}; // delta row
        int[] dc = {0, 0, -1, 1}; // delta col

        while (!pq.isEmpty()) {
            GridCell current = pq.poll(); // Get the node with the smallest distance

            // If we've already visited this node with a shorter path, skip
            if (current.isVisited()) {
                continue;
            }

            // Mark current node as visited
            current.setVisited(true);

            // Record this step for visualization: Node being visited
            visualizationSteps.add(new VisualizationStep(
                    List.of(new GridCell(current.getRow(), current.getCol(), NodeState.VISITED)), // Mark as visited for animation
                    new HashMap<>(), // No distance updates in this specific step for this node, just visited
                    "visiting"
            ));

            // If we reached the end node, reconstruct the path and break
            if (current.equals(actualEndNode)) {
                shortestPath = reconstructPath(actualEndNode);
                pathFound = true;
                // Add a final step to highlight the path
                visualizationSteps.add(new VisualizationStep(
                        shortestPath.stream()
                                .map(node -> new GridCell(node.getRow(), node.getCol(), NodeState.PATH))
                                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll),
                        new HashMap<>(),
                        "path_found"
                ));
                break;
            }

            // Explore neighbors
            for (int i = 0; i < 4; i++) {
                int newR = current.getRow() + dr[i];
                int newC = current.getCol() + dc[i];

                // Check bounds
                if (newR < 0 || newR >= grid.length || newC < 0 || newC >= grid[0].length) {
                    continue;
                }

                GridCell neighbor = grid[newR][newC];

                // Skip if neighbor is an obstacle or already visited
                if (neighbor.getType() == NodeState.OBSTACLE || neighbor.isVisited()) {
                    continue;
                }

                // Calculate new distance to neighbor (assuming uniform cost of 1 per step)
                double newDistance = current.getDistance() + 1;

                // If a shorter path to neighbor is found
                if (newDistance < neighbor.getDistance()) {
                    neighbor.setDistance(newDistance);
                    neighbor.setPreviousNode(current); // Set current as previous node for path reconstruction
                    pq.add(neighbor); // Add or update neighbor in priority queue

                    // Record distance update for visualization
                    Map<GridCell, Double> updatedDistancesMap = new HashMap<>();
                    // Send a new GridCell instance to avoid circular references/complex object graphs in JSON
                    updatedDistancesMap.put(new GridCell(neighbor.getRow(), neighbor.getCol(), neighbor.getType()), newDistance);
                    visualizationSteps.add(new VisualizationStep(
                            new ArrayList<>(), // No new nodes visited in this specific step
                            updatedDistancesMap,
                            "updating_distance"
                    ));
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("shortestPath", shortestPath);
        result.put("visualizationSteps", visualizationSteps);
        result.put("pathFound", pathFound);
        return result;
    }

    /**
     * Reconstructs the shortest path from the end node back to the start node
     * using the `previousNode` pointers.
     *
     * @param endNode The end GridCell from which to start reconstruction.
     * @return A list of GridCell objects representing the shortest path.
     */
    private List<GridCell> reconstructPath(GridCell endNode) {
        List<GridCell> path = new ArrayList<>();
        GridCell current = endNode;
        while (current != null) {
            // Create a new GridCell to avoid circular references in JSON serialization
            // and only include necessary info for the path (row, col, type)
            // The type for the path nodes will be set on the frontend for visualization
            path.add(0, new GridCell(current.getRow(), current.getCol(), current.getType()));
            current = current.getPreviousNode();
        }
        // Remove the start node if it's duplicated (it's often added twice due to how reconstruction works)
        // This handles cases where startNode is also considered part of the path
        if (path.size() > 1 && path.get(0).equals(path.get(1))) {
            // If the first two elements are the same (start node repeated), remove one.
            // This can happen if the 'start' node is re-added when reconstructing.
            // We want the path FROM start TO end, not including start twice.
            path.remove(0);
        }
        return path;
    }
}