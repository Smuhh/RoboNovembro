package frc.robot.commands.LimelightCMD;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Limelight.LimelightSub;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;

public class AlignSwerve extends Command {

    private final LimelightSub limelightSub;
    private final SwerveSubsystem swerve;
    private final PIDController pidController = new PIDController(0.09, 0, 0);
    private final int Id = 23;


    public AlignSwerve(LimelightSub limelightSub, SwerveSubsystem swerve){
            this.limelightSub = limelightSub;
            this.swerve = swerve;
            addRequirements(limelightSub, swerve);
        
    }
    @Override
    public void initialize() {
        pidController.setSetpoint(0);
        pidController.setTolerance(1);
         pidController.setSetpoint(frc.robot.Constants.LimelightConstants.txvalue);

    }
    @Override
    public void execute() {

        if(limelightSub.getTV() && limelightSub.hasTag(Id)){
            double Tx = limelightSub.gettX();
            double rotationSpeed = MathUtil.clamp(pidController.calculate(Tx), -1.0, 1.0);
            swerve.drive(new Translation2d(), rotationSpeed, true);
    } else {
        swerve.drive(new Translation2d(), 0, true);
    }

}

    @Override
    public boolean isFinished() {
        return pidController.atSetpoint();
    }
    @Override
    public void end(boolean interrupted) {
    swerve.drive(new Translation2d(0, 0), 0, true);

    }
}
