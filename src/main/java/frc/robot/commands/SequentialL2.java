package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.commands.Garra.GarraCMD;
import frc.robot.commands.swervedrive.Elevador.ElevatorCMD;
import frc.robot.subsystems.ElevatorSUB;
import frc.robot.subsystems.GarraSUB;
import frc.robot.subsystems.GarraSUB.Estado;    
public class SequentialL2 extends SequentialCommandGroup {
    public SequentialL2(ElevatorSUB elevator, GarraSUB garra) {
        addCommands(
            new GarraCMD(garra,  Constants.GarraConstants.ANGULO_PONTO_ERETO),
            new ElevatorCMD(elevator, Constants.ElevatorConstants.pointL3),
            new GarraCMD(garra, Constants.GarraConstants.ANGULO_PONTO_ERETO), //garra fica reta para colocar a gamepiece na garra
            new GarraCMD(garra, Estado.COLETANDO),
            new GarraCMD(garra, Constants.GarraConstants.ANGULO_PONTO_1),
            new InstantCommand(() -> elevator.setEstado(ElevatorSUB.ElevatorState.Alvo_L2)),
            new ElevatorCMD(elevator, Constants.ElevatorConstants.pointL2) //elevador sobe para L2
        );
    }
    
}
