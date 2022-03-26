// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import frc.robot.Constants;
import frc.robot.Robot;

import static frc.robot.Constants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Intake {

    private final Robot robot;

    Spark LED = new Spark(0);
    private final Constants constants = new Constants();

    private final CANSparkMax rollerMotor;
    private final CANSparkMax conveyorMotor;

    private final DigitalInput intakeSwitch;
    private final DigitalInput conveyorSwitch;

    public Intake(Robot robot) {
        this.robot = robot;

        rollerMotor = new CANSparkMax(INTAKE_ROLLER_MOTOR, MotorType.kBrushless);
        conveyorMotor = new CANSparkMax(INTAKE_CONVEYOR_MOTOR, MotorType.kBrushless);

        intakeSwitch = new DigitalInput(INTAKE_LIMIT_SWITCH);
        conveyorSwitch = new DigitalInput(CONVEYOR_LIMIT_SWITCH);

        conveyorMotor.burnFlash();
    }
     
    /** Configuration */
    private double rollerSpeed = 0.75;
    private double conveyorSpeed = 0.45;

    /** This function is called periodically during operator control. */
    public void teleopPeriodic() {

        // Check input from left trigger
        if (constants.operatorLT() > 0.2) {
            runRoller(0);
            runConv();
        }
        // Check input for A button
        else if (constants.operatorA()) {
            forceRunConv();
        }
        // Check input for B button
        else if (constants.operatorB()) { 
            runConvInverted();
        }
        else {
            stop();
        }
    }

    /** Command functions */

    public void runRoller(int direction) {
        rollerMotor.set(rollerSpeed);
    }

    public void forceRunConv() {
        conveyorMotor.set(conveyorSpeed);
    }

    public void forceRunRoller() {
        rollerMotor.set(0.8);
    }

    public void runConv() {
        boolean cargoInIntake = intakeSwitch.get();
        boolean cargoInConveyor = conveyorSwitch.get();

        // Cargo at intake, Cargo at conveyor
        if (!cargoInIntake && !cargoInConveyor) {
            conveyorMotor.set(conveyorSpeed);
        }
        // Only cargo at intake;
        else if (!cargoInIntake && cargoInConveyor) {
            conveyorMotor.set(conveyorSpeed);
        }
        // Only cargo at conveyor
        else if (cargoInIntake && !cargoInConveyor) {
            conveyorMotor.set(0);
        }
        // No Cargo
        else {
            conveyorMotor.set(conveyorSpeed);
        }
    }

    public void runConvInverted() {
        conveyorMotor.set(robot.invert(conveyorSpeed));
        rollerMotor.set(robot.invert(rollerSpeed));
    }

    public void stop() {
        conveyorMotor.set(0);
        rollerMotor.set(0);
    }
}