public class Obstacle implements Entity{

    private Position position;

    public Obstacle(Position position) {
        this.position = position;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public Role getRole() {
        return Role.OBSTACLE;
    }

    @Override
    public boolean blocksPath() {
        return true;
    }

    @Override
    public String toString(){
        return "#";
    }
}
