package org.usfirst.frc.team20.robot;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends IterativeRobot  implements PIDOutput {
		RobotDrive MyDrive;
	    PIDController turnController;   
	    double rotateToAngleRate;
	    SpeedControllers speedController;
	    boolean cameraAngleCalculated = false;
	    int auto;
	    AHRS hrs;
	    Utils util;
	    boolean set;
	    float angleFromCamera = 0.00f;
	    double distanceFromCamera = 0.00;
	    String [] rocketScriptData;
	    RocketScript getNewScript = new RocketScript();
	    int rocketScriptLength = 0;
	    int rocketScriptCurrentCount;
	    
	@Override
	public void robotInit() {
		hrs = new AHRS(SerialPort.Port.kMXP);
		util = new Utils();
		speedController = new SpeedControllers();
		speedController.setDriveTalons();
		MyDrive = new RobotDrive(speedController.rightMaster,speedController.leftMaster);
		MyDrive.setExpiration(1.0);
		turnController = new PIDController(PidTurnValues.kP, PidTurnValues.kI, PidTurnValues.kD, PidTurnValues.kF, hrs,this);
		turnController.setInputRange(-180.0f,  180.0f);
		turnController.setOutputRange(-1.0, 1.0);
		turnController.setAbsoluteTolerance(PidTurnValues.kToleranceDegrees);
		turnController.setContinuous(true);
		Timer.delay(0.05);
		
		System.out.println("&&&&&&&&&&&&&&&&&&&&&& ROBOT INIT RUNNING &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		
	}
	@Override
	public void autonomousInit() {
		// auto mode call
		//auto = AutoModes.AUTO_MODE_3B;
		hrs.reset();
		speedController.resetTalons();
		speedController.setDriveTalons();
		rocketScriptCurrentCount = 0;
		
		rocketScriptData = getNewScript.auto_04();
		rocketScriptLength = rocketScriptData.length;
		
		speedController.leftMaster.setEncPosition(0);
		System.out.println("**************************Auto Init completed");
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		   if (rocketScriptCurrentCount <  rocketScriptLength)
		   {
			   //System.out.println("********running rocketScript counter");
		      String [] values = rocketScriptData[rocketScriptCurrentCount].split(";");
		      System.out.println("Value 1: " + values[0] + " Value 2: " + values[1]);
		      if (Integer.parseInt(values[0]) == RobotModes.SMART_DRIVE_STRAIGHT) {
		    	  double fudgeFactor = Double.parseDouble(values[1]);
		    	 if (driveStraight(0.65, distanceFromCamera-12,6.5,angleFromCamera + fudgeFactor)){
		    		 rocketScriptCurrentCount++;
		    	 }
		      }
		     
		      if (Integer.parseInt(values[0]) == RobotModes.SMART_TURN_ANGLE) {
		    	  System.out.println("Smart Turn Angle turning" + angleFromCamera);
		    	 if (TurnAngle(angleFromCamera)){
		    		 speedController.leftMaster.setEncPosition(0);
		    		 rocketScriptCurrentCount++;
		    	 }
		    	  
		      }
		      
		      if (Integer.parseInt(values[0]) == RobotModes.GET_CAMERA_ANGLE) {
	  			String getSocketData;
				hrs.reset();
				Timer.delay(0.5);
				getSocketData = util.getCameraAngle();
				String [] socketValues = getSocketData.split("\\*");
				distanceFromCamera = Double.parseDouble(socketValues[0]);
				angleFromCamera =  Float.parseFloat(socketValues[1]);
				System.out.println("Distance from camera: " + distanceFromCamera);
				System.out.println("Angle from camera: " + angleFromCamera);
				turnController.reset();
				turnController.setSetpoint(angleFromCamera);
				turnController.enable();
	    		rocketScriptCurrentCount++;
		      }
		      
		      if (Integer.parseInt(values[0]) == RobotModes.RAW_TURN_ANGLE) {
		    	  if (turnRoughAngle(Double.parseDouble(values[1]))) {
		    		  rocketScriptCurrentCount++;
		    	  }
		      }
		      if (Integer.parseInt(values[0]) == RobotModes.RAW_DRIVE_STRAIGHT) {
		    	 if (dumbDriveStraight(1.0, Double.parseDouble(values[1]), 6.5)) {
		    		 speedController.leftMaster.setEncPosition(0);
		    		 hrs.reset();
		    		 rocketScriptCurrentCount++;
		    	 }
		    	  
		      }
		   }
		   
		}
	
	@Override
	public void teleopPeriodic() {
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
	
	private boolean TurnAngle(float cameraAngle )
	{
		double angle = cameraAngle;
		boolean doneTurning = false;
		double	currentRotationRate = rotateToAngleRate;
		 if (Math.abs(angle - hrs.getAngle()) < .6 && Math.abs(currentRotationRate) < .5){
				currentRotationRate = 0;
				MyDrive.arcadeDrive(0.0, 0);
				doneTurning = true;	
			}
		 else
		 {
			 try {
				 MyDrive.arcadeDrive(0.0, currentRotationRate);
			 	} catch ( RuntimeException ex ) {
			 		DriverStation.reportError("Error communicating with drive system: " + ex.getMessage(), true);
			 	}
		 }
		
			 return doneTurning;
	}
	public boolean turnRoughAngle(double turnAngle){
		if(turnAngle < 0){
			if(Math.abs(hrs.getYaw()-turnAngle) < 5){
				MyDrive.arcadeDrive(0,0);
				return true;
			}else{
				MyDrive.arcadeDrive(0,-.5);
			}
		}else{
			if(Math.abs(hrs.getYaw()-turnAngle)<5){
				MyDrive.arcadeDrive(0, 0);
				return true;
			}else{
				MyDrive.arcadeDrive(0,.5);
			}
		}
		return false;
	}
	
	public boolean dumbDriveStraight(double speed, double inches, double multiplier){
		if(Math.abs(speedController.leftMaster.getEncPosition()/1024 *Math.PI*4) > Math.abs(inches * multiplier)){
			MyDrive.arcadeDrive(0, 0);
			return true;
		}else{
			if(inches > 0) {
				speedController.rightMaster.set(speed);
				speedController.leftMaster.set(-speed*0.95);
			}
			else {
				speedController.rightMaster.set(-speed);
				speedController.leftMaster.set(speed*0.95);
			}
		}
		return false;
	}
	
	public boolean driveStraight(double speed, double inches,double multiplier, double angle){
		boolean doneDriving = false;
		double	currentRotationRate = rotateToAngleRate;
		
		if(speedController.leftMaster.getEncPosition()/1024*Math.PI*4 > (inches*multiplier)){
			 MyDrive.arcadeDrive(0, 0);
  			doneDriving = true;
		}
		else{
			  turnController.setSetpoint(angle);
			  turnController.enable();
			  MyDrive.arcadeDrive(speed, currentRotationRate);
		}
		return doneDriving;
	}
	
	@Override
	public void pidWrite(double output) {
		 rotateToAngleRate = output * 0.8;
	}	
}

