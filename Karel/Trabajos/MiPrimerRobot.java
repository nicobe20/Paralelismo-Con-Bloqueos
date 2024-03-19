import kareltherobot.*;
import java.awt.Color;

public class MiPrimerRobot implements Directions {

    public static class Workers extends Thread {
        private int street;
        private int avenue;
        private Direction direction;
        private int beepers;
        private Color color;

        public Workers(int street, int avenue, Direction direction, int beepers, Color color) {
            this.street = street;
            this.avenue = avenue;
            this.direction = direction;
            this.beepers = beepers;
            this.color = color;
        }

        public void run() {
            // Instanciar un robot con los parámetros proporcionados
            Racer worker = new Racer(street, avenue, direction, beepers, color);

            // Realizar el recorrido del robot
            worker.race();
        }
    }

    public static void createRobots(String[] args) {
        // Definición de valores por defecto
        int mineros = 1;
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

        // Creación de mineros
        for (int z = 0; z < mineros; z++) {
            Workers mineroBot = new Workers(7, 1, South, 0, Color.DARK_GRAY);
            mineroBot.start();
            try {
                // Pausa de 1000 milisegundos (1 segundo) entre la creación de cada robot
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Creación de trenes
        for (int y = 0; y < trenes; y++) {
            Workers trenesBot = new Workers(7, 1, South, 0, Color.CYAN);
            trenesBot.start();
            try {
                // Pausa de 1000 milisegundos (1 segundo) entre la creación de cada robot
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Creación de extractores
        for (int x = 0; x < extractores; x++) {
            Workers extractoresBot = new Workers(7, 1, South, 0, Color.RED);
            extractoresBot.start();
            try {
                // Pausa de 1000 milisegundos (1 segundo) entre la creación de cada robot
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
    public Racer(int street, int avenue, Direction direction, int beepers, Color color) {
        super(street, avenue, direction, beepers, color); // Utilizamos el constructor adecuado de la superclase Robot
        World.setupThread(this);
    }

    public void race() {
        // Mover hasta que se apague

        while (true) {
            if (frontIsClear()) {
                move();

            } else {
                turnLeft();
            }

            // if (nextToARobot()) {
            //     turnOff();
            // }
        }
        // Apagar al robot
    }

    public void run() {
        race();
    }
}
