package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;

import static frc.robot.Constants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Intake {

    /**
     * Motors are currently initialized as CAN-Spark Max, but this might
     * change in the future. If using a Talon SRX, change the static
     * initialized controllers from CANSparkMax to TalonSRX
     */
    private final CANSparkMax leftIntakeMotor;
    private final CANSparkMax rightIntakeMotor;

    public Intake()
    {
        /** Motor speeds will be reported in dashboard later */
        leftIntakeMotor = new CANSparkMax(LEFT_INTAKE_MOTOR, MotorType.kBrushless);
        rightIntakeMotor = new CANSparkMax(RIGHT_INTAKE_MOTOR, MotorType.kBrushless);
    }
    
    private final Joystick operatorJoystick = new Joystick(OPERATOR_JOYSTICK_ID);

    /** This function is called periodically during operator control. */
    public void teleopPeriodic() {
        // Fill later
    }
}
