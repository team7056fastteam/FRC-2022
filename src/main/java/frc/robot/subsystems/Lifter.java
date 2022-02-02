// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

//import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Robot;

//import static frc.robot.Constants.*;

public class Lifter {

    private final Robot robot;
    double currentTime;

    public Lifter()
    {
        robot = new Robot();
        currentTime = robot.getCurrentTime();

        /** Pneumatics will be reported in dashboard later */
    }
    
    // private final Joystick driverJoystick = new Joystick(DRIVER_JOYSTICK_ID);


    /** This function is called periodically during operator control. */
    public void teleopPeriodic() {
        // Fill later
    }
}
