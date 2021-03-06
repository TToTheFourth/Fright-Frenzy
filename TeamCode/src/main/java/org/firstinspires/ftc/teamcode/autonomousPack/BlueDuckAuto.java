package org.firstinspires.ftc.teamcode.autonomousPack;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RepresentoClass;
import org.firstinspires.ftc.teamcode.TensorFlow;

@Autonomous
@Disabled
public class BlueDuckAuto extends LinearOpMode {
    int level;
    public void runOpMode() throws InterruptedException {
        RepresentoClass b = new RepresentoClass(this); //calls RepresentoClass to bring in the methods
        TensorFlow t = new TensorFlow(this); //calls tensorFlow to be able to call our method that identifies where the duck is
        t.turnOn(); //turns tensorFlow on
        b.startGyro(); //turns the gyro on
        waitForStart(); //waits for program to start
        b.slide(0.5, 20); //goes right 24 inches
        b.duckSpin(); //spins the duck carousel
        b.slide(-0.5, 20); // goes left 24 inches
        b.goForward(0.4, 15); //goes forward to blocks
        if(t.isDuck()) { //sees if duck is in middle spot
            level = 2;
        }
        b.turnLeft(20, .3);
        if(t.isDuck()) { //sees if duck is in left spot
            level = 1;
        }
        b.turnRight(40, .3);
        if(t.isDuck()) { //sees if duck is in right spot
            level = 3;
        }
        if(!t.isDuck()) { //sees if duck is in the middle
            level = 2;
        }
        b.turnLeft(20, .3);
        b.slide(-0.5, 24); //slides left 24 inches to hub
        b.raiseCargo(level/*what level is the duck at?*/); //raises linear slide and places cargo
        b.slide(0.5, 50); //slides right 48 inches
        b.stopMotor(); //ensures all motors are not moving
    }
}
