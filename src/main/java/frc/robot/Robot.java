// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;  
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.subsystems.*;

public class Robot extends TimedRobot {

    Drivetrain _drive;
    Intake _intake;

    private final Joystick driver = new Joystick(0);

    /**
    * This function is run when the robot is first started up and should be used for any
    * initialization code.
    */
    @Override
    public void robotInit() {
        // Initialize robot subsystems
        _drive = new Drivetrain();
        _intake = new Intake();
    }

    /**
    * This function is called every robot packet, no matter the mode. Use this for items like
    * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
    */
    @Override
    public void robotPeriodic() {
      // TODO
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
    }
}