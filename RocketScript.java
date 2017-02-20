package org.usfirst.frc.team20.robot;

public class RocketScript {
	//SmartDriveStraight(distance,angle)
	//smartTurnAngle(angle)
	//TurnCameraangle(angle,ip,port)
	//rawTurnAngle(angle)
	//rawDriveStraight(distance)
	//Shooting()
	
	String SmartDriveStraight = "1";  
	String smartTurnAngle = "2";	
	String getCameraAngle = "3";  //(angle,ip,port) //gets distance too
	String rawTurnAngle = "4";  //angle
	String rawDriveStraight = "5"; // ( distance, fudgie)
    
	public String[] auto_01()  {
		String [] autoCode = new String[4];
		autoCode[0] = rawDriveStraight + ";" + "40";
		autoCode[1] = rawTurnAngle + ";"  +  "-30.00";
		autoCode[2] = rawDriveStraight + ";" + "20";
		autoCode[3] = rawTurnAngle + ";"  +  "30.00";
		
	   return autoCode;	
		
	}
	
	public String[] auto_02()  {
		String [] autoCode = new String[2];
		autoCode[0] = getCameraAngle + ";" + "NULL";
		autoCode[1] = SmartDriveStraight + ";"  +  "0";
  	   return autoCode;	
		
	}
	
	public String[] auto_03() {
		String [] autoCode = new String[5];
		autoCode[0] = rawDriveStraight + ";" + "110";
		autoCode[1] = rawTurnAngle + ";" + "-43.0";
		autoCode[2] = getCameraAngle + ";" + "NULL";
		autoCode[3] = smartTurnAngle + ";" + "NULL";
		autoCode[4] = SmartDriveStraight + ";" + "-2.0";
		return autoCode;
	}
	
	public String[] auto_04() {
		String [] autoCode = new String[7];
		autoCode[0] = rawDriveStraight + ";" + "-36";
		autoCode[1] = rawTurnAngle + ";" + "-50";
		autoCode[2] = rawDriveStraight + ";" + "-72";
		autoCode[3] = rawTurnAngle + ";" + "-100";
		autoCode[4] = getCameraAngle + ";" + "NULL";
		autoCode[5] = smartTurnAngle + ";" + "NULL";
		autoCode[6] = SmartDriveStraight + ";" + "-6";
		return autoCode;
	}
	
	
	
}