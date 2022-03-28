package frc.robot.utils;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class Utilities {

    public void configure(String name, CANSparkMax motor, int currentLimit, double rampRate, boolean inverted, ShuffleboardTab tab) {
        motor.setSmartCurrentLimit(currentLimit);
        motor.setInverted(inverted);
        motor.setOpenLoopRampRate(rampRate);
        motor.setClosedLoopRampRate(rampRate);
        motor.burnFlash();

        tab.addNumber("[" + name + "] Motor Speed", () -> motor.get());
        tab.addNumber("[" + name + "] Applied Output", () -> motor.getAppliedOutput());
        tab.addNumber("[" + name + "] Output Current", () -> motor.getOutputCurrent());
    }

    public static double deadband(double input) {
		return deadband(input, 0.025);
	}

    public static double deadband(double input, double buffer) {
		if (Math.abs(input) < buffer) return 0;
		return input;
	}
}
