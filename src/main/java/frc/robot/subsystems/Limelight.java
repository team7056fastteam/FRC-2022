// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Robot;

public class Limelight {

    private final Robot robot;
    double currentTime;

    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

    private final Joystick driver = new Joystick(0);
    
    public Limelight() {
        robot = new Robot();
    }

    public void robotInit() {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
    }

    public void teleopPeriodic() {

        if (driver.getRawButton(1)) {   
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);
        }
        else {
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
        }

    }
}
