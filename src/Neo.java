import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Neo extends Thread implements Entity, Bot{

    private Double speed;

    private Position position;

    private boolean isDead;

    private Matrix matrix;

    private PathFinder pathFinder;

    private CountDownLatch startBarrier;


    public Neo(Double speed, Position position, Matrix matrix, CountDownLatch startBarrier) {
        this.speed = speed;
        this.position = position;
        this.matrix = matrix;
        this.pathFinder = new PathFinder(matrix.getRows(),matrix.getColumns());
        this.isDead = false;
        this.startBarrier = startBarrier;
    }

    public void run(){
        try {
            startBarrier.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        while (!isDead() && !matrix.isFinished()){
            Position next = calculateNextMove();
            System.out.println("Neo calculando siguiente movimiento...");

            if (next != null){
                boolean moved = matrix.moveEntity(this, next);
                if (!moved){
                    continue;
                }
            }
            try {
                Thread.sleep(waitTime());
            } catch (InterruptedException e) {
                kill();
                throw new RuntimeException(e);
            }
        }
    }

    private long waitTime() {
        return (long)(Math.max(100,1000/speed));
    }

    private Position calculateNextMove() {
        List<Position> objectives = matrix.getTelephonesPosition();
        List<List<Position>> paths = new ArrayList<>();

        for (Position objective : objectives){
            List<Position> path = pathFinder.searchPath(getPosition(), objective,
                    position -> matrix.isAvailable(position),
                    position -> matrix.positionWeight(position));
            if (!path.isEmpty()){
                paths.add(path);
            }
        }
        return paths.stream()
                .min(Comparator.comparingInt(List::size))
                .map(path -> path.get(0))
                .orElse(null);
    }

    @Override
    public boolean isDead() {
        return isDead;
    }

    @Override
    public void moveTo(Position position) {

    }

    @Override
    public void kill() {
        isDead = true;
    }

    @Override
    public Position getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public Double getSpeed() {
        return this.speed;
    }

    @Override
    public Role getRole() {
        return Role.NEO;
    }

    @Override
    public boolean blocksPath() {
        return false;
    }

    @Override
    public String toString() {
        return "N";
    }
}
