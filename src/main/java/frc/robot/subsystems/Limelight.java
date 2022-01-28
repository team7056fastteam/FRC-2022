// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import org.photonvision.PhotonCamera;

import frc.robot.Robot;

public class Limelight {

    private final Robot robot;
    
    public Limelight() {
        robot = new Robot();
    }

    PhotonCamera camera = new PhotonCamera("photonvision");

    public void robotInit() {
        camera.setDriverMode(true);
    }

    public void autonomousInit() {
        camera.setDriverMode(false);
    }
}
