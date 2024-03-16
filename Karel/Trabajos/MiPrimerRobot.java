import kareltherobot.*;
import java.awt.Color;
import karel.Racer;

public class MiPrimerRobot implements Directions {
    public static void main(String[] args) {
        // Usamos el archivo que creamos del mundo
        World.readWorld("Mundo.kwld");
        World.setVisible(true);

        // Crear el primer robot (Racer) en la posición (1, 1) y dirección Este
        Racer racer1 = new Racer(1, 1, East, 0);

        // Crear el segundo robot (Racer) en la posición (2, 1) y dirección Este
        Racer racer2 = new Racer(2, 1, East, 0);

        // Iniciar el hilo del primer robot (Racer)
        racer1.startThread();

        // Iniciar el hilo del segundo robot (Racer)
        racer2.startThread();
    }
}