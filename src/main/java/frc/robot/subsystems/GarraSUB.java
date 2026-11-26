package frc.robot.subsystems;

import org.littletonrobotics.junction.Logger;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class GarraSUB extends SubsystemBase {

    // ===== Constantes do rolo (ajuste pelo log do AdvantageScope) =====
    private static final double POTENCIA_COLETA = 0.5;
    private static final double POTENCIA_EJECAO = 0.86;
    private static final double POTENCIA_RETENCAO = 0.00;   // segura a peça depois de coletar
    private static final double CORRENTE_COLETA = 10.0;      // A -> olhe Garra/CorrenteFiltrada e ajuste
    private static final double TEMPO_IGNORAR_PICO = 0.3;   // s -> ignora o pico de partida do motor
    private static final double TEMPO_DEBOUNCE = 0.05;      // s
    private static final double TEMPO_EJECAO = 1.0;         // s
    
    // ===== Estados =====
    public enum Estado {
        PARADA,
        COLETANDO,
        EJETANDO,
        PECA_COLETADA
    }

    private Estado estadoAtual = Estado.PARADA;

    // ===== Hardware =====
    private final SparkMax MotorRolo = new SparkMax(8, MotorType.kBrushless);
    private final SparkMax MotorAngulo = new SparkMax(7, MotorType.kBrushless);
    private final SparkClosedLoopController controllerAngulo = MotorAngulo.getClosedLoopController();
    private final RelativeEncoder encoder = MotorAngulo.getEncoder();

    // ===== Detecção de peça =====
    private final Debouncer debouncerColeta =
            new Debouncer(TEMPO_DEBOUNCE, Debouncer.DebounceType.kRising);
    private final MedianFilter filtroCorrente = new MedianFilter(5);
    private final Timer timerColeta = new Timer();
    private final Timer timerEjecao = new Timer();
    private double correnteFiltrada = 0.0;

    public GarraSUB() {

        // Configuração do motor Rolo
        SparkMaxConfig configRolo = new SparkMaxConfig();

        configRolo
            .idleMode(IdleMode.kBrake)   // brake ajuda a segurar a peça
            .smartCurrentLimit(40)
            .openLoopRampRate(0.5);

        MotorRolo.configure(
            configRolo,
            ResetMode.kResetSafeParameters,
            PersistMode.kPersistParameters);

        // Configuração do motor Angulo (sem mudanças por enquanto)
        SparkMaxConfig configAngulo = new SparkMaxConfig();

        configAngulo.idleMode(IdleMode.kBrake).smartCurrentLimit(40);

        configAngulo.closedLoop.pid(0.05, 0, 0.0);

        configAngulo.closedLoop.feedForward.kG(0.12);

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

    // ===== Métodos =====

    public void SetRolo(double potencia) {
        MotorRolo.set(potencia);
    }

    public void SetAngulo(double angulo) {
        controllerAngulo.setSetpoint(angulo, ControlType.kPosition);
    }

    public void Parar() {
        setEstado(Estado.PARADA);
    }

    public double GetAngulo() {
        return encoder.getPosition();
    }

    public double getCorrenteRolo() {
        return MotorRolo.getOutputCurrent();
    }

    public Estado getEstado() {
        return estadoAtual;
    }

    public void setEstado(Estado novoEstado) {
        this.estadoAtual = novoEstado;
    }

    /** Inicia a coleta. Sempre reinicia, então dá para recoletar se a peça caiu. */
    public void Coletar() {
        debouncerColeta.calculate(false);
        filtroCorrente.reset();
        correnteFiltrada = 0.0;
        timerColeta.restart();
        setEstado(Estado.COLETANDO);
    }

    public void Ejetar() {
        timerEjecao.restart();
        setEstado(Estado.EJETANDO);
    }

    @Override
    public void periodic() {
        switch (estadoAtual) {
            case COLETANDO:
                SetRolo(POTENCIA_COLETA);

                correnteFiltrada = filtroCorrente.calculate(getCorrenteRolo());

                boolean correnteAlta = timerColeta.hasElapsed(TEMPO_IGNORAR_PICO)
                        && correnteFiltrada >= CORRENTE_COLETA;

                if (debouncerColeta.calculate(correnteAlta)) {
                    setEstado(Estado.PECA_COLETADA);
                }
                break;

            case PECA_COLETADA:
                SetRolo(POTENCIA_RETENCAO);
                break;

            case EJETANDO:
                SetRolo(-POTENCIA_EJECAO);

                if (timerEjecao.hasElapsed(TEMPO_EJECAO)) {
                    timerEjecao.stop();
                    SetRolo(0);
                    setEstado(Estado.PARADA);
                }
                break;

            case PARADA:
                SetRolo(0);
                break;
        }

        Logger.recordOutput("Garra/Angulo", GetAngulo());
        Logger.recordOutput("Garra/CurrentRolo", getCorrenteRolo());
        Logger.recordOutput("Garra/CorrenteFiltrada", correnteFiltrada);
        Logger.recordOutput("Garra/Estado", getEstado());
    }
}