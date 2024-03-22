import java.util.ArrayList;
import java.util.List;

public class Minar {

    private List<Minero> allMineros;
    private int capacidadMinero;
    private int beepersEnPuertoMina;
    private boolean puertoMinaOcupado;
    private boolean ocupado;

    public Minar() {
        this.allMineros = new ArrayList<>();
        this.capacidadMinero = 3;
        this.beepersEnPuertoMina = 0;
        this.puertoMinaOcupado = false;
        this.ocupado = false;
    }

    public int getCapacidadMinero() {
        return capacidadMinero;
    }

    public List<Minero> getMineros() {
        return allMineros;
    }

    public boolean getMina() {
        return ocupado;
    }

    public int getBeepersPuertoMina() {
        return beepersEnPuertoMina;
    }

    public boolean getPuertoMina() {
        return puertoMinaOcupado;
    }

    public void updateMina() {
        ocupado = !ocupado;
    }

    public void truePuertoMina() {
        puertoMinaOcupado = true;
    }

    public void falsePuertoMina() {
        puertoMinaOcupado = false;
    }

    public void incrementBeepersPuertoMina() {
        beepersEnPuertoMina++;
    }

    public void decrementBeepersPuertoMina() {
        beepersEnPuertoMina--;
    }
}