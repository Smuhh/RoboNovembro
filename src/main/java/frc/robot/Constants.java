// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import swervelib.math.Matter;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean constants. This
 * class should not be used for any other purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants
{

  public static final double ROBOT_MASS = (148 - 20.3) * 0.453592; // 32lbs * kg per pound
  public static final Matter CHASSIS    = new Matter(new Translation3d(0, 0, Units.inchesToMeters(8)), ROBOT_MASS);
  public static final double LOOP_TIME  = 0.13; //s, 20ms + 110ms sprk max velocity lag
  public static final double MAX_SPEED  = Units.feetToMeters(14.5);
  // Maximum speed of the robot in meters per second, used to limit acceleration.

//  public static final class AutonConstants
//  {
//
//    public static final PIDConstants TRANSLATION_PID = new PIDConstants(0.7, 0, 0);
//    public static final PIDConstants ANGLE_PID       = new PIDConstants(0.4, 0, 0.01);
//  }

  public static final class DrivebaseConstants
  {

    // Hold time on motor brakes when disabled
    public static final double WHEEL_LOCK_TIME = 10; // seconds
  }

  public static class OperatorConstants
  {

    // Joystick Deadband
    public static final double DEADBAND        = 0.1;
    public static final double LEFT_Y_DEADBAND = 0.1;
    public static final double RIGHT_X_DEADBAND = 0.1;
    public static final double TURN_CONSTANT    = 6;
  }

  public static class ElevatorConstants {
    public static final double PASSO_DACORRENTEMM = 5.25;
    public static final double Reducao = 5.0; 
    public static final double DENTES_CATRACA = 18.0;
    public static final double CONVERSAOEMCM = (DENTES_CATRACA * PASSO_DACORRENTEMM) / (10 * Reducao);
    
    public static final double pointL1 = 2.4;
    public static final double pointL2 = 9.714987754821777; //38.20 antigo
    public static final double pointL3 = 32.25994110107422;  //original = 43.0
  }

  public static class GarraConstants {
    public static final double ANGULO_PONTO_1 = 1.04761934280395; //0.53
    public static final double ANGULO_PONTO_ERETO = 3.57; //1.85
  }
 
  public static final Mode simMode = Mode.REPLAY; // REPLAY para usar o log

  public enum Mode { SIM, REPLAY }

  public static class LimelightConstants{
   public static final double txvalue = 7.50;
  }

  // ... suas outras constantes (ElevatorConstants etc.) continuam aqui
}

