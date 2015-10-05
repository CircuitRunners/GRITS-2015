package org.github.CircuitRunners;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class Robot extends IterativeRobot {

    // 0: Front Left, 1: Front Right, 2: Back Left, 3: Back Right
    private CANTalon[4] driveMotors;

    private Talon elevator;

    // 0: ?, 1: ?
    private Talon[2] intakeMotors;

    // Robot Drive
    private RobotDrive drive;

    // Joystick
    private Joystick mysteryController;

    private double c;

    public void robotInit() {
        for (int i = 0; i < 4; i++) driveMotors[i] = new CANTalon(i);

        elevator = new Talon(0);
        
        for (int i = 0; i < 2; i++) intakeMotors[i] = new Talon(i+1);

        drive = new RobotDrive(driveMotors[0], driveMotors[1], driveMotors[2], driveMotors[3]);

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
