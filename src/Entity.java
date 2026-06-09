public interface Entity {
    Position getPosition();
    void setPosition(Position position);
    Role getRole();
    boolean blocksPath();
    String toString();
}
