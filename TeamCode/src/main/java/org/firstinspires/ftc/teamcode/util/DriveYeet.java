package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Disabled
@TeleOp
public class DriveYeet extends LinearOpMode {
    //declares each sensor, motor, and servo
    private double servSpeed1;
    private DcMotor backLeftMotor;
    private DcMotor frontLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor frontRightMotor;
    private DcMotor spinner;
    private DcMotor sweepo;
    private DcMotor slide;
    private Servo cargo;
    private Servo iconServoR;
    private Servo iconServoL;
    private DistanceSensor distanceSensor;


    @Override
    public void runOpMode() throws InterruptedException {
        // hardware for:
        // --sweeper motor
        // --box servo
        // --linear slide motor
        // --spinner servo

        // type name = value
        //connects each motor, sensor, and servo to the hardware map
        sweepo = hardwareMap.get(DcMotor.class, "sweepo");
        slide = hardwareMap.get(DcMotor.class, "slide");
        cargo = hardwareMap.get(Servo.class, "boxservo");
        iconServoR = hardwareMap.get(Servo.class, "servo1");
        iconServoL = hardwareMap.get(Servo.class, "servo0");
        spinner = hardwareMap.get(DcMotor.class, "spinner");
        backLeftMotor = hardwareMap.get(DcMotor.class, "motor0");
        frontLeftMotor = hardwareMap.get(DcMotor.class, "motor1");
        frontRightMotor = hardwareMap.get(DcMotor.class, "motor2");
        backRightMotor = hardwareMap.get(DcMotor.class, "motor3");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "sensor4");


        //sets each motor to have a brake method, this significantly reduces drift
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        spinner.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart(); //waits for start

        boolean useDistanceSensor = true;

        Timer timer = new Timer();
        while (opModeIsActive()){

            //declares neccesary variables for mechinam wheels
            double rightX_G1;
            double rightY_G1;
            double leftX_G1;
            double leftY_G1;

            //sets unit for distance sensor
            double distance = distanceSensor.getDistance(DistanceUnit.INCH);

            // half speed while right bumper is pressed
            double wubble = 0.5;
            if (gamepad1.right_bumper) {
                wubble = 1;
            }

            //sets variables to the gamepad controls
            rightY_G1 = -gamepad1.right_stick_y * wubble;
            rightX_G1 = -gamepad1.right_stick_x * wubble;
            leftY_G1 = gamepad1.left_stick_y * wubble;
            leftX_G1 = -gamepad1.left_stick_x * wubble;

            //uses math specific to mechinam wheels to go forward, backwards, and slide left and right
            double frontLeft = (rightX_G1 + rightY_G1 - leftX_G1);
            double backLeft = (rightX_G1 + rightY_G1 + leftX_G1);
            double backRight = (rightX_G1 - rightY_G1 + leftX_G1);
            double frontRight = (rightX_G1 - rightY_G1 - leftX_G1);

            //sets power to doubles
            frontLeftMotor.setPower(frontLeft);
            backLeftMotor.setPower(backLeft);
            backRightMotor.setPower(backRight);
            frontRightMotor.setPower(frontRight);

            // map controller for:
            // --sweeper motor
            // --box servo
            // --linear slide motor
            // --spinner servo

            if (gamepad2.right_trigger > 0) {
                sweepo.setPower(-gamepad2.right_trigger);
            } else if (gamepad2.left_trigger > 0) {
                sweepo.setPower(gamepad2.left_trigger);
            } else {
                sweepo.setPower(0);
            }

            if (gamepad2.circle) {
                useDistanceSensor = true;
            } else if (gamepad2.x) {
                useDistanceSensor = false;
            }

            if(!gamepad2.dpad_up) {
                // if not raising the box turn off the timer
                timer.stop();
            } else if(gamepad2.dpad_up && !timer.isRunning() && distance <= 3.0) {
                // if the timer is not already running and the box is starting
                // to be raised from ramp position (dist < 3) then start the timer
                // to give the servo time to move to the safe position
                timer.start(1000); // 1.0 seconds
            }

            // turn on the slide motor if
            // 1. dpad_up is pressed
            // 2. the servo timer is not running OR the server timer is running and 1 sec has passed
            // 3. the don't use distance sensor OR distance is less than 15
            if(gamepad2.dpad_up && ((timer.isRunning() && timer.check()) || !timer.isRunning()) && (!useDistanceSensor || distance <= 15.0)) {
                // only start to move up if the servo has had time
                // to move to the safe position
                slide.setPower(1.0);
            } else if (gamepad2.dpad_down && (!useDistanceSensor || distance >= 2.4)) {
                slide.setPower(-1);
            } else {
                slide.setPower(0);
            }

            /*
            if (gamepad2.dpad_up && distance < 4) {
                slide.setPower(0.3);
            } else if (gamepad2.dpad_up && distance >= 4) {
                slide.setPower(1.0);
            } else if (gamepad2.dpad_down && distance >= 2.5) {
                slide.setPower(-1);
            } else {
                slide.setPower(0);
            }
*/
            //method that makes the cargo box be in the correct position
            if(gamepad2.dpad_up || gamepad2.dpad_down) {
                cargo.setPosition(0.28); // safe pose
            }
            else if (gamepad2.triangle && (!useDistanceSensor || distance > 2.5) && cargo.getPosition() < 0.6){
                cargo.setPosition(Servo.MIN_POSITION); // drop pos
            }
            else if((useDistanceSensor && distance < 2.5) || (!useDistanceSensor && gamepad2.a)) {
                cargo.setPosition(Servo.MAX_POSITION); // ramp pos
            }
            else {
                cargo.setPosition(0.28); // safe pose
            }

            //makes spinner power conditional to the right and left bumpers of gamepad 2
            if (gamepad2.right_bumper){
                spinner.setPower(1.0);
            } else if (gamepad2.left_bumper) {
                spinner.setPower(-1.0);
            } else {
                spinner.setPower(0.0);
            }

            //code for icon grabber
//            if (gamepad1.a) {
//                iconServoL.setPosition(1);
//                iconServoR.setPosition(0);
//            } else if (gamepad1.b) {
//                iconServoL.setPosition(1);
//                iconServoR.setPosition(1);
//            } else if (gamepad1.y) {
//                iconServoL.setPosition(0);
//                iconServoR.setPosition(0);
//            }

        }
    }
}