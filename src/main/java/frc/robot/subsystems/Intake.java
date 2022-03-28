// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import frc.robot.Robot;

import static frc.robot.utils.Constants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Intake {
    private Robot robot;

    Spark LED = new Spark(0);
    XboxController operator = new XboxController(1);

    private final CANSparkMax rollerMotor;
    private final CANSparkMax conveyorMotor;
    private final DigitalInput intakeSwitch;
    private final DigitalInput conveyorSwitch;

    public Intake(Robot _robot) {
        robot = _robot;

        rollerMotor = new CANSparkMax(INTAKE_ROLLER_MOTOR, MotorType.kBrushless);
        conveyorMotor = new CANSparkMax(INTAKE_CONVEYOR_MOTOR, MotorType.kBrushless);
        rollerMotor.burnFlash();
        conveyorMotor.burnFlash();

        intakeSwitch = new DigitalInput(INTAKE_LIMIT_SWITCH);
        conveyorSwitch = new DigitalInput(CONVEYOR_LIMIT_SWITCH);
    }

    /** Configuration */
    private double rollerSpeed = 0.75;
    private double conveyorSpeed = 0.45;
    private double conveyorHigh = 0.80;

    /** This function is called periodically during operator control. */
    public void teleopPeriodic() {

        // Conveyor control
        if (operator.getLeftTriggerAxis() > 0.2) {
            runRoller(0);
            runConv();
        }
        // Override conveyor
        else if (operator.getAButton()) {
            forceRunConv();
        }
        // Inverse override conveyor
        else if (operator.getBButton()) {
            runConvInverted();
        } else {
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
        rollerMotor.set(conveyorHigh);
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