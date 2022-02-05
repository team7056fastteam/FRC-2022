// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Robot;

import static frc.robot.Constants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Intake {

    private final Robot robot;
    double currentTime;

    private final CANSparkMax rollerMotor;
    private final CANSparkMax topIntakeMotor;
    private final CANSparkMax bottomIntakeMotor;
    private final CANSparkMax conveyorMotor;

    private final DigitalInput intakeSwitch;
    private final DigitalInput conveyorSwitch;

    public Intake() {
        robot = new Robot();
        currentTime = robot.getCurrentTime();

        /** Motor speeds will be reported in dashboard later */
        rollerMotor = new CANSparkMax(INTAKE_ROLLER_MOTOR, MotorType.kBrushless);
        topIntakeMotor = new CANSparkMax(INTAKE_TOP_MOTOR, MotorType.kBrushless);
        bottomIntakeMotor = new CANSparkMax(INTAKE_BOTTOM_MOTOR, MotorType.kBrushless);
        conveyorMotor = new CANSparkMax(INTAKE_CONVEYOR_MOTOR, MotorType.kBrushless);

        intakeSwitch = new DigitalInput(INTAKE_LIMIT_SWITCH);
        conveyorSwitch = new DigitalInput(CONVEYOR_LIMIT_SWITCH);
    }

    private final XboxController operatorController = new XboxController(OPERATOR_JOYSTICK_ID);
     
    /** Motor speed variables */
    private double rollerSpeed = 0.5;
    private double intakeSpeed = 0.5;
    private double conveyorSpeed = 0.25;

    private boolean invertConveyor;

    private boolean hasCounted = false;
    private int counter = 0;

    /** This function is called periodically during operator control. */
    public void teleopPeriodic() {

        // Check input from left trigger
        if (operatorController.getLeftTriggerAxis() > 0.2) {
            runRoller();
            runIntake();
            runConv();
        }
        else {
            hasCounted = false;
        }

        // Check input for A button
        if (operatorController.getBButton()) { 
            invertConveyor = true;
        }
        else {
            invertConveyor = false;
        }

        // Check how many balls are counted
        if (counter == 1) {
            // LED on 1
        }
        else if (counter == 2) {
            // LED on 2
        }
        else if (counter == 3) {

            // Stop int from wrapping over 2
            counter = 0;
        }
        else {
            // LED off
        }
    }

    /** Command functions */

    public void runRoller() {
        rollerMotor.set(rollerSpeed);
    }

    public void runIntake() {
        topIntakeMotor.set(intakeSpeed);
        bottomIntakeMotor.set(robot.invert(intakeSpeed));
    }

    public void runConv() {
        boolean cargoInIntake = intakeSwitch.get();
        boolean cargoInConveyor = conveyorSwitch.get();

        /** 
         * [Intake switch, Conveyor switch]
         * 0 - No cargo
         * 1 - Cargo
        */

        if (invertConveyor) {
            runConvInverted();
        }

        else {

            // Case [0, 0]
            if (!cargoInIntake && !cargoInConveyor) {
                conveyorMotor.set(conveyorSpeed);
            }
            // Case [1, 0]
            else if (!cargoInIntake && cargoInConveyor) {
                conveyorMotor.set(conveyorSpeed);
            }
            // Case [0, 1]
            else if (cargoInIntake && !cargoInConveyor) {
                conveyorMotor.set(0);

                if (!hasCounted) {
                    hasCounted = true;
                    counter++;
                }
            }
            // Case [1, 1]
            else {
                conveyorMotor.set(conveyorSpeed);

                if (!hasCounted) {
                    hasCounted = true;
                    counter++;
                }
            }
        }
    }

    public void runConvInverted() {
        conveyorMotor.set(robot.invert(conveyorSpeed));
    }
}