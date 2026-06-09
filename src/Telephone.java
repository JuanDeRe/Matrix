public class Telephone implements Entity{
    private Position position;

    public Telephone(Position position) {
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
        return Role.TELEPHONE;
    }

    @Override
    public boolean blocksPath() {
        return false;
    }
    @Override
    public String toString(){
        return "T";
    }
}
