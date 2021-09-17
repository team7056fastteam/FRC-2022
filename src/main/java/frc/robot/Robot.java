// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.subsystems.DrivetrainSubsystem;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;

public class Robot extends TimedRobot {

    DrivetrainSubsystem _drive;

    private final Joystick driver = new Joystick(0);

    /**
    * This function is run when the robot is first started up and should be used for any
    * initialization code.
    */
    @Override
    public void robotInit() {
        // Initialize robot subsystems
        _drive = new DrivetrainSubsystem();
    }

    /**
    * This function is called every robot packet, no matter the mode. Use this for items like
    * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
    */
    @Override
    public void robotPeriodic() {

    }

    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {

        // Run drivetrain
        _drive.drive(
                  ChassisSpeeds.fromFieldRelativeSpeeds(
                          driver.getY(GenericHID.Hand.kLeft),
                          driver.getX(GenericHID.Hand.kLeft),
                          driver.getY(GenericHID.Hand.kRight),
                          _drive.getGyroscopeRotation()));
    }
}