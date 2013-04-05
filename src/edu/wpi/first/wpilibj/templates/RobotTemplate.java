/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
	int autonomousState = 0;
    public void printMsg(String message) {
        userMessages.println(DriverStationLCD.Line.kMain6, 1, message );
        userMessages.updateLCD();
    }
    boolean deadband(double[] values){
    	for(int i = 0; i < 2; i++){
	    	if (values[i] < 0.5 && values[i] > -0.5){
	    		return true;
	    	}
    	}
    	return false;
    }
    RobotDrive drivetrain;
    //Relay spikeA;
    Joystick leftStick;
    Joystick rightStick;
    //public String controlScheme = "twostick";
    int leftStickX, leftStickY;
    DriverStationLCD userMessages;
    String controlScheme = "twostick";
    Timer timer;
    DigitalInput switchA, switchB;
    Jaguar launcher;
    Jaguar launcher2;
    Jaguar launcher3;
    //maybe more?
    double voltage;
    //DriverStation driverStation = new DriverStation();
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        //Instantialize objects for RobotTemplate
        
        //driverStation = new DriverStation();
        rightStick = new Joystick(1);
        leftStick  = new Joystick(2);
        userMessages = DriverStationLCD.getInstance();
        
        //2-Wheel tank drive
        //spikeA = new Relay(1);
        drivetrain = new RobotDrive(1,2);
        launcher = new Jaguar(5);
        //***CHANGE THESE PLACEHOLDER QUESTIONMARKS***
        //once jaguars are installed
        //launcher2 = new Jaguar(?);
        //launcher3 = new Jaguar(?);
        /*pistonUp = new Solenoid(1);
        pistonDown = new Solenoid(2);
        sol3 = new Solenoid(3);
        sol4 = new Solenoid(4);
        sol5 = new Solenoid(5);*/
        
        //4-Wheel tank drive
        //Motors must be set in the following order:
        //LeftFront=1; LeftRear=2; RightFront=3; RightRear=4;
        //drivetrain = new RobotDrive(1,2,3,4);
        //drivetrain.tankDrive(leftStick, rightStick);
        /*pistonDown.set(true);
        pistonUp.set(true);*/
        switchA = new DigitalInput(1);
        switchB = new DigitalInput(2);//remember to check port
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousInit() {
        
    	autonTimer.reset();
        autonTimer.start();
       
        autonomousState = 0;
       
    }
    Timer autonTimer = new Timer();
    public void autonomousPeriodic()
    {
       switch ( autonomousState )
       {
           case 0:
               drivetrain.drive(.30, 0);
               if ( autonTimer.get() > 7 ){
                   autonomousState++;
               }
               break;
           case 1:
               drivetrain.drive(0, 0);
               launcher.set(1.0);
               break;
               
               
       }
    }
    public void telopInit() {
        //drivetrain.setSafetyEnabled(true);
        //drivetrain.tankDrive(leftStick.getY(), rightStick.getY());
        //compressorA.start();
        printMsg("Teleop started.");
    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	//x is a placeholder variable
    	/*if(switchA.get()){//if switch isn't tripped
        	printMsg("Moving motor.");
        	//victor.set(0.5); //start motor
        }
        else{
        	printMsg("Motor stopped");
        	//victor.set(0); //stop motor
        }*/
        //getWatchdog().setEnabled(true);
    	
    	double[] values = {leftStick.getY(), rightStick.getY()};
    	if(deadband(values)){
    		drivetrain.stopMotor();
    	}
    	//double leftValue = leftStick.getY();
    	//double rightValue = rightStick.getY();
    	/*if(leftStick.getY() > 0){
    		leftValue -= 0.5;
    	}
    	else{
    		leftValue += 0.5;
    	}
    	if(rightStick.getY() > 0){
    		rightValue -= 0.5;
    	}
    	else{
    		rightValue += 0.5;
    	}*/
    	drivetrain.tankDrive(leftStick, rightStick);
        
        if (leftStick.getTrigger()) {
        	if(launcher.get() != -1){
        		launcher.set(-1);
        		//launcher2.set(-1);
        		//launcher3.set(-1);
        	}
            else {
            	launcher.set(0);
            	//launcher2.set(0);
        	//	launcher3.set(0);
            }
        } 
        
        if (rightStick.getTrigger()) {
        	if(launcher.get() != 1){
        		launcher.set(1);
        	//	launcher2.set(1);
        	//	launcher3.set(1);
        	}
            else {
            	launcher.set(0);
        //    	launcher2.set(0);
        //		launcher3.set(0);
            }
        }
    
        
      /*  //Switch between "onestick" and "twostick" control schemes
        if (leftStick.getRawButton(6)) {
            controlScheme = "twostick";
        }
        if (leftStick.getRawButton(7)) {
            controlScheme = "onestick";
        }
        
        if (controlScheme.equals("twostick")) {
            drivetrain.tankDrive(rightStick, leftStick);
            printMsg("Tankdrive activated.");
        }
        else if (controlScheme.equals("onestick")) {
            drivetrain.arcadeDrive(leftStick);
            printMsg("Arcade drive activated.");
        }*/
        
        /*if(switchA.get()){//if switch isn't tripped
        	printMsg("Moving motor.");
        	//victor.set(0.5); //start motor
        }
        else{
        	printMsg("Motor stopped");
        	//victor.set(0); //stop motor
        }*/
        //Rotate in-place left and right, respectively
        if (leftStick.getRawButton(8)) {
            drivetrain.setLeftRightMotorOutputs(-1.0, 1.0);
            printMsg("Rotating counterclockwise in place.");
        }
        if (leftStick.getRawButton(9)) {
            drivetrain.setLeftRightMotorOutputs(1.0, -1.0);
            printMsg("Rotating clockwise in place.");
        }
        
        //userMessages.println(DriverStationLCD.Line.kMain6, 1, "This is a test" );
        userMessages.updateLCD();
    }
    
    /*public void disabledInit() {
    }*/
    
}
