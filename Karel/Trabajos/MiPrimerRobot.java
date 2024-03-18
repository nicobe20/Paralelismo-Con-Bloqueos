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
        int mineros = 2;
        int trenes = 2;
        int extractores = 2;

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
            Workers mineroBot = new Workers(10, 1, South, 0, Color.PINK);
            mineroBot.start();
        }

        // Creación de trenes
        for (int y = 0; y < trenes; y++) {
            Workers trenesBot = new Workers(12, 1, South, 0, Color.BLUE);
            trenesBot.start();
        }

        // Creación de extractores
        for (int x = 0; x < extractores; x++) {
            Workers extractoresBot = new Workers(14, 1, South, 0, Color.RED);
            extractoresBot.start();
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
        // Mover hacia adelante
        move();
        
        // Verificar si hay un beeper en la casilla actual y recogerlo si es así
        if (nextToABeeper()) {
            pickBeeper();
        }
        
        // Apagar al robot
        turnOff();
    }

    public void run() {
        race();
    }
}
