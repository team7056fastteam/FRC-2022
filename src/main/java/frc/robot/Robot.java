// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
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
    Constants constants = new Constants();

    // private final Joystick driver = new Joystick(0);
    // private final Joystick operator = new Joystick(1);
    private final Timer timer = new Timer();
    
    // Static variables
    char auton;
    double gyroRotation;
    double t;

    /**
    * This function is run when the robot is first started up and should be used for any
    * initialization code.
    */
    @Override
    public void robotInit() {
        // Initialize robot subsystems
        _drive = new Drivetrain();
        _navpod = new NavPod();
        _lifter = new Lifter();
        _shooter = new Shooter();
        _limelight = new Limelight();

        // Reset instance variables
        auton = 'a';
        gyroRotation = 0.0;
        t = 0.0;

        // Check if the NavPod is connected to RoboRIO
        if (_navpod.isValid())
        {
            NavPodConfig config = new NavPodConfig();
            config.cableMountAngle = 0;
            config.fieldOrientedEnabled = false;
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

            // Update console with NavPod info every 10ms
            _navpod.setAutoUpdate(0.10, update -> System.err.printf("h: %f, x: %f, sx: %f, y: %f, ys: %f\n",
            update.h, update.x, update.sx, update.y, update.sy));
        }

        // Initialize subsystems that need to be updated before autonomous/operator control
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
        // Check if NavPod has been initialized, run updates
        if ((_navpod != null) && _navpod.isValid()) {
            NavPodUpdate update = _navpod.getUpdate();
            gyroRotation = update.h;
        }

        // Allow autonomous selection
        if (constants.operatorA()) {
            auton = 'a';
            System.out.println("Auton A selected...");
        }
        else if (constants.operatorB()) {
            auton = 'b';
            System.out.println("Auton B selected...");
        }
        else if (constants.operatorX()) {
            auton = 'c';
            System.out.println("Auton C selected...");
        }
        else if (constants.operatorY()) {
            auton = 'd';
            System.out.println("Auton D selected...");
        }
    }

    /** This function is run once each time the robot enters autonomous mode. */
    @Override
    public void autonomousInit() {
        /** Reset the timer so the new autonomous session will start from zero */
        timer.reset();
        timer.start();
        t = 0;

        System.out.println("Running Autonomous Function : " + String.valueOf(auton).toUpperCase());
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
        else if (auton == 'c') {
            autonC();
        }
        else if (auton == 'd') {
        }
        else {
            // Default to auton mode A if necessary
            auton = 'a';
        }
    }

    /**
    * Autonomous functions are labeled in order of importance
    * and usage on the field.
    * 
    * Collector Positions - Functions to collect cargo and deliver it at a fast rate
    * Defensive Positions - Functions to prioritize moving enemy cargo before collecting
    */

    /*  
        Collector Position 1
        1. Deposit cargo into hub
        2. Drive towards cargo in center area
        3. Collect cargo in center area
        4. Drive towards terminal
        5. Push cargo into terminal for human player
        6. Return to hub
        7. Deposit cargo into hub
    */
    public void autonA() {
        if (t > 0 && t < 2) {
            _shooter.runShooter();
            _shooter.runConv();
        }
        else {
            _shooter.stop();
            // Drive in reverse towards ball
            drive(0, -.4, 0);
        }
    }

    /*  
        Collector Position 2
        1. Deposit cargo into hub
        2. Drive towards cargo in lower area
        3. Collect cargo in lower area
        4. Return to hub position
        5. Deposit cargo into hub
        6. Drive to center line
    */
    public void autonB() {

    }

    /*  
        Defensive Position 1
        1. Deposit cargo into hub
        2. Drive towards enemy cargo in lower area
        3. Collect enemy cargo in lower area
        4. Drive towards hangar
        5. Deposit enemy cargo at hangar
        6. Drive to center line
    */
    public void autonC() {

    }

    /** This function is run once each time the robot enters operator control. */
    @Override
    public void teleopInit() {

    }

    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {
        double xT = 1.0;

        // Check for driver RT held/pressed
        if (constants.driverRT() > 0.5) {
            xT = 0.65;
        }

        // Check for driver RB pressed
        // Zero the gyroscope while driving
        if (constants.driverRB()) {
            setGyroscopeHeading(0);
        }

        double xPercent = -modifyAxis((constants.driverRY() * 0.75) * xT);
        double yPercent = -modifyAxis((constants.driverRX() * 0.75) * xT);
        double zPercent = -modifyAxis((constants.driverLX() * 0.65) * xT);
        
        // Field Oriented Drive
        /*
        _drive.drive(
                  ChassisSpeeds.fromFieldRelativeSpeeds(
                          xPercent * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                          yPercWWWent * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND, 
                          zPercent * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND,
                          _drive.getRotation()));
        */

        // Robot Oriented Drive
        _drive.drive(
            new ChassisSpeeds(
                xPercent * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                yPercent * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND, 
                zPercent * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND
        ));

        // Send commands to other classes
        _limelight.teleopPeriodic();
        _lifter.teleopPeriodic();

        /* The Intake subsystem is controlled in the Shooter.java
        class to avoid multiple motor controller initialization events. */
        _shooter.teleopPeriodic();
        // _intake.teleopPeriodic(); Depends on Shooter.java
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

    /** This function allows an easy drive function to exist for autonomous */
    public void drive(double translationX, double translationY, double rotation) {
        _drive.drive(
            new ChassisSpeeds(
                -modifyAxis(translationX) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                -modifyAxis(translationX) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND, 
                -modifyAxis(translationX) * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND
        ));
    }

    @Override
    public void disabledInit() {
        stop();
    }
}