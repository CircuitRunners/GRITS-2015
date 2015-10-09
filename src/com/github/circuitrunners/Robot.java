package com.github.circuitrunners;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
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
        for (int i = 0; i < 2; i++) intakeMotors[i] = new Talon(driveMotors.length+i+1);

        drive = new RobotDrive(driveMotors[0], driveMotors[3], driveMotors[1], driveMotors[2]);
        drive.setInvertedMotor(MotorType.kFrontRight, true);
        drive.setInvertedMotor(MotorType.kRearRight, true);
        
        driveStick = new Joystick(0);
        controlStick = new Joystick(1);
    }

    public void teleopInit() {
        drive.setSafetyEnabled(true);
    }

    public void teleopPeriodic() {
    	
        double throttle = (driveStick.getThrottle()+1)/-2;
        double xAxis = throttle * (Math.abs(driveStick.getX()) > 0.15 ? driveStick.getX() : 0.0);
        double yAxis = throttle * (Math.abs(driveStick.getY()) > 0.15 ? driveStick.getY() : 0.0);
        double rotation = throttle * (Math.abs(driveStick.getTwist()) > 0.15 ? driveStick.getTwist() : 0.0);

        // Mecanum drive
        drive.mecanumDrive_Cartesian(xAxis, yAxis, rotation, 0);

        // Elevator control
        elevator.set(driveStick.getRawButton(5) ? -1.0 : driveStick.getRawButton(3) ? 1.0 : 0.0);
        
        //Intake control
		intakeTensionMotor.set(driveStick.getRawButton(2) ? -1.0 : driveStick.getRawButton(7) ? 1.0 : 0.0);
        if (Math.abs(controlStick.getTwist()) > 0) {
        	double toteTurnSpeed = -controlStick.getTwist();
        	intakeMotors[0].set(toteTurnSpeed > 0 ? Math.pow(toteTurnSpeed, 2) : -Math.pow(toteTurnSpeed, 2));
        	intakeMotors[1].set(toteTurnSpeed > 0 ? Math.pow(toteTurnSpeed, 2) : -Math.pow(toteTurnSpeed, 2));
        } else {
        	double intakeSpeed = driveStick.getRawButton(1) ? -1.0 : driveStick.getRawButton(8) ? 0.4 : 0.0;
    		intakeMotors[0].set(-intakeSpeed);
            intakeMotors[1].set(intakeSpeed);
        }
    }
}
