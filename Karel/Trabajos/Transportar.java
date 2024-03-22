import java.util.ArrayList;
import java.util.List;

public class Transportar {
    private List<Tren> allTrenes = new ArrayList<>();
    private int capacidadTren = 7;

    public int getCapacidadTren() {
        return capacidadTren;
    }

    public List<Tren> getTrenes() {
        return allTrenes;
    }
}

