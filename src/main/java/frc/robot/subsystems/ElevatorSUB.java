package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ElevatorSUB extends SubsystemBase {

    private final SparkMax Motor1 = new SparkMax(6, MotorType.kBrushless);
    private final RelativeEncoder encoder = Motor1.getEncoder();
    private final SparkMaxConfig config = new SparkMaxConfig();
    private final SparkClosedLoopController closed = Motor1.getClosedLoopController();

    // Slot 0 = SUBIDA (ajuste aqui)
    private static final ClosedLoopSlot SLOT_SUBIDA = ClosedLoopSlot.kSlot0;
    private static final double KP_SUBIDA = 0.023;
    private static final double KG_SUBIDA = 1.0;

    // Slot 1 = DESCIDA (ajuste aqui)
    private static final ClosedLoopSlot SLOT_DESCIDA = ClosedLoopSlot.kSlot1;
    private static final double KP_DESCIDA = 0.023;
    private static final double KG_DESCIDA = 0.35; // precisa ficar abaixo de ~0.44

    private double setpoint;
    private ClosedLoopSlot slotAtual = SLOT_SUBIDA;

    public enum ElevatorState {
        Movendo,
        Alvo_L1,
        Alvo_L2,
        Alvo_L3,
        PontoZero
    }

    private ElevatorState Estado = ElevatorState.PontoZero;

    public ElevatorSUB() {
        config.smartCurrentLimit(60);
        config.idleMode(IdleMode.kBrake);
        config.closedLoopRampRate(0.8);
        config.encoder.velocityConversionFactor(1);
        config.encoder.positionConversionFactor(Constants.ElevatorConstants.CONVERSAOEMCM);

        // ---------- Slot 0: subida ----------
        config.closedLoop.pid(KP_SUBIDA, 0, 0, SLOT_SUBIDA);
        config.closedLoop.outputRange(-0.8, 1.0, SLOT_SUBIDA);
        config.closedLoop.feedForward.kG(KG_SUBIDA, SLOT_SUBIDA);
        config.closedLoop.feedForward.kV(0.0021, SLOT_SUBIDA);

        // ---------- Slot 1: descida ----------
        config.closedLoop.pid(KP_DESCIDA, 0, 0, SLOT_DESCIDA);
        config.closedLoop.outputRange(-0.8, 1.0, SLOT_DESCIDA);
        config.closedLoop.feedForward.kG(KG_DESCIDA, SLOT_DESCIDA);
        config.closedLoop.feedForward.kV(0.0021, SLOT_DESCIDA);

        Motor1.setPeriodicFrameTimeout(5);
        Motor1.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        // Depois do configure, para o fator de conversão já estar aplicado
        encoder.setPosition(1.21);
    }

    public double getTensao() {
        return Motor1.getAppliedOutput() * Motor1.getBusVoltage();
    }

    public double getOutuputAppy() {
        return Motor1.getAppliedOutput();
    }

    public double getBus() {
        return Motor1.getBusVoltage();
    }

    public double getPosicao() {
        return encoder.getPosition();
    }

    public double getVelocidade() {
        return encoder.getVelocity();
    }

    public void setPosicao(double posicao) {
        setpoint = posicao;
        Estado = ElevatorState.Movendo;

        boolean descendo = posicao < getPosicao();
        slotAtual = descendo ? SLOT_DESCIDA : SLOT_SUBIDA;

        closed.setSetpoint(posicao, ControlType.kPosition, slotAtual);
    }

    public ElevatorState getEstado() {
        return Estado;
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("PosicaoCM(Elevator)", getPosicao());
        SmartDashboard.putNumber("Velocidade(Elevator)", getVelocidade());
        SmartDashboard.putNumber("tensao", getTensao());
        SmartDashboard.putNumber("Apply Output", getOutuputAppy());
        SmartDashboard.putNumber("Elevator/Erro", setpoint - getPosicao());
        SmartDashboard.putString("Elevator/Slot", slotAtual == SLOT_DESCIDA ? "Descida" : "Subida");

        switch (Estado) {

            case Movendo:
                if (Math.abs(getPosicao() - setpoint) <= 1.0) {

                    if (Math.abs(setpoint - Constants.ElevatorConstants.pointL1) <= 1.2) {
                        Estado = ElevatorState.Alvo_L1;
                    } else if (Math.abs(setpoint - Constants.ElevatorConstants.pointL2) <= 1.2) {
                        Estado = ElevatorState.Alvo_L2;
                    } else if (Math.abs(setpoint - Constants.ElevatorConstants.pointL3) <= 1.2) {
                        Estado = ElevatorState.Alvo_L3;
                    } else if (Math.abs(setpoint - Constants.ElevatorConstants.pointl0) <= 1.2) {
                        Estado = ElevatorState.PontoZero;
                    }
                }
                break;

            default:
                break;
        }

        SmartDashboard.putString("Elevator/Estado", getEstado().toString());
    }
}