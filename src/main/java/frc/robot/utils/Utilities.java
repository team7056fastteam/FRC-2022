package frc.robot.utils;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class Utilities {

    public void configure(String name, CANSparkMax motor, int currentLimit, boolean inverted, ShuffleboardTab tab){
        motor.setSmartCurrentLimit(currentLimit);
        motor.setInverted(inverted);
        motor.burnFlash();

        tab.addNumber("[" + name + "] Motor Speed", () -> motor.get());
        tab.addNumber("[" + name + "] Applied Output", () -> motor.getAppliedOutput());
        tab.addNumber("[" + name + "] Output Current", () -> motor.getOutputCurrent());
    }
}
