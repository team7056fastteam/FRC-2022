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

    public void red() {
        led.set(0.61);
    }

    public void green() {
        led.set(0.77);
    }

    public void blue() {
        led.set(0.87);
    }

    public void yellow() {
        led.set(0.69);
    }

    public void purple() {
        led.set(0.91);
    }
    
    public void allianceColor() {
        // Retrieve FMS data
        boolean isRed = NetworkTableInstance.getDefault().getTable("FMSInfo").getEntry("IsRedAlliance").getBoolean(true);

        if (isRed == true) {
            led.set(0.49);
            led.set(0.61);
        } 
        else { 
            led.set(0.49);
            led.set(0.87);
        }
    }
}