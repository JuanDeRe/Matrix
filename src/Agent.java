import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Agent extends Thread implements Entity, Bot{

    private Double speed;

    private Position position;

    private Matrix matrix;

    private boolean isDead;

    private PathFinder pathFinder;

    private CountDownLatch startBarrier;

    public Agent(Double speed, Position position, Matrix matrix, CountDownLatch startBarrier){
        this.position = position;
        this.speed = speed;
        this.matrix = matrix;
        this.isDead = false;
        this.pathFinder = new PathFinder(matrix.getRows(), matrix.getColumns());
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
        return (long)Math.max(100,1000/speed);
    }

    private Position calculateNextMove() {
        List<Position> objectives = matrix.getNeosPositons();

        List<List<Position>> paths = new ArrayList<>();

        for (Position objective : objectives){
            List<Position> path = pathFinder.searchPath(getPosition(), objective,
                    position -> matrix.isAvailable(position),
                    position -> 1);
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
    public void kill(){
        this.isDead = true;
    }

    @Override
    public boolean isDead() {
        return isDead;
    }

    @Override
    public void moveTo(Position position) {
        this.position = position;
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
        return Role.AGENT;
    }

    @Override
    public boolean blocksPath() {
        return true;
    }

    @Override
    public String toString(){
        return "A";
    }

    public Double getDangerZone(){
        return speed*2;
    }

    public Double getDanger(){
        return speed*10;
    }
}
