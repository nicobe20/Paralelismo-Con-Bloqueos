import kareltherobot.*;
import java.awt.Color;

public class MiPrimerRobot implements Directions {

    public static class Workers extends Racer {
        
    public Workers(int street, int avenue, Direction direction, int beepers, Color color) {
        super(street, avenue, direction, beepers);
       
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
            Workers mineroBot = new Workers(10, 1, South, 0, Color.BLACK);
        }

        // Creación de trenes
        for (int y = 0; y < trenes; y++) {
            Workers trenesBot = new Workers(12, 1, South, 0, Color.BLUE);
        }

        // Creación de extractores
        for (int x = 0; x < extractores; x++) {
            Workers extractoresBot = new Workers(14, 1, South, 0, Color.RED);
        }
    }

    public static void main(String[] args) {
        World.readWorld("Mundo.kwld");
        World.setVisible(true);
        createRobots(args);
    }
}
