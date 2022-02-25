// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
// import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Constants;
import frc.robot.Robot;

import static frc.robot.Constants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Intake {

    private final Robot robot;
    private final Constants constants = new Constants();

    private final CANSparkMax rollerMotor;
    private final CANSparkMax conveyorMotor;

    private final DigitalInput intakeSwitch;
    private final DigitalInput conveyorSwitch;

    public Intake() {
        robot = new Robot();

        rollerMotor = new CANSparkMax(INTAKE_ROLLER_MOTOR, MotorType.kBrushless);
        conveyorMotor = new CANSparkMax(INTAKE_CONVEYOR_MOTOR, MotorType.kBrushless);

        intakeSwitch = new DigitalInput(INTAKE_LIMIT_SWITCH);
        conveyorSwitch = new DigitalInput(CONVEYOR_LIMIT_SWITCH);
    }

    // private final Joystick operator = new Joystick(OPERATOR_JOYSTICK_ID);
     
    /** Configuration */
    private double rollerSpeed = 0.5;
    private double conveyorSpeed = 0.25;

    private boolean invertConveyor;
    private boolean hasCounted = false;
    private int counter = 0;

    /** This function is called periodically during operator control. */
    public void teleopPeriodic() {

        // Check input from left trigger
        if (constants.operatorLT() > 0.1) {
            runRoller();
            runConv();
        }
        else {
            hasCounted = false;
        }

        // Check input for A button
        if (constants.operatorA()) {
            forceRunConv();
        }

        // Check input for B button
        if (constants.operatorB()) { 
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

    public void forceRunConv() {
        conveyorMotor.set(conveyorSpeed);
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

    public void stop() {
        conveyorMotor.set(0);
        rollerMotor.set(0);
    }

    // I wonder if somebody will find this :(
    // 2/22/22
}