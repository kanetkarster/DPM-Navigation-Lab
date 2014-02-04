/*
 * Lab3.java
 * 
 * Called on start; Controls what the robot does.
 */
import lejos.nxt.*;

public class Lab3 {
	public static double xDest, yDest;
	public static void main(String[] args) {
		int buttonChoice;
		// some objects that need to be instantiated
		UltrasonicSensor myUS = new UltrasonicSensor(SensorPort.S2);
		Odometer odometer = new Odometer();
		Driver navigator = new Driver(odometer);
		OdometryDisplay odometryDisplay = new OdometryDisplay(odometer);
		Avoid avoid = new Avoid(myUS, odometer, navigator);
		do {
			// clear the display
			LCD.clear();

			// ask the user whether the motors should Avoid Block or Go to locations
			LCD.drawString("< Left | Right >", 0, 0);
			LCD.drawString("       |        ", 0, 1);
			LCD.drawString(" Avoid | Drive  ", 0, 2);
			LCD.drawString(" Block | to loc   ", 0, 3);
			LCD.drawString("       | ations", 0, 4);

			buttonChoice = Button.waitForAnyPress();
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);

		if (buttonChoice == Button.ID_LEFT) {
			LCD.clear();
			//starts the avoid thread
			avoid.start();
			//starts odometer thread
			odometer.start();
			//starts odometer display thread
			odometryDisplay.start();
			//initializes locations
			double[][] locations = {{0, 60}, {60, 0}};
			for (double[] d : locations){
				//tells robot to travel
				xDest = d[0];
				yDest = d[1];
				navigator.travel(d[0], d[1]);
				//Stores Destination for rerouting
			}
		} else {
			odometer.start();
			odometryDisplay.start();
			navigator.start();
			
			navigator.travel(60, 30);
			navigator.travel(30, 30);
			navigator.travel(30, 60);
			navigator.travel(60, 0);
		}

		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);
	}
}