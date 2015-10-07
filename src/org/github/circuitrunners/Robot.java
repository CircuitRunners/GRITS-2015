package org.github.circuitrunners;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

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
    private Joystick mysteryController;

    private double c;

    public void robotInit() {
        for (int i = 0; i < 4; i++) driveMotors[i] = new Talon(i);

        elevator = new CANTalon(0);
        
        intakeTensionMotor = new Talon(driveMotors.length+1);
        for (int i = 0; i < 2; i++) intakeMotors[i] = new Talon(driveMotors.length+i+1);

        drive = new RobotDrive(driveMotors[0], driveMotors[1], driveMotors[2], driveMotors[3]);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        
        mysteryController = new Joystick(0);
    }

    public void teleopInit() {
        drive.setSafetyEnabled(true);
    }

    public void teleopPeriodic() {
        c = /*get throttle*/;

        double xAxis = mysteryController.getX();
        double yAxis = mysteryController.getY();
        double rotAxis = mysteryController.getZ();

        // Mecanum drive
        drive.mecanumDrive_Polar(xAxis, yAxis, rotAxis);

        // Elevator control
        elevator.set(/*down button*/ ? -1.0 : /*up button*/ ? 1.0 : 0.0);
        
        //Intake control
        double intakeSpeed = /*in button*/ ? -1.0 : /*out button*/ ? 1.0 : 0.0;
        intakeMotors[0].set(intakeSpeed);
        intakeMotors[1].set(-intakeSpeed);
    }
}
