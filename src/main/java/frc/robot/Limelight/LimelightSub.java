package frc.robot.Limelight;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LimelightSub extends SubsystemBase {

    private static String Limelight = "limelight-back";
    
    public LimelightSub() {
    }

    public final boolean hastarget() {
    return LimelightHelpers.getTV(Limelight);

  }

    public double gettX() {
        return LimelightHelpers.getTX(Limelight);
    }

    public double getTA() {
        return LimelightHelpers.getTA(Limelight);
    }

    public double getTY() {
        return LimelightHelpers.getTY(Limelight);
    }

    public boolean getTV() {
        return LimelightHelpers.getTV(Limelight);
    }
    
    public double getTargetID() {
    return LimelightHelpers.getFiducialID(Limelight);
  }

     public boolean hasTag(int id) {
    return hastarget()
        && getTargetID() == id;
  }


    @Override
    public void periodic() {
        Logger.recordOutput("Limelight/get X", gettX());
    };
    
}
