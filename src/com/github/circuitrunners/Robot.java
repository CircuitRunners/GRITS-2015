package com.github.circuitrunners;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Talon;

public class Robot extends IterativeRobot {

    // 0: Front Left, 1: Front Right, 2: Back Right, 3: Back Left
    private Talon[] driveMotors = new Talon[4];

    private CANTalon elevator = new CANTalon(0);

    private Talon intakeTensionMotor;
    // 0: left, 1: right
    private Talon[] intakeMotors = new Talon[2];

    // Robot Drive
    private RobotDrive drive;

    // Joystick
    private Joystick driveStick;

	private Joystick controlStick;

    public void robotInit() {
        for (int i = 0; i < 4; i++) driveMotors[i] = new Talon(i);

        elevator = new CANTalon(0);
        
        intakeTensionMotor = new Talon(driveMotors.length);
        for (int i = 0; i < 2; i++) intakeMotors[i] = new Talon(driveMotors.length+i+2);

        drive = new RobotDrive(driveMotors[0], driveMotors[1], driveMotors[2], driveMotors[3]);
        
        driveStick = new Joystick(0);
        controlStick = new Joystick(1);
    }

    public void teleopInit() {
        drive.setSafetyEnabled(true);
    }

    public void teleopPeriodic() {
    	
        double throttle = (driveStick.getThrottle()+1)/2;
        double xAxis = throttle * driveStick.getY();
        double yAxis = throttle * -driveStick.getTwist();
        double rotation = throttle * driveStick.getX();

        // Mecanum drive
        drive.mecanumDrive_Cartesian(xAxis, yAxis, rotation, 0);

        // Elevator control
        elevator.set(driveStick.getRawButton(6) ? -1.0 : driveStick.getRawButton(4) ? 1.0 : 0.0);
        
        //Intake control
		intakeTensionMotor.set(driveStick.getRawButton(2) ? 1.0 : driveStick.getRawButton(9) ? -1.0 : 0.0);
        if (Math.abs(controlStick.getTwist()) > 0) {
        	intakeMotors[0].set(controlStick.getTwist());
        	intakeMotors[1].set(controlStick.getTwist());
        } else {
        	double intakeSpeed = driveStick.getRawButton(1) ? -1.0 : driveStick.getRawButton(5) ? 1.0 : 0.0;
    		intakeMotors[0].set(intakeSpeed);
            intakeMotors[1].set(-intakeSpeed);
        }
    }
}
