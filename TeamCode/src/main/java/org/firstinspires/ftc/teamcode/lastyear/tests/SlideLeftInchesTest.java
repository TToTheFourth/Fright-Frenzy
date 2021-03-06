package org.firstinspires.ftc.teamcode.lastyear.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.lastyear.RepresentoBotMVP;

/**
 * Expected Result: Robot slides left 50 inches
 */

@Disabled
@Autonomous(group = "Tests")
public class SlideLeftInchesTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        RepresentoBotMVP bot = new RepresentoBotMVP(this);
        bot.startGyro();

        waitForStart();

        bot.slide(-0.5, 50);
    }
}
