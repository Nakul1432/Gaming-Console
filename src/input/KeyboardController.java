package src.input;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class KeyboardController {

    private Robot robot;

    public KeyboardController() {
        try {
            robot = new Robot();
            robot.setAutoDelay(0);
            robot.setAutoWaitForIdle(false);

        
            System.out.println(" KeyboardController Initialized");
          

        } catch (Exception e) {
            System.err.println("Failed to initialize Robot.");
            e.printStackTrace();
        }
    }

  
    public void triggerKey(int keyCode, boolean isPress) {
        if (isPress) {
            System.out.println("[KEYBOARD]  Pres: " + keyCode);
            pressKey(keyCode);
        } else {
            System.out.println("[KEYBOARD] Release : " + keyCode);
            releaseKey(keyCode);
        }
    }

 
    public void pressKey(int keyCode) {
        if (robot == null)
            return;

        try {
            robot.keyPress(keyCode);
            System.out.println(
                    "[ROBOT KEY] PRESSED -> " + keyCode
            );
        } catch (Exception e) {
            System.err.println(
                    "Failed pressing key : " + keyCode
            );
        }
    }

   
    public void releaseKey(int keyCode) {
        if (robot == null)
            return;

        try {
            robot.keyRelease(keyCode);
            System.out.println(
                    "[ROBOT KEY] RELEASED -> " + keyCode
            );
        } catch (Exception e) {
            System.err.println(
                    "Failed releasing key : " + keyCode
            );
        }
    }

   
    public void triggerMouseState(int buttonType, boolean isPress) {
        if (robot == null)
            return;

        int mask;
        String buttonName;

        switch (buttonType) {
            case 1:
                mask = InputEvent.BUTTON1_DOWN_MASK;
                buttonName = "LEFT CLICK (FIRE)";
                break;

            case 2:
                mask = InputEvent.BUTTON2_DOWN_MASK;
                buttonName = "MIDDLE CLICK";
                break;

            case 3:
                mask = InputEvent.BUTTON3_DOWN_MASK;
                buttonName = "RIGHT CLICK (SCOPE)";
                break;

            default:
                System.out.println("[MOUSE] Unknown Button : " + buttonType);
                return;
        }

        try {
            if (isPress) {
                robot.mousePress(mask);
                System.out.println(
                        "[MOUSE]  PRESS   " + buttonName
                );
            } else {
                robot.mouseRelease(mask);
                System.out.println(
                        "[MOUSE]  RELEASE -" + buttonName
                );
            }
        } catch (Exception e) {
            System.err.println(
                    "Mouse failed : " + e.getMessage()
            );
        }
    }

    public void moveMouseRelative(int dx, int dy) {
        if (robot == null)
            return;

        try {
           
            Point current = MouseInfo.getPointerInfo().getLocation();

          
            int targetX = current.x + dx;
            int targetY = current.y + dy;

           
            robot.mouseMove(targetX, targetY);

           
            System.out.println(
                    "[TOUCHPAD] "+
                    " -> New Position (" + targetX + "," + targetY + ")"
            );

        } catch (Exception e) {
            System.err.println(
                    "[TOUCHPAD] Mouse Move Failed : " + e.getMessage()
            );
        }
    }

  
    public void leftClick() {
        if (robot == null)
            return;

        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        System.out.println("[MOUSE] LEFT CLICK");
    }

 
    public void rightClick() {
        if (robot == null)
            return;

        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);

        System.out.println("[MOUSE] RIGHT CLICK");
    }

   
    public void middleClick() {
        if (robot == null)
            return;

        robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);

        System.out.println("[MOUSE] MIDDLE CLICK");
    }
}