package com.dijkstra.dijkstrabackend.model;

//Enum representing the various states a grid cell can have during pathfinding.

public enum NodeState {
    EMPTY,
    OBSTACLE,
    START,
    END,
    VISITED,
    PATH
}