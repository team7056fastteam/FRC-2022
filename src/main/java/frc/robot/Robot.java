// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.subsystems.*;
import frc.robot.utils.Utilities;

public class Robot extends TimedRobot {

    // Subsystems
    Drivetrain _drive;
    Intake _intake;
    Lifter _lifter;
    Shooter _shooter;
    NavPod _navpod;

    // Instance variables
    char auton = 'a';
    double gyroRotation = 0.0;
    double steer = 0.0;
    double t = 0.0;
    float STEER_K = 0.009f;

    // Drive variables
    double driveX;
    double driveY;
    double driveZ;

    // Limelight
    NetworkTable table;
    NetworkTableEntry tx;
    NetworkTableEntry tv;

    private final Timer timer = new Timer();
    Joystick driver = new Joystick(0);
    XboxController operator = new XboxController(1);

    /**
     * This function is run when the robot is first started up and should be used
     * for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        _navpod = new NavPod(this);

        // Check if the NavPod is connected to RoboRIO
        if (_navpod.isValid()) {
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

        // Initialize Limelight
        table = NetworkTableInstance.getDefault().getTable("limelight");
        tx = table.getEntry("tx");
        tv = table.getEntry("tv");

        setLimelight(false);
    }

    private double modifyAxis(double value) {
        return Utilities.deadband(Math.copySign(value * value, value), 0.05);
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for
     * items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and
     * test.
     */
    @Override
    public void robotPeriodic() {
        // Allow autonomous selection
        if (operator.getAButton()) {
            auton = 'a';
            System.out.println("Low Goal Auton");
        } else if (operator.getBButton()) {
            auton = 'b';
            System.out.println("High Goal Auton - Closer");
        } else if (operator.getXButton()) {
            auton = 'c';
            System.out.println("High Goal Auton - Farther");
        }
    }

    /** This function is run once each time the robot enters autonomous mode. */
    @Override
    public void autonomousInit() {
        /** Reset the timer so the new autonomous session will start from zero */
        timer.reset();
        timer.start();
        t = 0;

        setLimelightCamera(true);
        setLimelight(false);

        stop();
    }

    /** This function is called periodically during autonomous mode. */
    @Override
    public void autonomousPeriodic() {
        t = timer.get();

        if (auton == 'a') {
            autonA();
        } else if (auton == 'b') {
            autonB();
        } else if (auton == 'c') {
            autonC();
        } else {
            // Default to auton mode A at startup
            auton = 'b';
        }
    }

    public void autonA() {
        if (t > 0 && t < 1) {
            stop();
        } else if (t > 1 && t < 3) {
            _drive.drive(new ChassisSpeeds(
                    modifyAxis(0.4) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                    -modifyAxis(0) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                    -modifyAxis(0) * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND));
            _intake.runConv();
            _intake.forceRunRoller();
        } else if (t > 3 && t < 6) {
            _drive.drive(new ChassisSpeeds(
                    -modifyAxis(0.4) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                    -modifyAxis(0) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                    -modifyAxis(0) * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND));
            _intake.runConv();
            _intake.forceRunRoller();
        } else if (t > 6 && t < 6.5) {
            stop();
            _intake.stop();
        } else if (t > 6.5 && t < 7.5) {
            trackTarget();
            _drive.drive(new ChassisSpeeds(
                    -modifyAxis(0.0) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                    -modifyAxis(0) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                    -modifyAxis(steer) * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND));
        } else if (t > 7.5 && t < 9) {
            _shooter.runShooter();
            _intake.forceRunConv();
            stop();
        } else {
            stop();
            _intake.stop();
            _shooter.stop();
        }
    }

    public void autonB() {
        if (t > 0 && t < 1) {
            stop();
        } else if (t > 1 && t < 3) {
            _drive.drive(new ChassisSpeeds(
                    modifyAxis(0.4) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                    -modifyAxis(0) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                    -modifyAxis(0) * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND));
            _intake.runConv();
            _intake.forceRunRoller();
        } else if (t > 3 && t < 4.5) {
            _drive.drive(new ChassisSpeeds(
                    -modifyAxis(0.4) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                    -modifyAxis(0) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                    -modifyAxis(0) * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND));
            _intake.runConv();
            _intake.forceRunRoller();
        } else if (t > 4.5 && t < 5) {
            setLimelight(true);

            steer = tx.getDouble(0.0) * STEER_K;
            System.out.println(steer);

            stop();
            _intake.stop();
        } else if (t > 5.5 && t < 6.5) {
            setLimelight(true);

            steer = tx.getDouble(0.0) * STEER_K;
            System.out.println(steer);

            _drive.drive(new ChassisSpeeds(
                    -modifyAxis(0) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                    -modifyAxis(0) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                    -modifyAxis(steer * 4) * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND));
        } else if (t > 6.5 && t < 8.5) {
            _shooter.forceRunShooter();
            _intake.forceRunConv();
            stop();
        } else {
            stop();
            _intake.stop();
            _shooter.stop();
        }
    }

    public void autonC() {
        if (t > 0 && t < 1) {
            stop();
        } else if (t > 1 && t < 3) {
            _drive.drive(new ChassisSpeeds(
                    modifyAxis(0.4) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                    -modifyAxis(0) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                    -modifyAxis(0) * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND));
            _intake.runConv();
            _intake.forceRunRoller();
        } else if (t > 3 && t < 4.15) {
            _drive.drive(new ChassisSpeeds(
                    -modifyAxis(0.4) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                    -modifyAxis(0) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                    -modifyAxis(0) * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND));
            _intake.runConv();
            _intake.forceRunRoller();
        } else if (t > 4.15 && t < 5) {
            setLimelight(true);

            steer = tx.getDouble(0.0) * STEER_K;
            System.out.println(steer);

            stop();
            _intake.stop();
        } else if (t > 5.5 && t < 6.5) {
            setLimelight(true);

            steer = tx.getDouble(0.0) * STEER_K;
            System.out.println(steer);

            _drive.drive(new ChassisSpeeds(
                    -modifyAxis(0) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                    -modifyAxis(0) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                    -modifyAxis(steer * 4) * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND));
        } else if (t > 6.5 && t < 8.5) {
            _shooter.forceRunShooter();
            _intake.forceRunConv();
            stop();
        } else {
            stop();
            _intake.stop();
            _shooter.stop();
        }
    }

    @Override
    public void teleopInit() {
        setLimelightCamera(true);
        setLimelight(false);

        setDefaultPosition(0, 0);
        setGyroscopeHeading(0);
        _drive.zeroGyroscope();
    }

    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {
        // Check for driver RT held/pressed
        double xT = 1.0;
        if (driver.getRawButton(12)) {
            xT = 0.65;
        }

        // Check for driver LT pressed
        if (driver.getRawButton(13)) {
            trackTarget();

            driveX = 0.0;
            driveY = 0.0;
            driveZ = -1 * steer;
        } else {
            driveX = -modifyAxis((driver.getRawAxis(1)) * xT * -1);
            driveY = -modifyAxis((driver.getRawAxis(0)) * xT);
            driveZ = -modifyAxis((driver.getRawAxis(3)) * xT);

            // Reset limelight
            setLimelight(false);
        }

        // Check if LB is pressed
        if (driver.getRawButton(1)) {
            _drive.lock();
        }
        else {
            /*
            _drive.drive(
                    new ChassisSpeeds(
                            driveX * +Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                            driveY * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                            driveZ * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND));
            */
            _drive.drive(
                    ChassisSpeeds.fromFieldRelativeSpeeds(
                            driveX * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                            driveY * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
                            driveZ * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND,
                            _drive.getRotation()));
            
        }

        // Send commands to other classes
        _lifter.teleopPeriodic();
        _intake.teleopPeriodic();
        _shooter.teleopPeriodic();
    }

    public void trackTarget() {
        setLimelight(true);

        double x = tx.getDouble(0.0);
        double v = tv.getDouble(0.0);

        driveX = 0;
        driveY = 0;

        // Check for a valid target
        if (v < 1.0) {
            System.out.println("ATTEMPTING TO TARGET");
            steer = 0.0;
        }

        steer = x * STEER_K;
        System.out.println("TARGETING");
    }

    /** This function reverts motor speeds without error */
    public double invert(double value) {
        return (value * (-1));
    }

    /** This function returns the Rotation2d calculated gyro heading */
    public Rotation2d getGyroscopeRotation2d() {
        return Rotation2d.fromDegrees(gyroRotation);
    }

    /** This function returns the degrees gyro heading */
    public double getGyroscopeRotation() {
        return gyroRotation;
    }

    /** This function sets the gyro heading */
    public void setGyroscopeHeading(double h) {
        _navpod.resetH(h);
    }

    /** This function sets the relative position of the NavPod */
    public void setDefaultPosition(double x, double y) {
        _navpod.resetXY(x, y);
    }

    /** This function sets the LEDs for the Limelight */
    public void setLimelight(boolean mode) {
        if (mode == true) {
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);
        } else {
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
        }
    }

    public void setLimelightCamera(boolean mode) {
        if (mode == true) {
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(0);
        } else {
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);
        }
    }

    /** This function stops the drivetrain */
    public void stop() {
        _drive.drive(new ChassisSpeeds(0, 0, 0));
    }

    @Override
    public void disabledInit() {
        stop();

        // Disable Limelight
        setLimelight(false);
    }

    // Don't change the code, he said
}