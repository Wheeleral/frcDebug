package frc.robot.devices;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

public class NeoMotor implements IMotor {

    protected CANSparkMax motor;
    private RelativeEncoder encoder;
    private SparkMaxPIDController pidController;
    private double voltage = 0.0;
    private double motorValue = 0.0;
    private CANSparkMax.ControlType motorControlType = CANSparkMax.ControlType.kDutyCycle;
    private NeoMotor leaderMotor;

    // These are needed because getting these values from the PIDController
    // takes an excessively long time for some reason
    private double kP = 0;
    private double kI = 0;
    private double kD = 0;
    private double kF = 0;

    public NeoMotor(int port) {
       motor = new CANSparkMax(port, MotorType.kBrushless);
       motor.restoreFactoryDefaults();
       encoder = motor.getEncoder();
       pidController = motor.getPIDController();
       setPercent(0);
    }

    public void setSpeed(double speed){
        motorValue = speed / encoder.getVelocityConversionFactor();
        motorControlType = CANSparkMax.ControlType.kVelocity;
        pidController.setReference(motorValue, motorControlType);
    }

    public double getSpeed(){
        double rate = encoder.getVelocity();
        return motor.getInverted() ? -rate : rate;

    }

    public void setpercent(double percent){
        motorValue= percent;
        motorControlType = CANSparkMax.ControlType.kDutyCycle;
        voltage = percent;
        pidController.setReference(motorValue, motorControlType);
    }

    public double getPercent(){
        return this.voltage;
    }

    public void setDistance(double dist){
        motorValue = dist;
        motorControlType = CANSparkMax.ControlType.kPosition;
        pidController.setReference(motorValue, motorControlType);
    }

    public void resetEncoder(double distance) {
        encoder.setPosition(distance);
    }

    public double getDistance(){
        return encoder.getPosition();
    }

    public void setConversionFactor(double factor){
        encoder.setPositionConversionFactor(factor);
        encoder.setVelocityConversionFactor(factor / 60);
    }

    public double getConversionFactor(){
        return encoder.getPositionConversionFactor();
    }

    public void setInverted(boolean isInverted) {
        motor.setInverted(isInverted);
    }
    public boolean getInverted() {
        return motor.getInverted();
    }
    
    public void setPid(double kP, double kI, double kD){
        setP(kP);
        setI(kI);
        setD(kD);       
    }

    public void setPidf(double kP, double kI, double kD, double kF) {
        setP(kP);
        setI(kI);
        setD(kD);
        setF(kF);
    }

    public void setP(double kP){
        this.kP = kP;
        pidController.setP(kP);
    }

    public void setI(double kI){
        this.kI = kI;
        pidController.setI(kI);
    }

    public void setD(double kD){
        this.kD = kD;
        pidController.setD(kD);
    }

    public void setF(double kF) {
        this.kF = kF;
        pidController.setFF(kF);
    }

    public double getP(){
        return kP;
    }

    public double getI(){
        return kI;
    }

    public double getD(){
        return kD;
    }

    public double getF() {
        return kF;
    }

    public void follow(IMotor leader){
        if(leader.getClass() == NeoMotor.class) {
            leaderMotor = (NeoMotor)leader;
            this.motor.follow(this.leaderMotor.motor);
        
        }
    }
    public void setEncoderPositionAndRate(double position, double rate){
    }

    public double getEncoderCount() {
        return getDistance() / getConversionFactor();
    }

    public void setPercent(double percent) {
        //i forgot what goes here
    }

}
