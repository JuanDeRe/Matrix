import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Matrix {
    private Entity[][] matrix;
    private List<Agent> agents;
    private List<Telephone> telephones;
    private List<Neo> neos;
    private List<Obstacle> obstacles;
    private CountDownLatch startBarrier;
    private volatile boolean finished;

    public Matrix(Integer rows, Integer columns, List<Position> obstacles, List<Position> telephones, List<Position> neos, List<Position> agents, Double neoSpeed, List<Double> agentSpeed){
        this.matrix = new Entity[rows][columns];
        this.finished = false;
        this.startBarrier = new CountDownLatch(1);
        setObstacles(obstacles);
        setTelephones(telephones);
        setAgents(agents, agentSpeed, startBarrier);
        setNeos(neos, neoSpeed, startBarrier);
    }

    public void start(){
        for(Agent agent : agents){
            agent.start();
        }
        for(Neo neo : neos){
            neo.start();
        }
        startBarrier.countDown();
    }

    private void setNeos(List<Position> neos, Double neoSpeed, CountDownLatch startBarrier) {
        this.neos = new ArrayList<>();
        for (Position position : neos){
            if(isInBounds(position) && isEmpty(position)){
                Neo neo = new Neo(neoSpeed, new Position(position.getRow(), position.getColumn()), this, startBarrier);
                this.neos.add(neo);
                matrix[position.getRow()][position.getColumn()] = neo;
            }
        }
    }

    private void setAgents(List<Position> agents, List<Double> agentSpeeds, CountDownLatch startBarrier) {
        this.agents = new ArrayList<>();

        for (int i = 0; i < agents.size(); i++) {
            Position position = agents.get(i);

            if (isInBounds(position) && isEmpty(position)) {
                Double speed = agentSpeeds.get(i % agentSpeeds.size());

                Agent agent = new Agent(
                        speed,
                        new Position(position.getRow(), position.getColumn()),
                        this,
                        startBarrier
                );

                this.agents.add(agent);
                matrix[position.getRow()][position.getColumn()] = agent;
            }
        }
    }

    private void setTelephones(List<Position> telephones) {
        this.telephones = new ArrayList<>();
        for (Position position : telephones){
            if(isInBounds(position) && isEmpty(position)){
                Telephone telephone = new Telephone(new Position(position.getRow(), position.getColumn()));
                this.telephones.add(telephone);
                matrix[position.getRow()][position.getColumn()] = telephone;
            }
        }
    }

    private void setObstacles(List<Position> obstacles) {
        this.obstacles = new ArrayList<>();
        for (Position position : obstacles){
            if(isInBounds(position) && isEmpty(position)){
                Obstacle obstacle = new Obstacle(new Position(position.getRow(), position.getColumn()));
                this.obstacles.add(obstacle);
                matrix[position.getRow()][position.getColumn()] = obstacle;
            }
        }
    }

    private boolean isInBounds(Position position){
        return (position.getRow() >= 0) && (position.getRow() < matrix.length) && (position.getColumn() >= 0) && (position.getColumn() < matrix[0].length);
    }

    public Entity getEntity(Position position){
        return matrix[position.getRow()][position.getColumn()];
    }

    public Double positionWeight(Position position){
        Double weight = 1.0;

        for (Agent agent : agents){
            Integer distance = distance(position, agent.getPosition());
            Double area = agent.getDangerZone();

            if (distance <= area){
                Double danger = agent.getDanger();
                Double extraDanger = danger * (area - distance + 1) / (area + 1);
                weight += extraDanger;
            }
        }
        return weight;
    }

    public synchronized boolean isAvailable(Position position) {
        if (!isInBounds(position)) {
            return false;
        }

        Entity entity = getEntity(position);

        return entity == null || !entity.blocksPath();
    }

    public boolean isEmpty(Position position){
        return getEntity(position) == null;
    }

    public List<Position> getNeosPositons(){
        List<Position> neosPositions = new ArrayList<>();
        for (Neo neo : neos){
            neosPositions.add(neo.getPosition());
        }
        return neosPositions;
    }

    private Integer distance(Position a, Position b){
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getColumn() - b.getColumn());
    }

    public boolean isFinished() {
        return this.finished;
    }

    public synchronized boolean moveEntity(Entity entity, Position next) {
        if (!isInBounds(next)) {
            return false;
        }

        Entity target = matrix[next.getRow()][next.getColumn()];
        if (target != null && target.blocksPath()) {
            return false;
        }
        if (target != null) {
            if (entity.getRole().equals(Role.NEO) && target.getRole().equals(Role.TELEPHONE)) {
                this.finished = true;
            }

            if (entity.getRole().equals(Role.AGENT) && target.getRole().equals(Role.NEO)) {
                this.finished = true;
            }
        }

        Position current = entity.getPosition();

        matrix[current.getRow()][current.getColumn()] = null;
        matrix[next.getRow()][next.getColumn()] = entity;

        entity.setPosition(next);

        return true;
    }

    public List<Position> getTelephonesPosition() {
        List<Position> telephonesPositions = new ArrayList<>();
        for (Telephone telephone : telephones){
            telephonesPositions.add(telephone.getPosition());
        }
        return telephonesPositions;
    }

    public Integer getRows(){
        return this.matrix.length;
    }

    public Integer getColumns(){
        return this.matrix[0].length;
    }
    @Override
    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                Entity entity = matrix[i][j];

                sb.append(colorize(entity));
                sb.append(" ");
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    private String colorize(Entity entity) {
        if (entity == null) {
            return ".";
        }

        String value = entity.toString();

        return switch (entity.getRole()) {
            case NEO -> ConsoleColors.GREEN + value + ConsoleColors.RESET;
            case AGENT -> ConsoleColors.RED + value + ConsoleColors.RESET;
            case OBSTACLE -> ConsoleColors.PURPLE + value + ConsoleColors.RESET;
            case TELEPHONE -> ConsoleColors.YELLOW + value + ConsoleColors.RESET;
        };
    }
}
