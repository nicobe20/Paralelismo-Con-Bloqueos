import java.util.ArrayList;
import java.util.List;

public class Extraer {

    private List<Extractor> allExtractores = new ArrayList<>();
    private int capacidadExtractor = 3;
    private int beepersEnPuertoExtractor = 0;
    private boolean puertoExtractorOcupado = false;

    public Extraer() {
        this.allExtractores = new ArrayList<>();
        this.capacidadExtractor = 3;
        this.beepersEnPuertoExtractor = 0;
        this.puertoExtractorOcupado = false;
    }

    public int getCapacidadExtractor() {
        return capacidadExtractor;
    }

    public List<Extractor> getExtractores() {
        return allExtractores;
    }

    public boolean getPuertoExtractor() {
        return puertoExtractorOcupado;
    }

    public int getBeepersPuertoExtractor() {
        return beepersEnPuertoExtractor;
    }

    public void ocuparPuertoExtractor() {
        puertoExtractorOcupado = true;
    }

    public void desocuparPuertoExtractor() {
        puertoExtractorOcupado = false;
    }

    public void incrementBeepersPuertoExtractor() {
        beepersEnPuertoExtractor++;
    }

    public void decrementBeepersPuertoExtractor() {
        beepersEnPuertoExtractor--;
    }
}
