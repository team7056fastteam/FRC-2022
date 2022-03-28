// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Robot;

public class Limelight {
    // private Robot _robot;

    NetworkTable table;
    NetworkTableEntry tx;
    NetworkTableEntry tv;
    double steer;

    XboxController driver = new XboxController(0);

    public Limelight(Robot robot) {
        // _robot = robot;

        table = NetworkTableInstance.getDefault().getTable("limelight");
        tx = table.getEntry("tx");
        tv = table.getEntry("tv");
    }

    public void robotInit() {
        setLED(false);
        steer = 0.0;
    }

    public void teleopPeriodic() {

        float STEER_K = 0.009f;

        // Check for operator LB button
        if (driver.getLeftTriggerAxis() > 0.1) {
            setLED(true);

            double x = tx.getDouble(0.0);
            double v = tv.getDouble(0.0);

            // Check for a valid target
            if (v < 1.0) {
                steer = 0.0;
                return;
            }

            @SuppressWarnings("unused")
            double steerVal = x * STEER_K;

            // robot.aimAtTarget(x * STEER_K);
        }

        // Reset limelight
        else {
            setLED(false);
        }
    }

    public void setLED(boolean mode) {
        if (mode == true) {
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);
        } else {
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
        }
    }

    public void disabledInit() {
        setLED(false);
    }
}
