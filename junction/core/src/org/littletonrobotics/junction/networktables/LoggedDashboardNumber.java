package org.littletonrobotics.junction.networktables;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.inputs.LoggableInputs;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LoggedDashboardNumber implements LoggedDashboardInput {
  private final String key;
  private double defaultValue;
  private double value;

  private final LoggableInputs inputs = new LoggableInputs() {
    public void toLog(LogTable table) {
      table.put(key, value);
    }

    public void fromLog(LogTable table) {
      value = table.getDouble(key, defaultValue);
    }
  };

  /**
   * Creates a new LoggedDashboardNumber, for handling a number input sent via
   * NetworkTables.
   * 
   * @param key The key for the number, published to
   *            "/SmartDashboard/{key}" for NT or
   *            "/DashboardInputs/{key}" when logged.
   */
  public LoggedDashboardNumber(String key) {
    this(key, 0.0);
  }

  /**
   * Creates a new LoggedDashboardNumber, for handling a number input sent via
   * NetworkTables.
   * 
   * @param key          The key for the number, published to
   *                     "/SmartDashboard/{key}" for NT or
   *                     "/DashboardInputs/{key}" when logged.
   * @param defaultValue The default value if no value in NT is found.
   */
  public LoggedDashboardNumber(String key, double defaultValue) {
    this.key = key;
    this.defaultValue = defaultValue;
    this.value = defaultValue;
    SmartDashboard.putNumber(key, SmartDashboard.getNumber(key, defaultValue));
    periodic();
    Logger.getInstance().registerDashboardInput(this);
  }

  /** Updates the default value, which is used if no value in NT is found. */
  public void setDefault(double defaultValue) {
    this.defaultValue = defaultValue;
  }

  /**
   * Publishes a new value. Note that the value will not be returned by
   * {@link #get()} until the next cycle.
   */
  public void set(double value) {
    SmartDashboard.putNumber(key, value);
  }

  /** Returns the current value. */
  public double get() {
    return value;
  }

  public void periodic() {
    if (!Logger.getInstance().hasReplaySource()) {
      value = SmartDashboard.getNumber(key, defaultValue);
    }
    Logger.getInstance().processInputs(prefix, inputs);
  }
}