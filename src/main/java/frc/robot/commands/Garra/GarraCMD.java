package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.GarraSUB;
import frc.robot.subsystems.GarraSUB.Estado;

public class GarraCMD extends Command {

    private final GarraSUB garra;
    private final Estado estadoDesejado;
    private final Double anguloAlvo;

    // Construtor 1: Para controlar o ROLO (L2 e R2)
    public GarraCMD(GarraSUB garra, Estado estadoDesejado) {
        this.garra = garra;
        this.estadoDesejado = estadoDesejado;
        this.anguloAlvo = null;
    }

    // Construtor 2: Para controlar o ÂNGULO (os 3 botões)
    public GarraCMD(GarraSUB garra, double anguloAlvo) {
        this.garra = garra;
        this.estadoDesejado = null;
        this.anguloAlvo = anguloAlvo;
    }

    @Override
    public void initialize() {
        if (estadoDesejado != null) {
            garra.setEstado(estadoDesejado);
        }
        if (anguloAlvo != null) {
            garra.SetAngulo(anguloAlvo);
        }
    }

    @Override
    public void end(boolean interrupted) {
        // Quando soltar o gatilho do rolo, ele para sozinho
        if (estadoDesejado != null) {
            garra.setEstado(Estado.PARADA);
        }
    }

    @Override
    public boolean isFinished() {
        // Se for comando de ângulo, termina na hora (true); se for rolo, segura enquanto apertado (false)
        return anguloAlvo != null;
    }
}
