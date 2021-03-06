package org.firstinspires.ftc.teamcode.lastyear;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Disabled
@Autonomous
public class ShootTest extends LinearOpMode {
    private DcMotor claw;

    @Override
    public void runOpMode() {
        //RepresentoBotMVP  bot = new RepresentoBotMVP(this);

        claw = hardwareMap.get(DcMotor.class, "claw");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        claw.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        while (opModeIsActive()) {

            claw.setPower(1000);

            telemetry.addData("Target Power Claw", -5);

        }

        claw.setPower(0);

    }
}
