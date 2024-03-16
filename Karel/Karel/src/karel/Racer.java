import kareltherobot.*;

public class Racer extends Robot {
    public Racer(int street, int avenue, Direction direction, int beepers) {
        super(street, avenue, direction, beepers);
        World.setupThread(this);
    }

    public void race() {
        while (!nextToABeeper())
            move();
        pickBeeper();
        turnOff();
    }

    public void run() {
        race();
    }
}
