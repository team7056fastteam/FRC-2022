package frc.robot.subsystems;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Robot;

public class Auton {

    Robot robot;
    Drivetrain _drive;

    double currentTime;
    char autonMode;

    boolean hasStarted;
    boolean hasReachedPosition = false;
    int phase = 0;
/*
    double currentX = robot.getXPos();
    double currentY = robot.getYPos();
    double currentZ = robot.getGyroscopeRotation();
*/
    double currentX = 0;
    double currentY = 0;
    double currentZ = 0;
    private final Joystick driver = new Joystick(0);

    public Auton() {
        robot = new Robot();
        currentTime = robot.getCurrentTime();
    }

    public void robotInit() {
        autonMode = 'a';
        hasStarted = false;
        phase = 1;
    }
    
    public void robotPeriodic() {

        // Set autonomous function with driver input
        if (!hasStarted)
        {
            if (driver.getRawButton(7)) { autonMode = 'a'; }
            if (driver.getRawButton(8)) { autonMode = 'b'; }
            if (driver.getRawButton(9)) { autonMode = 'c'; }
            if (driver.getRawButton(10)) { autonMode = 'd'; }
            if (driver.getRawButton(11)) { autonMode = 'e'; }
            if (driver.getRawButton(12)) { autonMode = 'f'; }
        }
    }

    public void autonomousInit() {
        // End autonomous selector when the match begins
        hasStarted = true;
        hasReachedPosition = false;
    }

    public void autonomousPeriodic() {
        if (autonMode == 'a') { autonA(); }
        if (autonMode == 'b') { autonB(); }
        if (autonMode == 'c') { autonC(); }
        if (autonMode == 'd') { autonD(); }
        if (autonMode == 'e') { autonE(); }
        if (autonMode == 'f') { autonF(); }
    }

    public void autonA() {
        if (phase == 0) {
            driveTo(10, 10, 0);
        }
        else if (phase == 1) {
            driveTo(0, 15, 90);
        }
        else if (phase == 2) {
            driveTo(20, 20, 270);
        }
    }

    public void autonB() { }

    public void autonC() { }

    public void autonD() { }

    public void autonE() { }

    public void autonF() { }

    /** Drive functions */
    public void driveTo(double desiredX, double desiredY, double desiredZ) {
        double xSpeed = calculateSpeed(desiredX - currentX);
        double ySpeed = calculateSpeed(desiredY - currentY);
        double rotation = calculateSpeed(desiredZ - currentZ);

        _drive.drive(ChassisSpeeds.fromFieldRelativeSpeeds(
            xSpeed * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
            ySpeed * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
            rotation * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND,
            _drive.getRotation()
        ));

        // End function usage if we are moving to the next autonomous phase
        if (xSpeed == 0 && ySpeed == 0 && rotation == 0) { phase++; robot.stop(); }
    }

    public double calculateSpeed(double distance) {
        // Do not change these values unless you know what you are doing!
        double max = 0.75;
        double rate = 0.7;
        double fixedDistance = Math.abs(distance);
        int direction = 1;

        // Make sure fixed distance doesn't go below zero
        if (fixedDistance < 0) { fixedDistance = 0; }

        // Calculate speed double
        double speed = Math.log((fixedDistance + 1) * rate);
        if (speed > max) { speed = max - 0.01; }
        if (speed < 0) { speed = 0.0; }
        if (fixedDistance < 0.01) { speed = 0.0; }

        if (distance < 0) { direction = -1; }

        return direction * speed;
    }

    public double calculateRotation(double angle) {
        // Do not change these values unless you know what you are doing!
        double max = 0.8;
        double rate = 0.0005;

        // Calculate speed double
        double speed = Math.log((angle + 1) * rate);
        if (speed > max) { speed = max - 0.01; }
        if (speed < 0) { speed = 0.0; }
        if (angle < 0.01) { speed = 0.0; }

        return speed;
    }
}
