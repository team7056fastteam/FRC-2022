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
    private final CANSparkMax rollerMotor;
    private final CANSparkMax conveyorMotor;
    private final CANSparkMax topIntakeMotor;
    private final CANSparkMax bottomIntakeMotor;

    public Intake()
    {
        /** Motor speeds will be reported in dashboard later */
        //leftIntakeMotor = new CANSparkMax(LEFT_INTAKE_MOTOR, MotorType.kBrushless);
        //rightIntakeMotor = new CANSparkMax(RIGHT_INTAKE_MOTOR, MotorType.kBrushless);
        rollerMotor = new CANSparkMax(INTAKE_ROLLER_MOTOR, MotorType.kBrushless);
        conveyorMotor = new CANSparkMax(INTAKE_CONVEYOR_MOTOR, MotorType.kBrushless);
        topIntakeMotor = new CANSparkMax(INTAKE_TOP_MOTOR, MotorType.kBrushless);
        bottomIntakeMotor = new CANSparkMax(INTAKE_BOTTOM_MOTOR, MotorType.kBrushless);
    }
    
    private final Joystick operatorJoystick = new Joystick(OPERATOR_JOYSTICK_ID);

    /** This function is called periodically during operator control. */
    public void teleopPeriodic() {

        // Roller & Intake
        if (operatorJoystick.getRawAxis(2) > 0.4) {
            runRoller();
            runIntake();
        }
    }

    public void runRoller() {

    }

    public void runIntake() {

    }

    public void runConv() {

    }
}
