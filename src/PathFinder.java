import java.util.*;
import java.util.function.Predicate;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;

public class PathFinder {
    private Integer rows;
    private Integer columns;

    private final Integer[] rowMoves = {-1,1,0,0};
    private final Integer[] columnMoves = {0,0,-1,1};

    public PathFinder(Integer rows, Integer columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public synchronized List<Position> searchPath(Position start, Position end, Predicate<Position> isAvailable, ToDoubleFunction<Position> calculateCost){
        PriorityQueue<Node> open = new PriorityQueue<>();
        Set<Position> closed = new HashSet<>();
        Map<Position, Position> parent = new HashMap<>();
        Map<Position, Double> cost = new HashMap<>();

        cost.put(start,0.0);

        Double initialCost = heuristic(start, end);
        open.add(new Node(start,initialCost));

        while (!open.isEmpty()){
            Node actualNode = open.poll();
            Position actual = actualNode.getPosition();

            if (closed.contains(actual)){
                continue;
            }
            if (actual.equals(end)){
                return buildPath(parent,start,end);
            }
            closed.add(actual);
            exploreNeighbors(actual, end, isAvailable, closed, calculateCost, cost, parent, open);
        }
        return new ArrayList<>();
    }

    private void exploreNeighbors(Position actual, Position end, Predicate<Position> isAvailable, Set<Position> closed, ToDoubleFunction<Position> calculateCost, Map<Position, Double> cost, Map<Position, Position> parent, PriorityQueue<Node> open) {
        for (Position neighbor : getNeighbors(actual)){
            if (!isAvailable.test(neighbor) && !neighbor.equals(end)){
                continue;
            }
            if (closed.contains(neighbor)){
                continue;
            }
            Double movementCost = Math.max(1, calculateCost.applyAsDouble(neighbor));
            Double newCost =  cost.get(actual) + movementCost;
            Double oldCost = cost.getOrDefault(neighbor,Double.MAX_VALUE);

            if(newCost < oldCost){
                parent.put(neighbor, actual);
                cost.put(neighbor, newCost);
                Double priority = newCost + heuristic(neighbor, end);
                open.add(new Node(neighbor, priority));
            }
        }
    }

    private List<Position> getNeighbors(Position position){
        List<Position> neighbors = new ArrayList<>();

        for (int i = 0; i < 4; i++){
            Integer newRow = position.getRow() + rowMoves[i];
            Integer newColumn = position.getColumn() + columnMoves[i];

            Position neighbor = new Position(newRow,newColumn);

            if(isInBounds(neighbor)){
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    private boolean isInBounds(Position position) {
        return (position.getRow() >= 0) && (position.getRow() < rows) && (position.getColumn() >= 0) && (position.getColumn() < columns);
    }

    private List<Position> buildPath(Map<Position,Position> parent, Position start, Position end) {
        List<Position> path = new ArrayList<>();
        Position actual = end;
        while (!actual.equals(start)){
            path.add(actual);
            actual = parent.get(actual);

            if (actual == null){
                return  new ArrayList<>();
            }
        }
        Collections.reverse(path);
        return path;
    }

    private Double heuristic(Position start, Position end) {
        return (double) (Math.abs(start.getRow() - end.getRow()) + Math.abs(start.getColumn() - end.getColumn()));
    }
}
