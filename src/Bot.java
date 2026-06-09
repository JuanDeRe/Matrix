public interface Bot {

    Double getSpeed();

    boolean isDead();

    void moveTo(Position position);

    void kill();

    Position getPosition();
}
