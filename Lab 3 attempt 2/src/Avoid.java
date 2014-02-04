/*
 * Thread which has the robot avoid blocks
 * Whenver the sensor detects a block it calls the avoid method
 */
import lejos.nxt.*;
import java.lang.System;
public class Avoid extends Thread{
	
	private final int MIN_DISTANCE = 10;
	private static double destX;
	private static double destY;
	
	private Odometer odometer;
	private UltrasonicSensor us;
	private Driver driver;
	
	public Avoid(UltrasonicSensor us, Odometer odometer, Driver driver){
		this.odometer = odometer;
		this.us = us;
		this.driver = driver;
	}
	/**
	 * Called when Avoid thread is started
	 * When it detects an object, the avoidBlock scenario is used
	 */
	@Override
	public void run(){
		while(true){
			//starts process if sensor detects a block
			if(us.getDistance() < MIN_DISTANCE){
				//stops motors, just in case
				Motor.A.stop();
				Motor.B.stop();
				//stores locations it is driving to, in case of obstacle avoidance
				destX = Lab3.xDest;				
				destY = Lab3.yDest;
				//starts to avoid block
				avoidBlock();
				//after it is done avoiding the block, it starts traveling to the previous destination
				driver.travel(destX, destY);
			}
			try { Thread.sleep(10); } catch(Exception e){}
		}
	}
	/**
	 * Has the robot avoid blocks
	 * 
	 * Turns 90 deg
	 * Goes straight a specified distance
	 * Turns back to earlier position
	 * 
	 */
	public void avoidBlock(){
		//rotates 90 degrees clockwise
		driver.turnTo(90);
		//moves forwards
		driver.goForward(40);
		//rotates 90 degrees counterclockwise
		driver.turnTo(-90);
		//recursively calls avoidBlock if it is still in sight
		if(us.getDistance() < MIN_DISTANCE){
			avoidBlock();
		}
	}
}
