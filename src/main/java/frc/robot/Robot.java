// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;  
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.subsystems.*;

public class Robot extends TimedRobot {

    Drivetrain _drive;
    Intake _intake;
    Lifter _lifter;
    Shooter _shooter;

    private final Joystick driver = new Joystick(0);
    
    private final Timer timer = new Timer();

    /**
    * This function is run when the robot is first started up and should be used for any
    * initialization code.
    */
    @Override
    public void robotInit() {
        // Initialize robot subsystems
        _drive = new Drivetrain();
        _intake = new Intake();
        _lifter = new Lifter();
        _shooter = new Shooter();
    }

     /** This function is run once each time the robot enters autonomous mode. */
    @Override
    public void autonomousInit() {
        /** Reset the timer so the new autonomous session will start from zero */
        timer.reset();
        /** Start autonomous clock */
        timer.start();
    }

    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {
        // TODO Create autonomous programs
    }

    /**
    * This function is called every robot packet, no matter the mode. Use this for items like
    * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
    */
    @Override
    public void robotPeriodic() {
        // TODO Run diagnostics
    }

    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {

        // Run drivetrain
        _drive.drive(
                  ChassisSpeeds.fromFieldRelativeSpeeds(
                          driver.getY(),
                          driver.getX(),
                          driver.getY(),
                          _drive.getGyroscopeRotation()));

        // Run intake
        _intake.teleopPeriodic();

        // Run lifter
        _lifter.teleopPeriodic();

        // Run shooter
        _shooter.teleopPeriodic();
    }

    // Simple function to invert motor speeds without error
    public double invert(double value) {
        return (value * (-1));
    }
}