package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Robot;

public class Auton {

    Robot robot;

    double currentTime;
    char autonMode;
    boolean hasStarted;

    double navX = robot.getXPos();
    double navY = robot.getYPos();
    double[] position = {navX, navY};

    double[] driveTo = {0.0, 0.0};

    private final Joystick driver = new Joystick(0);

    public Auton() {
        robot = new Robot();
        currentTime = robot.getCurrentTime();
    }

    public void robotInit() {
        autonMode = 'a';
        hasStarted = false;
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

    }

    public void autonB() {

    }

    public void autonC() {

    }

    public void autonD() {

    }

    public void autonE() {

    }

    public void autonF() {

    }
}
