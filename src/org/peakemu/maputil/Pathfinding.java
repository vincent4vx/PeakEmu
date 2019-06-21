/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.maputil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import org.peakemu.world.GameMap;
import org.peakemu.world.MapCell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Pathfinding {
    static private class Node implements Comparable<Node>{
        final private Coordinate coordinate;
        final private MapCell cell;
        final private int distantToStart;
        final private int distantToEnd;
        final private Node previous;

        public Node(Coordinate coordinate, MapCell cell, int distantToStart, int distantToEnd, Node previous) {
            this.coordinate = coordinate;
            this.cell = cell;
            this.distantToStart = distantToStart;
            this.distantToEnd = distantToEnd;
            this.previous = previous;
        }

        @Override
        public int compareTo(Node t) {
            return (distantToStart + distantToEnd) - (t.distantToStart + t.distantToEnd);
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 23 * hash + Objects.hashCode(this.coordinate);
            hash = 23 * hash + Objects.hashCode(this.cell);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Node other = (Node) obj;
            if (!Objects.equals(this.coordinate, other.coordinate)) {
                return false;
            }
            if (!Objects.equals(this.cell, other.cell)) {
                return false;
            }
            return true;
        }
    }
    
    static private Collection<Node> getAdjacentNodes(Node node, Node end, boolean allDirections){
        Collection<Node> adjacents = new ArrayList<>();
        
        Direction[] directions = allDirections ? Direction.values() : Direction.RESTRICTED_DIRECTIONS;
        
        for(Direction direction : directions){
            MapCell cell = direction.nextCell(node.cell);
            
            if(cell == null || !cell.isWalkable(true))
                continue;
            
            Coordinate coordinate = Coordinate.fromCell(cell);
            
            int distantToStart = node.distantToStart + 1;
            int distantToEnd = coordinate.getDistance(end.coordinate, allDirections);
            adjacents.add(new Node(coordinate, cell, distantToStart, distantToEnd, node));
        }
        
        return adjacents;
    }
    
    static public List<MapCell> findPath(GameMap map, MapCell start, MapCell end, boolean allDirections, CellChecker checker) throws PathfindingException{
        Coordinate startCoord = Coordinate.fromCell(start);
        Coordinate endCoord = Coordinate.fromCell(end);
        Node startNode = new Node(startCoord, start, 0, startCoord.getDistance(endCoord, allDirections), null);
        Node endNode = new Node(endCoord, end, endCoord.getDistance(startCoord, allDirections), 0, null);
        
        if(!checker.checkCell(end) || !end.isWalkable(true))
            throw new RuntimeException("Invalid end cell");
        
        List<Node> closedList = new ArrayList<>();
        PriorityQueue<Node> openList = new PriorityQueue<>();
        
        openList.add(startNode);
        
        while(!openList.isEmpty()){
            Node current = openList.poll();
            
            if(current.equals(endNode)){
                return toPath(current);
            }
            
            closedList.add(current);
            
            for(Node node : getAdjacentNodes(current, endNode, allDirections)){
                if(!checker.checkCell(node.cell))
                    continue;
                
                if(closedList.contains(node))
                    continue;
                
                if(!openList.contains(node)){
                    openList.add(node);
                }
            }
        }
        
        Node nearestNode = getNearestNode(closedList, endCoord);
        
        if(nearestNode == null)
            throw new PathfindingException("Path not found");
        else
            throw new PartialPathException(toPath(nearestNode), "Partial path found");
    }
    
    static private Node getNearestNode(Collection<Node> nodes, Coordinate dest){
        Node nearest = null;
        int distance = Integer.MAX_VALUE;
        
        for (Node node : nodes) {
            int newDist = node.coordinate.getDistance(dest);
            
            if(newDist < distance)
                nearest = node;
        }
        
        return nearest;
    }
    
    static public List<MapCell> findPath(GameMap map, MapCell start, MapCell end, boolean allDirections) throws PathfindingException{
        return findPath(map, start, end, allDirections, CellChecker.ALLOW_ALL);
    }
    
    static private List<MapCell> toPath(Node node){
        List<MapCell> path = new LinkedList<>();
        
        do{
            path.add(0, node.cell);
            node = node.previous;
        }while(node != null);
        
        return path;
    }
}
