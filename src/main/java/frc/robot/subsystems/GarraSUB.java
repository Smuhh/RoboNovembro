package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class GarraSUB extends SubsystemBase {
    
    SparkMax MotorRolo = new SparkMax(8, MotorType.kBrushless);
    SparkMax MotorAngulo = new SparkMax(7, MotorType.kBrushless);
    SparkClosedLoopController controllerAngulo = MotorAngulo.getClosedLoopController();
    
    public GarraSUB() {

        // Configuração do motor Rolo
        SparkMaxConfig configRolo = new SparkMaxConfig();
        
        configRolo
            .idleMode(IdleMode.kBrake)
            .smartCurrentLimit(40)
            .openLoopRampRate(0.5);

        MotorRolo.configure(
            configRolo,
            ResetMode.kResetSafeParameters, 
            PersistMode.kPersistParameters
        );

        // Configuração do motor Angulo
        SparkMaxConfig configAngulo = new SparkMaxConfig();
        
        configAngulo
            .idleMode(IdleMode.kBrake)
            .smartCurrentLimit(40);

        configAngulo.closedLoop
            .pid(0.05, 0, 0.0);

        configAngulo.closedLoop.feedForward
            .kG(0.12);

        configAngulo.closedLoop.maxMotion
            .cruiseVelocity(500)
            .maxAcceleration(600)       
            .allowedProfileError(0.5); 

        MotorAngulo.configure(
            configAngulo, 
            ResetMode.kResetSafeParameters,
            PersistMode.kPersistParameters
        );
    }

    // métodos 

    public void SetRolo(double RPM_Speed_C) {
        MotorRolo.set(RPM_Speed_C);
    }

    public void SetAngulo(double angulo) {
        controllerAngulo.setSetpoint(angulo, ControlType.kPosition);
    }

    public void Setparar_Rolo() {
        MotorRolo.set(0);
    }

    public double GetAngulo() {
        return MotorAngulo.getEncoder().getPosition();    
    }

    public enum Estado {
        PARADA,
        COLETANDO,
        EJETANDO
    }
    
    private Estado estadoAtual = Estado.PARADA;

    public void setEstado(Estado novoEstado) {
        this.estadoAtual = novoEstado;
    }
    
    @Override
    public void periodic() {
        switch (estadoAtual) {
            case COLETANDO:
                SetRolo(0.5);
                break;
            case EJETANDO:
                SetRolo(-0.5);
                break;
            case PARADA:
                SetRolo(0);
                break;
        }

        SmartDashboard.putNumber("angulo", GetAngulo());
    }
}