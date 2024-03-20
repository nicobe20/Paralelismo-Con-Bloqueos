import kareltherobot.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class MiPrimerRobot implements Directions {

    public static List<Minero> allMineros = new ArrayList<>();


    public static boolean ocupado = false;

    public static List<Minero> getMineros() {
        return allMineros;
    }

    public static boolean getMina() {
        return ocupado;
    }

    public static void updateMina() {
        ocupado = !ocupado;
    }

    public static class Workers extends Thread {
        private Minero minero; // Cada trabajador tiene su propio Minero
        private Racer worker; // Cada trabajador tiene su propio Racer (robot)

        public Workers(Minero minero, int street, int avenue, Direction direction, int beepers, Color color) {
            this.minero = minero;
            this.worker = new Racer(street, avenue, direction, beepers, color, minero); // Creamos el Racer con su
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
        // for (int y = 0; y < trenes; y++) {

        //     Workers trenesBot = new Workers(7, 1, South, 0, Color.CYAN, nuevoTren);
        //     trenesBot.start();
        //     try {
        //         Thread.sleep(2000);
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        // }

        // // Crear extractores
        // for (int x = 0; x < extractores; x++) {
        //     Workers extractoresBot = new Workers(7, 1, South, 0, Color.RED);
        //     extractoresBot.start();
        //     try {
        //         Thread.sleep(2000);
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        // }

    }

    public static void main(String[] args) {
        World.readWorld("Mundo.kwld");
        World.setVisible(true);
        createRobots(args);
    }
}

class Racer extends Robot implements Runnable {
    private Minero minero; // Cada Racer tiene su propio Minero asociado
    private Color robotColor;

    public Racer(int street, int avenue, Direction direction, int beepers, Color color, Minero minero) {
        super(street, avenue, direction, beepers, color);
        this.minero = minero; // Asignamos el Minero asociado al Racer

        World.setupThread(this);
        World.setDelay(50);
    }

    public void race() {
        while (true) {

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

                }

            } else if (minero.getStreet() == 11 && minero.getAvenue() == 13 && facingWest()) {
                MiPrimerRobot.updateMina();
                if (anyBeepersInBeeperBag()) {
                    for (int i = 0; i < 20; i++) {
                        putBeeper();
                    }
                }
                ubicarMineroPuntoEspera(minero);

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

    private void recogerBeeper() {
        minero.updateInsideTheMine();
        MiPrimerRobot.updateMina();
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