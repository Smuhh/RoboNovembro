package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElevatorSUB;

public class ElevatorCMD extends Command {
    
private final ElevatorSUB elevatorsub;
private final double position;

public ElevatorCMD(ElevatorSUB elevator, double position) {
this.elevatorsub = elevator;
this.position = position;
addRequirements(elevator);}

@Override
public void initialize() {

elevatorsub.setPosicao(position);

}


@Override
public boolean isFinished() {
    
    return Math.abs(elevatorsub.getPosicao() - position) < 1.0;

}

}


