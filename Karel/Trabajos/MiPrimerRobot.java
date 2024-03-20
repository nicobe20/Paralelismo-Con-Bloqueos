import kareltherobot.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class MiPrimerRobot implements Directions {

    public static List<Minero> allMineros = new ArrayList<>();
    public static List<Tren> allTrenes = new ArrayList<>();

    public static boolean ocupado = false;

    public static int beepersEnPuertoMina = 0;
    public static boolean mineroDescargando = false;

    public static List<Minero> getMineros() {
        return allMineros;
    }

    public static boolean getMina() {
        return ocupado;
    }

    public static boolean getPuertoMina() {
        return mineroDescargando;
    }

    public static int getBeepersPuertoMina() {
        return beepersEnPuertoMina;
    }

    public static void updateMina() {
        ocupado = !ocupado;
    }

    public static void truePuertoMina() {
        mineroDescargando = true;
    }

    public static void falsePuertoMina() {
        mineroDescargando = false;
    }

    public static void incrementBeepersPuertoMina() {
        beepersEnPuertoMina++;
    }

    public static void decrementBeepersPuertoMina() {
        beepersEnPuertoMina--;
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
        int trenes = 1;
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
        // for (int x = 0; x < extractores; x++) {
        // Workers extractoresBot = new Workers(7, 1, South, 0, Color.RED);
        // extractoresBot.start();
        // try {
        // Thread.sleep(2000);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // }

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
    private Color robotColor;

    public Racer(int street, int avenue, Direction direction, int beepers, Color color, Object mainObject) {
        super(street, avenue, direction, beepers, color);
        robotColor = color;

        if (mainObject instanceof Minero) {
            this.minero = (Minero) mainObject; // Asignamos el Minero asociado al Racer
        } else if (mainObject instanceof Tren) {
            this.tren = (Tren) mainObject; // Asignamos el Minero asociado al Racer

        }

        World.setupThread(this);
        World.setDelay(50);
    }

    public void race() {
        while (true) {
            if (robotColor.equals(Color.DARK_GRAY)) {

                if (minero.getStreet() == 11 && minero.getAvenue() == 8 && facingSouth()) {
                    turnLeft();
                }

                if (minero.getStreet() == 11 && minero.getAvenue() == 14) {
                    if (facingEast()) {
                        recogerBeeper();
                    } else if (facingNorth()) {
                        while (!facingEast()) {
                            turnLeft();
                        }
                        recogerBeeper();

                    } else if (facingWest()) {
                        while (MiPrimerRobot.getPuertoMina()) {
                            ponerEnEspera();
                        }
                    }

                    // Para poner el cargamento
                }

                if (minero.getStreet() == 11 && minero.getAvenue() == 13) {
                    if (facingWest()) {
                        MiPrimerRobot.truePuertoMina(); // Decimos que el puerto mina esta "ocupado"

                        MiPrimerRobot.updateMina(); // Decimos que la mina esta "desocupada"
                        if (anyBeepersInBeeperBag()) {
                            for (int i = 0; i < 20; i++) {
                                putBeeper();
                                MiPrimerRobot.incrementBeepersPuertoMina();
                            }
                        }
                        MiPrimerRobot.falsePuertoMina(); // Decimos que el puerto mina esta "desocupada"
                        ubicarMineroPuntoEspera(minero);

                    }
                }

                if (minero.getStreet() == 11 && minero.getAvenue() == 13 && MiPrimerRobot.getMina()) {
                    for (Minero mineroActual : MiPrimerRobot.getMineros()) {
                        if (mineroActual.getInsideTheMine() == false) {
                            ubicarMineroPuntoEspera(mineroActual);
                        }
                    }
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
                    while (MiPrimerRobot.getPuertoMina() == true || MiPrimerRobot.getBeepersPuertoMina() < 20) {
                        ponerEnEspera();
                    }
                    move();
                    updateTrenPosition(tren);
                }

                if (tren.getStreet() == 11 && tren.getAvenue() == 13) {
                    MiPrimerRobot.truePuertoMina();
                    if (nextToABeeper()) {
                        for (int i = 0; i < 20; i++) {
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

                if (frontIsClear()) {
                    move();
                    updateTrenPosition(tren);
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

    private void recogerBeeper() {
        minero.updateInsideTheMine();
        MiPrimerRobot.updateMina(); // Decimos que la mina esta "Ocupada"
        if (nextToABeeper()) {
            for (int i = 0; i < 20; i++) {
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
        move();
        updateMineroPosition(minero);
        turnLeft();
        move();
        updateMineroPosition(minero);
        turnLeft();

        while (MiPrimerRobot.getMina()) {
            ponerEnEspera();
        }
    }

    public void run() {
        race();
    }

}

class Minero {
    private int street;
    private int avenue;
    private boolean insideTheMine;

    public Minero(int street, int avenue, boolean insideTheMine) {
        this.street = street;
        this.avenue = avenue;
        this.insideTheMine = insideTheMine;
    }

    public int getStreet() {
        return street;
    }

    public int getAvenue() {
        return avenue;
    }

    public boolean getInsideTheMine() {
        return insideTheMine;
    }

    public void updateInsideTheMine() {
        insideTheMine = !insideTheMine;
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
    // private boolean insideTheMine;

    public Tren(int street, int avenue) {
        this.street = street;
        this.avenue = avenue;
        // this.insideTheMine = insideTheMine;
    }

    public int getStreet() {
        return street;
    }

    public int getAvenue() {
        return avenue;
    }

    // public boolean getInsideTheMine() {
    // return insideTheMine;
    // }

    // public void updateInsideTheMine() {
    // insideTheMine = !insideTheMine;
    // }

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