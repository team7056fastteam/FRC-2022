package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import frc.robot.Constants;

public class LED {

    Constants constants = new Constants();

    Spark led = new Spark(0);
    double ledVal;

    public LED()
    {
        //led = new Spark(Constants.LED_CHANNEL);
    }

    public void robotPeriodic() {
        led.set(ledVal);
    }

    public void set(double LED) {
        ledVal = LED;
    }
    
    public void allianceColor() {
/*
        // Retrieve FMS data
        boolean isRed = NetworkTableInstance.getDefault().getTable("FMSInfo").getEntry("IsRedAlliance").getBoolean(true);

        if (isRed == true) {
            ledVal = 0.61;
        } 
        else { 
            ledVal = 0.87;
        }
*/
        red();
    }

    public void red() {
        ledVal = 0.61;
    }

    public void blue() {
        ledVal = 0.61;
    }
}