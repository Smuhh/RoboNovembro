package frc.robot.commands.Garra;

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
        addRequirements(garra);
    }

    // Construtor 2: Para controlar o ÂNGULO (os 3 botões)
    public GarraCMD(GarraSUB garra, double anguloAlvo) {
        this.garra = garra;
        this.estadoDesejado = null;
        this.anguloAlvo = anguloAlvo;
        addRequirements(garra);
    }

    @Override
    public void initialize() {
        if (anguloAlvo != null) {
            garra.SetAngulo(anguloAlvo);
        }

        if (estadoDesejado == Estado.COLETANDO) {
            garra.Coletar();
        }

        if (estadoDesejado == Estado.EJETANDO) {
            garra.Ejetar();
        }
    }

    @Override
    public void end(boolean interrupted) {
        // Ejeção cancelada: para o rolo
        if (estadoDesejado == Estado.EJETANDO) {
            garra.setEstado(Estado.PARADA);
        }

        // Coleta cancelada no meio: para o rolo.
        // Só mexe se ainda estiver coletando, para não apagar um PECA_COLETADA.
        if (estadoDesejado == Estado.COLETANDO && interrupted
                && garra.getEstado() == Estado.COLETANDO) {
            garra.setEstado(Estado.PARADA);
        }
    }

    @Override
    public boolean isFinished() {
        if (anguloAlvo != null) {
            return Math.abs(garra.GetAngulo() - anguloAlvo) < 2.0;
        }

        if (estadoDesejado == Estado.COLETANDO) {
            return garra.getEstado() == Estado.PECA_COLETADA;
        }

        // Ejeção termina sozinha quando o timer da garra volta o estado para PARADA
        if (estadoDesejado == Estado.EJETANDO) {
            return garra.getEstado() != Estado.EJETANDO;
        }

        return false;
    }
}