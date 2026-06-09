public class Node implements Comparable<Node>{
    private Position position;
    private Double priority;

    public Node(Position position, Double priority) {
        this.position = position;
        this.priority = priority;
    }

    public Position getPosition() {
        return position;
    }

    public Double getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(priority, o.priority);
    }
}
