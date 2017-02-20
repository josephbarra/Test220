package org.usfirst.frc.team20.robot;
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

public class SpeedControllers {
	
    public CANTalon rightMaster = new CANTalon(2);
    public CANTalon rightSlave = new CANTalon(3);
    public CANTalon leftMaster = new CANTalon(0);
    public CANTalon leftSlave = new CANTalon(1); 
    
    
    public double getCurrent()
    {
    
    	double total = 0.0;
    	total = rightMaster.getOutputCurrent() + rightSlave.getOutputCurrent();
    	total = total + leftMaster.getOutputCurrent() + leftSlave.getOutputCurrent();
    	return total;
    	
    }
	
   public void setDriveTalons()
   {
	 
	   // drive train start
		rightSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		rightSlave.set(rightMaster.getDeviceID());
		leftSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		leftSlave.set(leftMaster.getDeviceID());
		leftMaster.setFeedbackDevice(FeedbackDevice.QuadEncoder);
	   // drive train end
   }
   public void resetTalons(){
//	   rightSlave.reset();
//	   leftSlave.reset();
	   rightMaster.reset();
	   leftMaster.reset();
   }

}
