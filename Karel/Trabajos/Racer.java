class Racer extends Robot 
{ 
public Racer(int Street, int Avenue, Direction direction, int beeps) 
{ 
super(Street, Avenue, direction, beeps); 
World.setupThread(this); 
} 
public void race() 
{ 
while(! nextToABeeper()) 
move(); 
pickBeeper(); 
turnOff(); 
} 
public void run() 
{  
race(); 
} 
}