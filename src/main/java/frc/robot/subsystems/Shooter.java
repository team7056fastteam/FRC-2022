package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;

import static frc.robot.Constants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Shooter {

    /**
     * Motors are currently initialized as CAN-Spark Max, but this might
     * change in the future. If using a Talon SRX, change the static
     * initialized controllers from CANSparkMax to TalonSRX
     */
    private final CANSparkMax leftShooterMotor;
    private final CANSparkMax rightShooterMotor;

    public Shooter()
    {
        /** Motor speeds will be reported in dashboard later */
        leftShooterMotor = new CANSparkMax(LEFT_SHOOTER_MOTOR, MotorType.kBrushless);
        rightShooterMotor = new CANSparkMax(RIGHT_SHOOTER_MOTOR, MotorType.kBrushless);
    }
    
    private final Joystick operatorJoystick = new Joystick(OPERATOR_JOYSTICK_ID);

    /** This function is called periodically during operator control. */
    public void teleopPeriodic() {
        // Fill later
    }
}
