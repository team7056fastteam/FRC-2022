// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.subsystems.*;

public class Robot extends TimedRobot {

    // Subsystems
    Drivetrain _drive;
    Intake _intake;
    Lifter _lifter;
    Shooter _shooter;
    Limelight _limelight;
    NavPod _navpod;
    
    char auton = 'a';
    double gyroRotation = 0;
    double t = 0;

    private final Timer timer = new Timer();
    XboxController driver = new XboxController(0);
    private final Constants constants = new Constants();

    /**
    * This function is run when the robot is first started up and should be used for any
    * initialization code.
    */
    @Override
    public void robotInit() {
        _navpod = new NavPod(this);

        // Check if the NavPod is connected to RoboRIO
        if (_navpod.isValid())
        {
            NavPodConfig config = new NavPodConfig();
            config.cableMountAngle = 0;
            config.fieldOrientedEnabled = true;
            config.initialHeadingAngle = 90;
            config.mountOffsetX = 0;
            config.mountOffsetY = 0;
            config.rotationScaleFactorX = 0.05; // 0.0675
            config.rotationScaleFactorY = 0.0; // 0.02
            config.translationScaleFactor = 0.00748; // 0.008567
            _navpod.setConfig(config);

            // Report values to the console
            config = _navpod.getConfig();
            System.err.printf("config.cableMountAngle: %f\n", config.cableMountAngle);
            System.err.printf("config.fieldOrientedEnabled: %b\n", config.fieldOrientedEnabled);
            System.err.printf("config.initialHeadingAngle: %f\n", config.initialHeadingAngle);
            System.err.printf("config.mountOffsetX: %f in\n", config.mountOffsetX);
            System.err.printf("config.mountOffsetY: %f in\n", config.mountOffsetY);
            System.err.printf("config.rotationScaleFactorX: %f\n", config.rotationScaleFactorX);
            System.err.printf("config.rotationScaleFactorY: %f\n", config.rotationScaleFactorY);
            System.err.printf("config.translationScaleFactor: %f\n", config.translationScaleFactor);

            setGyroscopeHeading(0);
            setDefaultPosition(0, 0);

            // Keep heading calibrated
            _navpod.setAutoUpdate(0.02, update -> gyroRotation = update.h);
        }
        
        // Initialize robot subsystems
        _drive = new Drivetrain(this);
        _intake = new Intake(this);
        _lifter = new Lifter(this);
        _shooter = new Shooter(this);
        _limelight = new Limelight(this);

        _limelight.robotInit();
    }

    private static double deadband(double value, double deadband) {
        if (Math.abs(value) > deadband) {
            if (value > 0.0) {
                return (value - deadband) / (1.0 - deadband);
            } else {
                return (value + deadband) / (1.0 - deadband);
            }
        } else {
            return 0.0;
        }
    }
    
    private static double modifyAxis(double value) {
        return deadband(Math.copySign(value * value, value), 0.1);
    }
    
    /**
    * This function is called every robot packet, no matter the mode. Use this for items like
    * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
    */
    @Override
    public void robotPeriodic() {

        // Allow autonomous selection
        if (constants.operatorA()) {
            auton = 'a';
            System.out.println("Red/Blue Lower Goal");
        }
        else if (constants.operatorB()) {
            auton = 'b';
            System.out.println("Red/Blue High Goal");
        }

        //_drive.setDegrees(gyroRotation);
    }

    /** This function is run once each time the robot enters autonomous mode. */
    @Override
    public void autonomousInit() {
        /** Reset the timer so the new autonomous session will start from zero */
        timer.reset();
        timer.start();
        t = 0;
        
        stop();
    }

    /** This function is called periodically during autonomous mode. */
    @Override
    public void autonomousPeriodic() {
        t = timer.get();

        if (auton == 'a') {
            autonA();
        }
        else if (auton == 'b') {
            autonB();
        }
        else {
            // Default to auton mode A at startup
            auton = 'a';
        }
    }

    public void autonA() {
        if (t > 0 && t < 2) {
            _drive.drive(new ChassisSpeeds(
                modifyAxis(0.55) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                -modifyAxis(0) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND, 
                -modifyAxis(0) * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND
            ));
            _intake.runConv();
            _intake.forceRunRoller();
        }
        else if (t > 2 && t < 5) {
            _drive.drive(new ChassisSpeeds(
                -modifyAxis(0.55) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                -modifyAxis(0) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND, 
                -modifyAxis(0) * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND
            ));
            _intake.runConv();
            _intake.forceRunRoller();
        }
        else if (t > 5 && t < 5.5) {
            stop();
            _intake.stop();
        }
        else if (t > 5.5 && t < 7) {
            _shooter.runShooter();
            _intake.forceRunConv();
            stop();
        }
        else {
            stop();
            _intake.stop();
            _shooter.stop();
        }
    }

    public void autonB() {
        if (t > 0 && t < 2) {
            _drive.drive(new ChassisSpeeds(
                modifyAxis(0.55) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                -modifyAxis(0) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND, 
                -modifyAxis(0) * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND
            ));
            _intake.runConv();
            _intake.forceRunRoller();
        }
        else if (t > 2 && t < 3.5) {
            _drive.drive(new ChassisSpeeds(
                -modifyAxis(0.55) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                -modifyAxis(0) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND, 
                -modifyAxis(0) * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND
            ));
            _intake.runConv();
            _intake.forceRunRoller();
        }
        else if (t > 3.5 && t < 4) {
            stop();
            _intake.stop();
        }
        else if (t > 4 && t < 6.5) {
            _shooter.forceRunShooter();
            _intake.forceRunConv();
            stop();
        }
        else {
            stop();
            _intake.stop();
            _shooter.stop();
        }
    }

    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {
        // Check for driver RT held/pressed
        double xT = 1.0;
        if (driver.getRightBumper()) { xT = 0.65; }

        // Check for drsiver RB pressed
        if (driver.getRightTriggerAxis() > 0.1) {

            // Zero the NavPod gyroscope
            setGyroscopeHeading(0);
        }

        double xPercent = -modifyAxis((driver.getRightY() * 0.65) * xT);
        double yPercent = -modifyAxis((driver.getRightX() * 0.65) * xT);
        double zPercent = -modifyAxis((driver.getLeftX() * 0.65) * xT);

        // Robot Oriented Drive
        _drive.drive(
            new ChassisSpeeds(
                xPercent * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                yPercent * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND, 
                zPercent * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND
            ));

        if (constants.driverLB()) {
            _drive.lock();
        }

        // Field Oriented Drive
        /*
        _drive.drive(
                  ChassisSpeeds.fromFieldRelativeSpeeds(
                          xPercent * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                          yPercWWWent * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND, 
                          zPercent * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND,
                          _drive.getRotation()));
        */

        // Send commands to other classes
        _limelight.teleopPeriodic();
        _lifter.teleopPeriodic();
        _intake.teleopPeriodic();
        _shooter.teleopPeriodic();
    }

    /** This function reverts motor speeds without error */
    public double invert(double value) { return (value * (-1)); }

    /** This function returns the Rotation2d calculated gyro heading */
    public Rotation2d getGyroscopeRotation2d() { return Rotation2d.fromDegrees(gyroRotation); }

    /** This function returns the degrees gyro heading */
    public double getGyroscopeRotation() { return gyroRotation; }

    /** This function sets the gyro heading */
    public void setGyroscopeHeading(double h) { _navpod.resetH(h); }

    /** This function sets the relative position of the NavPod */
    public void setDefaultPosition(double x, double y) { _navpod.resetXY(x, y); }

    /** This function stops the drivetrain */
    public void stop() {
        _drive.drive(new ChassisSpeeds(0, 0, 0));
    }

    @Override
    public void disabledInit() {
        stop();

        // Disable Limelight
        _limelight.disabledInit();
    }
}