
package org.firstinspires.ftc.teamcode.lastyear;

import android.graphics.Color;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class RepresentoBotMVP {

    private static final double MAX_ANGLE = 5.0;
    private static final double ANGLE_ADJ_PERC = 0.2;
    private static final double ANGLE_ADJ_SPEED = 0.2;


    private Timer myTimer;

    private DcMotor backLeftMotor;
    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backRightMotor;
    private DcMotor shoot;
    private DcMotor elbow;
    private Servo claw;
    private DcMotor thrower;

    private Gyro gyro;
    private LinearOpMode opMode;
    private DistanceSensor sensorDistance;
    private NormalizedColorSensor sensorColor;
    ModernRoboticsI2cRangeSensor rangeSensor;
    private Gyro2 miniGyro;
    private DcMotor convoy;
    private Servo miniSweep;
    private DcMotor sweeper;

    private java.util.Timer timeKeeper = new java.util.Timer();

    public RepresentoBotMVP(LinearOpMode om) {
        this.opMode = om;

        backLeftMotor = opMode.hardwareMap.get(DcMotor.class, "motor0");
        frontLeftMotor = opMode.hardwareMap.get(DcMotor.class, "motor1");
        frontRightMotor = opMode.hardwareMap.get(DcMotor.class, "motor2");
        backRightMotor = opMode.hardwareMap.get(DcMotor.class, "motor3");
        claw = opMode.hardwareMap.get(Servo.class, "claw");
        elbow = opMode.hardwareMap.get(DcMotor.class, "elbow");
        BNO055IMU imu = opMode.hardwareMap.get(BNO055IMU.class, "imu");
        miniSweep = opMode.hardwareMap.get(Servo.class, "servoSweep");
        sweeper = opMode.hardwareMap.get(DcMotor.class, "sweeper");
        thrower = opMode.hardwareMap.get(DcMotor.class, "thrower");
        convoy = opMode.hardwareMap.get(DcMotor.class, "convey2");
        gyro = new Gyro(imu, opMode);
        myTimer = new Timer();
        //stoneServo = opMode.hardwareMap.get(Servo.class, "stoneServo");

        //stops movement of robot quickly.
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void startGyro(){
        gyro.StartGyro();
        miniGyro = gyro.getMiniGyro();
    }

    public void turnRight(double degrees, double power) {
        //
        gyro.resetWithDirection(Gyro.RIGHT);
        // tells the gyro we are turning right

        // start the motors turning right
        double rightY_G1 = 0.0;
        double rightX_G1 = -1.0 * power;
        double leftX_G1 = 0.0;

        frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
        backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
        backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
        frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));
        // connects the motors to the correct variables

        // loop until the robot turns :) degrees
        double d = -1 * degrees;
        while (opMode.opModeIsActive()) {
            if (gyro.getAngle() <= d) {
                break;
            }
        }
        // gets angle turn

        frontLeftMotor.setPower(0.0);
        backLeftMotor.setPower(0.0);
        backRightMotor.setPower(0.0);
        frontRightMotor.setPower(0.0);
        // stops the motors
    }

    public void turnLeft(double degrees, double power) {
        gyro.resetWithDirection(Gyro.LEFT);
        // tells gyro we are going left

        // start the motors turning left
        double rightY_G1 = 0.0;
        double rightX_G1 = 1.0 * power;
        double leftX_G1 = 0.0;

        frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
        backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
        backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
        frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));
        // sets the motors to the correct variables

        // loop until the robot turns :) degrees
        double d = degrees;
        while (opMode.opModeIsActive()) {
            if (gyro.getAngle() >= d) {
                break;
            }
        }
        // gets degrees

        frontLeftMotor.setPower(0.0);
        backLeftMotor.setPower(0.0);
        backRightMotor.setPower(0.0);
        frontRightMotor.setPower(0.0);
        // stops motors
    }

    public void forwardUntilDistance(double until, double power) {
        double rightY_G1 = 1.0 * power;
        double rightX_G1 = 0.0;
        double leftX_G1 = 0.0;
        // sets power

        frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
        backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
        backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
        frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));
        // connects motors to the correct variable(s)

        miniGyro.reset();
        while (opMode.opModeIsActive()) {
            if (sensorDistance.getDistance(DistanceUnit.INCH) < until) {
                break;
            }

            if (miniGyro.getAngle() > MAX_ANGLE) {
                turnRight(ANGLE_ADJ_PERC * miniGyro.getAngle(), ANGLE_ADJ_SPEED);
                frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
                backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
                backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
                frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));

            } else if (miniGyro.getAngle() < -MAX_ANGLE) {
                turnLeft(-ANGLE_ADJ_PERC * miniGyro.getAngle(), ANGLE_ADJ_SPEED);
                frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
                backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
                backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
                frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));

            }
        }
        // sets the distance sensor to go until we are inches away from something

        frontLeftMotor.setPower(0.0);
        backLeftMotor.setPower(0.0);
        backRightMotor.setPower(0.0);
        frontRightMotor.setPower(0.0);
        // stops motor
    }

    public void forwardUntilPink(double power) {
        float hsvValues[] = {0F, 0F, 0F};

        // wait for the start button to be pressed.

        double h;
        double s;
        double v;

        double rightY_G1 = 1.0 * power;
        double rightX_G1 = 0.0;
        double leftX_G1 = 0.0;
        // sets power

        frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
        backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
        backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
        frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));

        miniGyro.reset();
        do {
            NormalizedRGBA colors = sensorColor.getNormalizedColors();
            // normalizes the colors so none of the colors have readings that are off the mark
            // ( a jump of 50 to 1059 )
            float max = Math.max(Math.max(Math.max(colors.red, colors.green), colors.blue), colors.alpha);
            colors.red   /= max;
            colors.green /= max;
            colors.blue  /= max;

            Color.colorToHSV(colors.toColor(), hsvValues);
            // changes rbg to hsv

            h = hsvValues[0];
            s = hsvValues[1];
            v = hsvValues[2];

            if (miniGyro.getAngle() > MAX_ANGLE) {
                turnRight(ANGLE_ADJ_PERC * miniGyro.getAngle(), ANGLE_ADJ_SPEED);
                frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
                backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
                backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
                frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));

            }else if (miniGyro.getAngle() < -MAX_ANGLE){
                turnLeft (-ANGLE_ADJ_PERC * miniGyro.getAngle(), ANGLE_ADJ_SPEED);
                frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
                backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
                backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
                frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));

            }
        } while (339.18094 > h && h > 331.43454 && 0.69088095 > s && s > 0.44286972 && 0.56182235 > v && v > 0.4597227);

        frontLeftMotor.setPower(0.0);
        backLeftMotor.setPower(0.0);
        backRightMotor.setPower(0.0);
        frontRightMotor.setPower(0.0);
        // stops motor
    }


    public void forwardUntilColor(double power) {
        float hsvValues[] = {0F, 0F, 0F};

        // wait for the start button to be pressed.

        double h;
        double s;
        double v;

        double rightY_G1 = 1.0 * power;
        double rightX_G1 = 0.0;
        double leftX_G1 = 0.0;
        // sets power

        frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
        backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
        backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
        frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));

        miniGyro.reset();
        do {
            NormalizedRGBA colors = sensorColor.getNormalizedColors();
            // normalizes the colors so none of the colors have readings that are off the mark
            // ( a jump of 50 to 1059 )
            float max = Math.max(Math.max(Math.max(colors.red, colors.green), colors.blue), colors.alpha);
            colors.red   /= max;
            colors.green /= max;
            colors.blue  /= max;

            Color.colorToHSV(colors.toColor(), hsvValues);
            // changes rbg to hsv

            h = hsvValues[0];
            s = hsvValues[1];
            v = hsvValues[2];

            if (miniGyro.getAngle() > MAX_ANGLE) {
                turnRight(ANGLE_ADJ_PERC * miniGyro.getAngle(), ANGLE_ADJ_SPEED);
                frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
                backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
                backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
                frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));

            }else if (miniGyro.getAngle() < -MAX_ANGLE){
                turnLeft (-ANGLE_ADJ_PERC * miniGyro.getAngle(), ANGLE_ADJ_SPEED);
                frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
                backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
                backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
                frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));

            }
        } while (154.88 > h && h > 103.04 && 0.107 > s && s > 0.055 && 0.361 > v && v > 0.349);

        frontLeftMotor.setPower(0.0);
        backLeftMotor.setPower(0.0);
        backRightMotor.setPower(0.0);
        frontRightMotor.setPower(0.0);
        // stops motor
    }

    public void forwardUntil(double power) {
        float hsvValues[] = {0F, 0F, 0F};

        // wait for the start button to be pressed.

        double h;
        double s;
        double v;

        double rightY_G1 = 1.0 * power;
        double rightX_G1 = 0.0;
        double leftX_G1 = 0.0;
        // sets power

        frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
        backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
        backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
        frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));

        miniGyro.reset();
        do {
            NormalizedRGBA colors = sensorColor.getNormalizedColors();
            // normalizes the colors so none of the colors have readings that are off the mark
            // ( a jump of 50 to 1059 )
            float max = Math.max(Math.max(Math.max(colors.red, colors.green), colors.blue), colors.alpha);
            colors.red   /= max;
            colors.green /= max;
            colors.blue  /= max;

            Color.colorToHSV(colors.toColor(), hsvValues);
            // changes rbg to hsv

            h = hsvValues[0];
            s = hsvValues[1];
            v = hsvValues[2];

            if (miniGyro.getAngle() > MAX_ANGLE) {
                turnRight(ANGLE_ADJ_PERC * miniGyro.getAngle(), ANGLE_ADJ_SPEED);
                frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
                backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
                backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
                frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));

            }else if (miniGyro.getAngle() < -MAX_ANGLE){
                turnLeft (-ANGLE_ADJ_PERC * miniGyro.getAngle(), ANGLE_ADJ_SPEED);
                frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
                backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
                backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
                frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));

            }
        } while (!(212.82 > h && h > 211.81 && 0.721 > s && s > 0.705 && 0.542 > v && v > 0.521) || !(357.21 > h && h > 355.42 && 0.690 > s && s > 0.676 && 0.651 > v && v > 0.639));

        frontLeftMotor.setPower(0.0);
        backLeftMotor.setPower(0.0);
        backRightMotor.setPower(0.0);
        frontRightMotor.setPower(0.0);
        // stops motor
    }

    public void slide (double power, double distance) {

        // sets power
        // left is negative

        double rightY_G1 = 0;
        double rightX_G1 = 0;
        double leftY_G1 = 0;
        double leftX_G1 = -power;

        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // sets encoder

        miniGyro.reset();
        long ticks = ticksToInchesSlide(distance);
        while (opMode.opModeIsActive()) {
            int rotations = frontRightMotor.getCurrentPosition();
            if (rotations<0) {
                rotations = rotations * -1;
            }
            if (rotations >= ticks) {
                break;
            }

            // Get the current heading; anything other than 0 is off course
            // this will return positive angle if drifting CCW
            // this will return negative angle if drifting CW
            double angle = miniGyro.getAngle();

            // Correct rightX_G1 [-1.0,1.0] to adjust turn
            // if rightX_G1 < 0 then robot will turn left
            // if rightX_G1 > 0 then robot will turn right
            rightX_G1 = -1.0 * angle * 0.022;

            frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
            backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
            backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
            frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));
        }

        // sets the inches to ticks so the motors understand
        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        frontRightMotor.setPower(0);
    }

    public void slideOld (double power, double distance) {

        // sets power
        // left is negative

        double rightY_G1 = 0;
        double rightX_G1 = 0;
        double leftY_G1 = 0;
        double leftX_G1 = -power;

        double frontLeft = (rightX_G1 + rightY_G1 - leftX_G1);
        double backLeft = (rightX_G1 + rightY_G1 + leftX_G1);
        double backRight = (rightX_G1 - rightY_G1 + leftX_G1);
        double frontRight = (rightX_G1 - rightY_G1 - leftX_G1);

        frontLeftMotor.setPower(frontLeft);
        backLeftMotor.setPower(backLeft);
        backRightMotor.setPower(backRight);
        frontRightMotor.setPower(frontRight);

        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // sets encoder

        miniGyro.reset();
        long ticks = ticksToInchesSlide(distance);
        while (opMode.opModeIsActive()) {
            int rotations = frontRightMotor.getCurrentPosition();
            if (rotations<0) {
                rotations = rotations * -1;
            }
            if (rotations >= ticks) {
                break;
            }

            if (miniGyro.getAngle() > MAX_ANGLE) {
                turnRight(ANGLE_ADJ_PERC * miniGyro.getAngle(), ANGLE_ADJ_SPEED);
                frontLeftMotor.setPower(frontLeft);
                backLeftMotor.setPower(backLeft);
                backRightMotor.setPower(backRight);
                frontRightMotor.setPower(frontRight);
            }else if (miniGyro.getAngle() < -MAX_ANGLE){
                turnLeft (-ANGLE_ADJ_PERC * miniGyro.getAngle(), ANGLE_ADJ_SPEED);
                frontLeftMotor.setPower(frontLeft);
                backLeftMotor.setPower(backLeft);
                backRightMotor.setPower(backRight);
                frontRightMotor.setPower(frontRight);
            }
        }
        // sets the inches to ticks so the motors understand
        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        frontRightMotor.setPower(0);
    }

    public void slideWithoutFix (double power, double distance) {
        double rightY_G1 = 0.0;
        double rightX_G1 = 0.0;
        double leftX_G1 = 1.0 * power;
        // sets
        // negative power is left

        double slowperc = 0.6;
        frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
        backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
        backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1)*slowperc);
        frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));
        // connects power to the correct variables

        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // sets encoder

        miniGyro.reset();
        long ticks = ticksToInchesSlide(distance);
        while (opMode.opModeIsActive()) {
            int rotations = backLeftMotor.getCurrentPosition();
            if (rotations<0) {
                rotations = rotations * -1;
            }
            if (rotations >= ticks) {
                break;
            }

            if (miniGyro.getAngle() > MAX_ANGLE) {
                turnRight(ANGLE_ADJ_PERC * miniGyro.getAngle(), ANGLE_ADJ_SPEED);
                frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
                backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
                backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1)*slowperc);
                frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));
            }else if (miniGyro.getAngle() < -MAX_ANGLE){
                turnLeft (-ANGLE_ADJ_PERC * miniGyro.getAngle(), ANGLE_ADJ_SPEED);
                frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
                backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
                backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1)*slowperc);
                frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));
            }
        }
        // sets the inches to ticks so the motors understand
        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        frontRightMotor.setPower(0);
    }

    public void slideWhile(double power) {
        double rightY_G1 = 0.0;
        double rightX_G1 = 0.0;
        double leftX_G1 = 1.0 * power;
        // sets power

        frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
        backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
        backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
        frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));
        // sets variables to the correct variables

        float hsvValues[] = {0, 0, 0};
        while (opMode.opModeIsActive()) {
            getColor(hsvValues);
            float h = hsvValues[0];
            float s = hsvValues[1];
            float v = hsvValues[2];
            if (!(35.1 < h && 47.1 > h && 0.4 < s && 0.751 > s && 0.333 < v && 0.557 > v)) {
                break;
            }
            // tells the sensor what the hsv values need to be before it needs to stop
            /*
            H: 45.1 - 37.1
            S: 0.651 - 0.500
            V: 0.457 - 0.433
            */
        }

        frontLeftMotor.setPower(0.0);
        backLeftMotor.setPower(0.0);
        backRightMotor.setPower(0.0);
        frontRightMotor.setPower(0.0);
        // stops the motors
    }

    public void getColor(float [] hsvValues) {
        NormalizedRGBA colors = sensorColor.getNormalizedColors();
        float max = Math.max(Math.max(Math.max(colors.red, colors.green), colors.blue), colors.alpha);
        colors.red   /= max;
        colors.green /= max;
        colors.blue  /= max;
        // normalizes colors

        Color.colorToHSV(colors.toColor(), hsvValues);
        // tells the colors now that they are normalized
    }

    public void goForward(double power, double distance){
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // sets the encoders

        double rightY_G1 = 1.0 * power;
        double rightX_G1 = 0.0;
        double leftX_G1 = 0.0;
        // sets power

        frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
        backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
        backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
        frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));
        // sets the correct variables to the motors

        long ticks = ticksToInchesForward(distance);
        miniGyro.reset();
        while (opMode.opModeIsActive()) {
            int rotations = frontRightMotor.getCurrentPosition();
            if (rotations<0) {
                rotations = rotations * -1;
            }
            if (rotations >= ticks) {
                break;
            }

            if (miniGyro.getAngle() > MAX_ANGLE) {
                turnRight(ANGLE_ADJ_PERC * miniGyro.getAngle(), ANGLE_ADJ_SPEED);
                frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
                backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
                backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
                frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));
            }else if (miniGyro.getAngle() < -MAX_ANGLE){
                turnLeft (-ANGLE_ADJ_PERC * miniGyro.getAngle(), ANGLE_ADJ_SPEED);
                frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
                backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
                backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
                frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));
            }
            // makes inches transfer to ticks
        }

        frontLeftMotor.setPower(0.0);
        backLeftMotor.setPower(0.0);
        backRightMotor.setPower(0.0);
        frontRightMotor.setPower(0.0);
        // sets motors to zero
    }

    public void goForwardGyroErrorCorrection(double power, double distance){
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // sets the encoders

        double rightY_G1 = 1.0 * power;
        double rightX_G1 = 0.0;
        double leftX_G1 = 0.0;
        // sets power

        // sets the correct variables to the motors

        long ticks = ticksToInchesForward(distance);

        double minPower = 0.3;
        if(power < 0) {
            minPower = minPower * -1.0;
        }
        double increment = (power - minPower) / 2.0;



        miniGyro.reset();
        while (opMode.opModeIsActive()) {
            int rotations = frontRightMotor.getCurrentPosition();
            if (rotations<0) {
                rotations = rotations * -1;
            }
            if (rotations >= ticks) {
                break;
            }

            long rem = ticks - rotations;
            if(rotations < 360) {
                rightY_G1 = minPower;
            } else if( rotations >= 360 && rotations < 720) {
                rightY_G1 = minPower + increment;
            } else if(rem < 360) {
                rightY_G1 = minPower;
            } else if( rem >= 360 && rem < 720) {
                rightY_G1 = minPower + increment;
            }  else  {
                rightY_G1 = power;
            }

            // slow down the last 4 inches
            //if(ticks - rotations < 160) {
            //    rightY_G1 = power * 0.5;
            //}

            // Get the current heading; anything other than 0 is off course
            // this will return positive angle if drifting CCW
            // this will return negative angle if drifting CW
            double angle = miniGyro.getAngle();

            // Correct rightX_G1 [-1.0,1.0] to adjust turn
            // if rightX_G1 < 0 then robot will turn left
            // if rightX_G1 > 0 then robot will turn right
            rightX_G1 = -1.0 * angle * 0.022;

            frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
            backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
            backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
            frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));

        }

        frontLeftMotor.setPower(0.0);
        backLeftMotor.setPower(0.0);
        backRightMotor.setPower(0.0);
        frontRightMotor.setPower(0.0);
        // sets motors to zero
    }

    public void goForwardNoGyro(double power, double distance){
        double rightY_G1 = 1.0 * power;
        double rightX_G1 = 0.0;
        double leftX_G1 = 0.0;
        // sets power

        frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
        backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
        backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
        frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));
        // sets the correct variables to the motors

        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // sets the encoders

        long ticks = ticksToInchesForward(distance);

        while (opMode.opModeIsActive()) {
            int rotations = backLeftMotor.getCurrentPosition();
            if (rotations<0) {
                rotations = rotations * -1;
            }
            if (rotations >= ticks) {
                break;
            }

            // makes inches transfer to ticks
        }

        frontLeftMotor.setPower(0.0);
        backLeftMotor.setPower(0.0);
        backRightMotor.setPower(0.0);
        frontRightMotor.setPower(0.0);
        // sets motors to zero
    }
    public void clawOpenPosition() {
        claw.setPosition(1);

    }
    public void clawClosePosition() {
        claw.setPosition(-1);

    }
    public void stopMotor(){
        frontLeftMotor.setPower(0.0);
        backLeftMotor.setPower(0.0);
        backRightMotor.setPower(0.0);
        frontRightMotor.setPower(0.0);
    }
    public long ticksToInchesForward(double inches) {
        return (long) (inches * 38.4);
        // ticks forward formula
    }
    public long ticksToInchesSlide(double inches) {
        return (long) (inches * 74.6);
        // tick to slide inches formula
    }
    public long inchesToTime(double inches, double power) {
        return (long) (0.0384 * inches * 500.0 / power);
        // inches to time formula
    }

    public void shootRings(long seconds) {
        elbow.setPower(-0.5);
        opMode.sleep(800);
        elbow.setPower(0);
        thrower.setPower(0.9);
        opMode.sleep(2000);

        convoy.setPower(-0.5);

        opMode.sleep(1000);

        sweeper.setPower(1);
        opMode.sleep(seconds * 1000);

        sweeper.setPower(0);
        convoy.setPower(0);
        thrower.setPower(0);
    }

    public void dropSweep() {
        miniSweep.setPosition(-1);
        opMode.sleep(500);
    }

    //  +++++++========================--------
    //  ramp up                        ramp down
    public void goForwardRamp(double power, double distance){
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        double rightY_G1 = 1.0 * power;
        double rightX_G1 = 0.0;
        double leftX_G1 = 0.0;

        long ticks = ticksToInchesForward(distance);

        // TODO: figure out the increment value based on the target speed
        float increement = 0.0f;

        // TODO: set the rightY_G1 start speed to a low value, but not so low the motors won't turn

        miniGyro.reset();
        while (opMode.opModeIsActive()) {
            int rotations = frontRightMotor.getCurrentPosition();
            if (rotations<0) {
                rotations = rotations * -1;
            }
            if (rotations >= ticks) {
                break;
            }

            // TODO: if a certain number of ticks has occured then increment rightY_G1 until desired speed

            // TODO: if we are approaching the target distance then start to decrement rightY_G1 speed

            // Get the current heading; anything other than 0 is off course
            // this will return positive angle if drifting CCW
            // this will return negative angle if drifting CW
            double angle = miniGyro.getAngle();

            // Correct rightX_G1 [-1.0,1.0] to adjust turn
            // if rightX_G1 < 0 then robot will turn left
            // if rightX_G1 > 0 then robot will turn right
            rightX_G1 = -1.0 * angle * 0.022;

            frontLeftMotor.setPower((rightX_G1 + rightY_G1 - leftX_G1));
            backLeftMotor.setPower((rightX_G1 + rightY_G1 + leftX_G1));
            backRightMotor.setPower((rightX_G1 - rightY_G1 + leftX_G1));
            frontRightMotor.setPower((rightX_G1 - rightY_G1 - leftX_G1));

        }

        // sets motors to zero
        frontLeftMotor.setPower(0.0);
        backLeftMotor.setPower(0.0);
        backRightMotor.setPower(0.0);
        frontRightMotor.setPower(0.0);
    }
}