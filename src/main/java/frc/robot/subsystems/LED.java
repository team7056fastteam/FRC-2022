package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import frc.robot.Constants;

public class LED {

    Constants constants = new Constants();

    private final Spark led;

    public LED()
    {
        led = new Spark(Constants.LED_CHANNEL);
    }

    public void set(double val) {
        if ((val >= -1.0) && (val <= 1.0)) {
            led.set(val);
        }
    }
    
    public void allianceColor() {
        // Retrieve FMS data
        boolean isRed = NetworkTableInstance.getDefault().getTable("FMSInfo").getEntry("IsRedAlliance").getBoolean(true);

        if (isRed == true) {
            led.set(-0.01);
        } 
        else { 
            led.set(0.19);
        }
    }
}