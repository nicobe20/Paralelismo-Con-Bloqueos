import kareltherobot.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class MiPrimerRobot implements Directions {

    public static int capacidadMinero = 25;
    public static int capacidadTren = 60;
    public static int capacidadExtractor = capacidadMinero;

    public static List<Minero> allMineros = new ArrayList<>();
    public static List<Tren> allTrenes = new ArrayList<>();
    public static List<Extractor> allExtractores = new ArrayList<>();

    public static int beepersEnPuertoMina = 0;
    public static int beepersEnPuertoExtractor = 0;

    public static boolean ocupado = false;
    public static boolean puertoMinaOcupado = false;
    public static boolean puertoExtractorOcupado = false;

    public static int getCapacidadMinero() {
        return capacidadMinero;
    }

    public static int getCapacidadTren() {
        return capacidadTren;
    }

    public static int getCapacidadExtractor() {
        return capacidadExtractor;
    }

    public static List<Minero> getMineros() {
        return allMineros;
    }

    public static List<Tren> getTrenes() {
        return allTrenes;
    }

    public static boolean getMina() {
        return ocupado;
    }

    public static boolean getPuertoMina() {
        return puertoMinaOcupado;
    }

    public static boolean getPuertoExtractor() {
        return puertoExtractorOcupado;
    }

    public static int getBeepersPuertoMina() {
        return beepersEnPuertoMina;
    }

    public static int getBeepersPuertoExtractor() {
        return beepersEnPuertoExtractor;
    }

    public static void updateMina() {
        ocupado = !ocupado;
    }

    public static void truePuertoMina() {
        puertoMinaOcupado = true;
    }

    public static void falsePuertoMina() {
        puertoMinaOcupado = false;
    }

    public static void ocuparPuertoExtractor() {
        puertoExtractorOcupado = true;
    }

    public static void desocuparPuertoExtractor() {
        puertoExtractorOcupado = false;
    }

    public static void incrementBeepersPuertoMina() {
        beepersEnPuertoMina++;
    }

    public static void decrementBeepersPuertoMina() {
        beepersEnPuertoMina--;
    }

    public static void incrementBeepersPuertoExtractor() {
        beepersEnPuertoExtractor++;
    }

    public static void decrementBeepersPuertoExtractor() {
        beepersEnPuertoExtractor--;
    }

    public static class Workers extends Thread {
        private Object mainObject; // Objeto principal (Minero, Tren, Extractor)
        private Racer worker;

        public Workers(Object mainObject, int street, int avenue, Direction direction, int beepers, Color color) {
            this.mainObject = mainObject;
            this.worker = new Racer(street, avenue, direction, beepers, color, mainObject); // Creamos el Racer con el
                                                                                            // objeto principal
        }

        public void run() {
            worker.race(); // Ejecutamos el movimiento del Racer
        }
    }

    public static void createRobots(String[] args) {
        // Definición de valores por defecto
        int mineros = 2;
        int trenes = 4;
        int extractores = 1;

        // Procesamiento de argumentos de línea de comandos
        if (args.length > 0) {
            for (int i = 0; i < args.length; i += 2) {
                if (args[i].equals("-m")) {
                    mineros = Integer.parseInt(args[i + 1]);
                } else if (args[i].equals("-t")) {
                    trenes = Integer.parseInt(args[i + 1]);
                } else if (args[i].equals("-e")) {
                    extractores = Integer.parseInt(args[i + 1]);
                }
            }
        }

        // Crear mineros
        for (int z = 0; z < mineros; z++) {
            Minero nuevoMinero = new Minero(7, 1, false);
            allMineros.add(nuevoMinero);

            Workers mineroBot = new Workers(nuevoMinero, 7, 1, South, 0, Color.DARK_GRAY);
            mineroBot.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // // Crear trenes
        for (int y = 0; y < trenes; y++) {
            Tren nuevoTren = new Tren(7, 1);
            allTrenes.add(nuevoTren);
            Workers trenesBot = new Workers(nuevoTren, 7, 1, South, 0, Color.CYAN);
            trenesBot.start();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // // Crear extractores
        for (int x = 0; x < extractores; x++) {
            Extractor nuevoTren = new Extractor(7, 1);
            allExtractores.add(nuevoTren);
            Workers extractoresBot = new Workers(nuevoTren, 7, 1, South, 0, Color.RED);
            extractoresBot.start();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        World.readWorld("Mundo.kwld");
        World.setVisible(true);
        createRobots(args);
    }
}

class Racer extends Robot implements Runnable {
    private Minero minero;
    private Tren tren;
    private Extractor extractor;
    private Color robotColor;

    public Racer(int street, int avenue, Direction direction, int beepers, Color color, Object mainObject) {
        super(street, avenue, direction, beepers, color);
        robotColor = color;

        if (mainObject instanceof Minero) {
            this.minero = (Minero) mainObject; // Asignamos el Minero asociado al Racer
        } else if (mainObject instanceof Tren) {
            this.tren = (Tren) mainObject; // Asignamos el Minero asociado al Racer
        } else if (mainObject instanceof Extractor) {
            this.extractor = (Extractor) mainObject; // Asignamos el Minero asociado al Racer
        }

        World.setupThread(this);
        World.setDelay(40);
    }

    public void race() {
        while (true) {
            if (robotColor.equals(Color.DARK_GRAY)) {

                if (minero.getStreet() == 11 && minero.getAvenue() == 8 && facingSouth()) {
                    turnLeft();
                }

                if (minero.getStreet() == 11 && minero.getAvenue() == 14) {
                    if (facingEast()) {
                        mineroPickBeeper();
                    } else if (facingNorth()) {
                        while (!facingEast()) {
                            turnLeft();
                        }
                        mineroPickBeeper();

                    } else if (facingWest()) {
                        while (MiPrimerRobot.getPuertoMina()) {
                            ponerEnEspera();
                        }
                    }
                }

                if (minero.getStreet() == 11 && minero.getAvenue() == 13) {
                    if (facingWest()) {
                        MiPrimerRobot.truePuertoMina(); // Decimos que el puerto mina esta "ocupado"

                        MiPrimerRobot.updateMina(); // Decimos que la mina esta "desocupada"
                        if (anyBeepersInBeeperBag()) {
                            for (int i = 0; i < MiPrimerRobot.getCapacidadMinero(); i++) {
                                putBeeper();
                                MiPrimerRobot.incrementBeepersPuertoMina();
                            }
                        }
                        ubicarMineroPuntoEspera(minero);
                    }
                }

                if (minero.getStreet() == 11 && minero.getAvenue() == 13 &&
                        MiPrimerRobot.getMina()) {
                    ubicarMineroPuntoEspera(minero);
                }

                if (frontIsClear()) {
                    move();
                    updateMineroPosition(minero);
                } else {
                    turnLeft();
                }
            } else if (robotColor.equals(Color.CYAN)) {

                if (tren.getStreet() == 11 && tren.getAvenue() == 8 && facingSouth()) {
                    turnLeft();
                }

                if (tren.getStreet() == 11 && tren.getAvenue() == 12 && facingEast()) {
                    while (MiPrimerRobot.getPuertoMina() == true
                            || MiPrimerRobot.getBeepersPuertoMina() < MiPrimerRobot.getCapacidadTren()) {
                        ponerEnEspera();
                    }
                    move();
                    updateTrenPosition(tren);
                }

                if (tren.getStreet() == 11 && tren.getAvenue() == 13) {
                    MiPrimerRobot.truePuertoMina();
                    if (nextToABeeper()) {
                        for (int i = 0; i < MiPrimerRobot.getCapacidadTren(); i++) {
                            pickBeeper();
                            MiPrimerRobot.decrementBeepersPuertoMina();
                        }
                        turnLeft();
                        turnLeft();
                        turnLeft();
                        MiPrimerRobot.falsePuertoMina();
                    }
                }

                if (tren.getStreet() == 6 && tren.getAvenue() == 13) {
                    turnLeft();
                }

                if (tren.getStreet() == 2 && tren.getAvenue() == 3) {
                    while (MiPrimerRobot.getPuertoExtractor()) {
                        ponerEnEspera();
                    }
                }

                if (tren.getStreet() == 1 && tren.getAvenue() == 3 && facingEast()) {
                    if (anyBeepersInBeeperBag()) {
                        MiPrimerRobot.ocuparPuertoExtractor(); // decimos que puerto extractor esta ocupado
                        for (int i = 0; i < MiPrimerRobot.getCapacidadTren(); i++) {
                            putBeeper();
                            MiPrimerRobot.incrementBeepersPuertoExtractor();
                        }
                    }
                    MiPrimerRobot.desocuparPuertoExtractor();
                }

                verificarYEsperarSiFrenteOcupado(11);
                verificarYEsperarSiFrenteOcupado(1);

                if (tren.getAvenue() == 8 && facingNorth()) {
                    while (true) {
                        boolean frenteOcupado = false;
                        int streetCurrentTrain = tren.getStreet();

                        for (Tren trenActual : MiPrimerRobot.getTrenes()) {
                            int street = trenActual.getStreet();
                            if (street > streetCurrentTrain && (street - 1) == streetCurrentTrain && facingNorth()
                                    && trenActual.getAvenue() == 8) {
                                frenteOcupado = true;
                                ponerEnEspera();
                                break;
                            }
                            if (streetCurrentTrain == 5 && streetCurrentTrain + 2 == trenActual.getStreet()
                                    && facingNorth() && trenActual.getAvenue() == 8) {
                                frenteOcupado = true;
                                ponerEnEspera();
                                break;
                            }
                        }
                        if (!frenteOcupado) {
                            break;
                        }
                    }
                }

                if (frontIsClear()) {
                    move();
                    updateTrenPosition(tren);
                } else {
                    turnLeft();
                }
            } else if (robotColor.equals(Color.RED)) {

                if (extractor.getStreet() == 1 && extractor.getAvenue() == 2) {
                    while (MiPrimerRobot.getBeepersPuertoExtractor() < MiPrimerRobot.getCapacidadExtractor() || MiPrimerRobot.getPuertoExtractor()) {
                        ponerEnEspera();
                    }
                }

                if (extractor.getStreet() == 1 && extractor.getAvenue() == 3 && nextToABeeper()) {
                    MiPrimerRobot.ocuparPuertoExtractor();
                    for (int i = 0; i < MiPrimerRobot.getCapacidadExtractor(); i++) {
                        pickBeeper();
                    }
                    turnLeft();
                    turnLeft();
                    MiPrimerRobot.desocuparPuertoExtractor();

                }

                if (extractor.getAvenue() == 1 && extractor.getStreet() == 1 && facingWest()) {
                    turnLeft();
                    turnLeft();
                    turnLeft();
                }

                if (extractor.getAvenue() == 1 && extractor.getStreet() == 7 && facingNorth()) {
                    turnLeft();
                    turnLeft();
                    turnLeft();
                }

                if (extractor.getStreet() == 13 && extractor.getAvenue() == 2) {
                    for (int i = 0; i < MiPrimerRobot.getCapacidadExtractor(); i++) {
                        putBeeper();
                    }
                    turnLeft();
                    turnLeft();
                }

                if (extractor.getStreet() == 7 && extractor.getAvenue() == 2 && facingSouth()) {
                    turnLeft();
                    turnLeft();
                    turnLeft();
                }

                if (frontIsClear()) {
                    move();
                    updateExtractorPosition(extractor);
                } else {
                    turnLeft();
                }
            }
        }
    }

    // Método para actualizar la posición de un minero
    private void updateMineroPosition(Minero minero) {
        if (facingNorth()) {
            minero.incrementStreet();
        } else if (facingSouth()) {
            minero.decreaseStreet();
        } else if (facingEast()) {
            minero.incrementAvenue();
        } else if (facingWest()) {
            minero.decreaseAvenue();
        }
    }

    private void updateTrenPosition(Tren tren) {
        if (facingNorth()) {
            tren.incrementStreet();
        } else if (facingSouth()) {
            tren.decreaseStreet();
        } else if (facingEast()) {
            tren.incrementAvenue();
        } else if (facingWest()) {
            tren.decreaseAvenue();
        }
    }

    private void updateExtractorPosition(Extractor extractor) {
        if (facingNorth()) {
            extractor.incrementStreet();
        } else if (facingSouth()) {
            extractor.decreaseStreet();
        } else if (facingEast()) {
            extractor.incrementAvenue();
        } else if (facingWest()) {
            extractor.decreaseAvenue();
        }
    }

    private void mineroPickBeeper() {
        MiPrimerRobot.updateMina(); // Decimos que la mina esta "Ocupada"
        if (nextToABeeper()) {
            for (int i = 0; i < MiPrimerRobot.getCapacidadMinero(); i++) {
                pickBeeper();
            }
        }
    }

    private void ponerEnEspera() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void ubicarMineroPuntoEspera(Minero minero) {
        while (!facingSouth()) {
            turnLeft();
        }
        MiPrimerRobot.falsePuertoMina(); // Decimos que el puerto mina esta "desocupada"
        move();
        updateMineroPosition(minero);
        turnLeft();
        int posicion = MiPrimerRobot.allMineros.indexOf(minero);
        if (posicion != -1 && posicion > 1) {
            if (minero.getStreet() == 10 && minero.getAvenue() == 13 && facingEast()) {
                turnOff();
            }
        }
        move();
        updateMineroPosition(minero);
        turnLeft();

        while (MiPrimerRobot.getMina()) {
            ponerEnEspera();
        }
    }

    public void verificarYEsperarSiFrenteOcupado(int streetDeseado) {
        if (tren.getStreet() == streetDeseado && facingEast()) {
            while (true) {
                boolean frenteOcupado = false;
                int avenueCurrentTrain = tren.getAvenue();
                for (Tren trenActual : MiPrimerRobot.getTrenes()) {
                    int avenue = trenActual.getAvenue();
                    if (avenue > avenueCurrentTrain && avenue == (avenueCurrentTrain + 1) && facingEast()
                            && trenActual.getStreet() == streetDeseado) {
                        frenteOcupado = true;
                        ponerEnEspera();
                        break;
                    }
                }
                if (!frenteOcupado) {
                    break;
                }
            }
        }
    }

    public void run() {
        race();
    }

}

class Minero {
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

class Tren {
    private int street;
    private int avenue;

    public Tren(int street, int avenue) {
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

class Extractor {
    private int street;
    private int avenue;

    public Extractor(int street, int avenue) {
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