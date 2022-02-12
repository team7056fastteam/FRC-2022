// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;  
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
    Auton _auton;
    NavPod _navpod;

    private final Joystick driver = new Joystick(0);
    private final Timer timer = new Timer();
    
    // Static variables
    double gyroRotation;
    double navX;
    double navY;
    double currentTime = 0;

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
        _limelight = new Limelight();
        _auton = new Auton();

        // NavPod initialization
        _navpod = new NavPod();

        // Check if the NavPod is connected to RoboRIO
        if (_navpod.isValid())
        {
            NavPodConfig config = new NavPodConfig();
            config.cableMountAngle = 270;
            config.fieldOrientedEnabled = true;
            config.initialHeadingAngle = 0;
            config.mountOffsetX = 0;
            config.mountOffsetY = 4.25;
            config.rotationScaleFactorX = 0.0675;
            config.rotationScaleFactorY = 0.02;
            config.translationScaleFactor = 0.008567;
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

            // double distance = _navpod.getDistance();
            // System.err.printf("distance: %f in\n", distance);

            setGyroscopeHeading(90);
            setDefaultPosition(0, 0);

            // Update console with NavPod info every 10ms
            _navpod.setAutoUpdate(0.5, update -> System.err.printf("h: %f, x: %f, sx: %f, y: %f, ys: %f\n",
            update.h, update.x, update.sx, update.y, update.sy));
        }

        _limelight.robotInit();
        _auton.robotInit();
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
        // Deadband
        value = deadband(value, 0.3);

        // Square the axis
        value = Math.copySign(value * value, value);

        return value;
    }

    /**
    * This function is called every robot packet, no matter the mode. Use this for items like
    * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
    */
    @Override
    public void robotPeriodic() {
        // Check if NavPod has been initialized
        if ((_navpod != null) && _navpod.isValid()) {
            NavPodUpdate update = _navpod.getUpdate();
            
            gyroRotation = update.h;
            navX = update.x;
            navY = update.y;
        }
        
        _auton.robotPeriodic();
    }

    /** This function is run once each time the robot enters autonomous mode. */
    @Override
    public void autonomousInit() {
        /** Reset the timer so the new autonomous session will start from zero */
        timer.reset();
        setGyroscopeHeading(90);

        /** Start autonomous clock */
        timer.start();
        currentTime = 0;

        _auton.autonomousInit();
    }

    // Recieve a double of the current time from the autonomous Timer
    public double getCurrentTime() { return currentTime; }

    @Override
    public void autonomousPeriodic() {
        _auton.autonomousPeriodic(); 
        currentTime = timer.get();
    }

    @Override
    public void teleopInit() {
        _drive.zeroGyroscope();
    }

    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {
        double xT = (driver.getRawAxis(3) * -0.5) + .5;

        double xPercent = xT * -modifyAxis(driver.getRawAxis(1));
        double yPercent = xT * -modifyAxis(driver.getRawAxis(0));
        double zPercent = xT * -modifyAxis(driver.getRawAxis(2) * 0.75);
        

        // Field Oriented Drive
        _drive.drive(
                  ChassisSpeeds.fromFieldRelativeSpeeds(
                          xPercent * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                          yPercent * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND, 
                          zPercent * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND,
                          _drive.getRotation()));
                          
        //_drive.drive(runFieldOriented(driver.getRawAxis(1), driver.getRawAxis(0), driver.getRawAxis(2)));
        

        

        // Robot Oriented Drive
        /*
        _drive.drive(
                  new ChassisSpeeds(
                          xPercent * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                          yPercent * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND, 
                          zPercent * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND));
                          */
        
        //_drive.drive(runRobotOriented(driver.getRawAxis(1), driver.getRawAxis(0), driver.getRawAxis(2) * 0.5));

        // Run limelight
        _limelight.teleopPeriodic();

        // Run intake
        _intake.teleopPeriodic();

        // Run lifter
        _lifter.teleopPeriodic();

        // Run shooter
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

    /** These functions
     *  recieves the current position of the NavPod */
    public double getXPos() { return navX; }
    public double getYPos() { return navY; }

    // Field Oriented drive
    public ChassisSpeeds runFieldOriented(double x, double y, double z) {
        double xT = (driver.getRawAxis(3) * -0.5) + .5;
        x = xT * -modifyAxis(x);
        y = xT * -modifyAxis(y);
        z = xT * -modifyAxis(z);

        return ChassisSpeeds.fromFieldRelativeSpeeds(
            x * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
            y * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
            z * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND,
            _drive.getRotation()
        );
    }

    // Robot Oriented drive
    public ChassisSpeeds runRobotOriented(double x, double y, double z) {
        double xT = (driver.getRawAxis(3) * -0.5) + .5;

        x = xT * -modifyAxis(x);
        y = xT * -modifyAxis(y);
        z = xT * -modifyAxis(z);

        return new ChassisSpeeds(
            x * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
            y * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
            z * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND
        );
    }

    /** This function hard stops the drivetrain */
    public void stop() {
        _drive.drive(new ChassisSpeeds(0, 0, 0));
    }

    @Override
    public void disabledInit() {
        stop();
        currentTime = 0;
    }
}