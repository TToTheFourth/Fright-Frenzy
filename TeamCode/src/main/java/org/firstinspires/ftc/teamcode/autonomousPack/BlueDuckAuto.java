package org.firstinspires.ftc.teamcode.autonomousPack;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RepresentoClass;

@Autonomous
class BlueDuckAuto extends LinearOpMode {
    RepresentoClass b = new RepresentoClass(this);
    public void runOpMode() throws InterruptedException {
        //identify where the duck is, what level the cargo goes on
        b.slide(0.5, 24); //goes right 24 inches
        //spins the duck carousel
        b.slide(-0.5, 48); // goes lift 48 inches
        b.goForward(0.4, 24); //goes forward to hub
        //raise cargo to the correct height
        //place cargo
        b.slide(0.5, 48); //slides right 48 inches
        b.stopMotor(); //ensures all motors are not moving
    }
}