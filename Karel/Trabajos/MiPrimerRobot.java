import kareltherobot.*; 
import java.awt.Color;

public class MiPrimerRobot implements Directions 
{ 
public static void main(String [] args) 
{ 
//usamos mundo!
World.readWorld("Mundo.kwld"); 
World.setVisible(true); 


// Coloca el robot en la posición inicial del mundo (1,1), 
// mirando al Este, sin ninguna sirena. 
Robot K1 = new Robot(1, 1, East, 0); 
Robot K2 = new Robot(2, 1, East, 0);
 
 
// Mover el robot 4 pasos 
  K1.move();
  K2.turnLeft();
  K2.turnLeft();
  K2.turnLeft();   
  K1.move(); 
  K2.move();
  K1.move(); 
  K2.move();
  K1.move(); 
  K2.move();
  K2.move();
// Recoger los 5 beepers 
  K1.pickBeeper(); 
  K1.pickBeeper(); 
  K1.pickBeeper(); 
  K1.pickBeeper(); 
  K1.pickBeeper(); 
// Girar a la izquierda y salir de los muros 
  K1.turnLeft(); 
  K2.turnLeft();
  K1.move(); 
  K2.move();
  K1.move(); 
  K2.move();
// Poner los beepers fuera de los muros 
  K1.putBeeper(); 
  K1.putBeeper(); 
  K1.putBeeper(); 
  K1.putBeeper(); 
  K1.putBeeper(); 
// Ponerse en otra posición y apagar el robot 
  K1.move();
  K2.move();
  K2.turnLeft(); 
  K2.move();
  K2.turnOff(); 
  K1.turnOff(); 
} 
}