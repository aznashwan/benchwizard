package com.vlad.rain.input;

import com.vlad.rain.ai.Droid;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by vlad on 4/4/16.
 */
public class SmartKey extends Key
{

    public SmartKey(Droid droid){
        super(droid);
    }

    @Override
    public void update() {

        try {

            Robot robot = new Robot();
            int bestKey = this.droid.bestMove();

            robot.keyPress(bestKey);
            robot.delay(100);
            robot.keyRelease(bestKey);

        } catch (AWTException e) {
            e.printStackTrace();
        }

        super.update();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        droid.actualMove(e.getKeyCode());
        super.keyPressed(e);
    }
}
