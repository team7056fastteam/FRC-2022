// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

public final class Constants {
    public static final double DRIVETRAIN_TRACKWIDTH_METERS = 0.6096;
    public static final double DRIVETRAIN_WHEELBASE_METERS = 0.6096;

    /** Joysticks */
    public static final int DRIVER_JOYSTICK_ID = 0;
    public static final int OPERATOR_JOYSTICK_ID = 1;

    /** Gyroscope Constants */

    public static final int DRIVETRAIN_PIGEON_ID = 0; // FIXME Set Pigeon ID

    /** Drivetrain Constants */

    public static final int FRONT_LEFT_MODULE_DRIVE_MOTOR = 0; // FIXME Set front left module drive motor ID
    public static final int FRONT_LEFT_MODULE_STEER_MOTOR = 4; // FIXME Set front left module steer motor ID
    public static final int FRONT_LEFT_MODULE_STEER_ENCODER = 0; // FIXME Set front left steer encoder ID
    public static final double FRONT_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(0.0); // FIXME Measure and set front left steer offset

    public static final int FRONT_RIGHT_MODULE_DRIVE_MOTOR = 1; // FIXME Set front right drive motor ID
    public static final int FRONT_RIGHT_MODULE_STEER_MOTOR = 5; // FIXME Set front right steer motor ID
    public static final int FRONT_RIGHT_MODULE_STEER_ENCODER = 0; // FIXME Set front right steer encoder ID
    public static final double FRONT_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(0.0); // FIXME Measure and set front right steer offset

    public static final int BACK_LEFT_MODULE_DRIVE_MOTOR = 2; // FIXME Set back left drive motor ID
    public static final int BACK_LEFT_MODULE_STEER_MOTOR = 6; // FIXME Set back left steer motor ID
    public static final int BACK_LEFT_MODULE_STEER_ENCODER = 0; // FIXME Set back left steer encoder ID
    public static final double BACK_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(0.0); // FIXME Measure and set back left steer offset

    public static final int BACK_RIGHT_MODULE_DRIVE_MOTOR = 3; // FIXME Set back right drive motor ID
    public static final int BACK_RIGHT_MODULE_STEER_MOTOR = 7; // FIXME Set back right steer motor ID
    public static final int BACK_RIGHT_MODULE_STEER_ENCODER = 0; // FIXME Set back right steer encoder ID
    public static final double BACK_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(0.0); // FIXME Measure and set back right steer offset

    /** Intake Constants */

    public static final int INTAKE_ROLLER_MOTOR = 8;
    public static final int INTAKE_CONVEYOR_MOTOR = 11;
    public static final int INTAKE_TOP_MOTOR = 9;
    public static final int INTAKE_BOTTOM_MOTOR = 10;

    /** Shooter Constants */
    
    public static final int SHOOTER_LEFT_MOTOR = 12;
    public static final int SHOOTER_RIGHT_MOTOR = 13;

    /** Lift Constants */

    public static final int LIFT_LEFT_MOTOR = 14;
    public static final int LIFT_RIGHT_MOTOR = 15;
    public static final int ANGLE_LEFT_MOTOR = 16;
    public static final int ANGLE_RIGHT_MOTOR = 17;
}