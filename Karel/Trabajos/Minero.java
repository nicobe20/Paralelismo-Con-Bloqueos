

public class Minero {
    private int street;
    private int avenue;

    public Minero(int street, int avenue, boolean insideTheMine) {
        this.street = street;
        this.avenue = avenue;
    }

    public int getStreet() {
        return street;
    }

    public int getAvenue() {
        return avenue;
    }

    public void incrementStreet() {
        street++;
    }

    public void incrementAvenue() {
        avenue++;
    }

    public void decreaseStreet() {
        street--;
    }

    public void decreaseAvenue() {
        avenue--;
    }
}


