package src.input;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import src.model.ControllerCommand;

public class InputHandler {

    private final KeyboardController keyboardController;
    private final Map<String, Integer> keyRegistry;

    public InputHandler() {
        keyboardController = new KeyboardController();
        keyRegistry = new HashMap<>();

        initializeMappings();
    }

    private void initializeMappings() {
       
        keyRegistry.put("BTN_UP", KeyEvent.VK_W);
        keyRegistry.put("BTN_DOWN", KeyEvent.VK_S);
        keyRegistry.put("BTN_LEFT", KeyEvent.VK_A);
        keyRegistry.put("BTN_RIGHT", KeyEvent.VK_D);

       
        keyRegistry.put("BTN_JUMP", KeyEvent.VK_SPACE);
        keyRegistry.put("BTN_SPRINT", KeyEvent.VK_SHIFT);
        keyRegistry.put("BTN_CROUCH", KeyEvent.VK_C);
        keyRegistry.put("BTN_PRONE", KeyEvent.VK_Z);
        keyRegistry.put("BTN_RELOAD", KeyEvent.VK_R);
        keyRegistry.put("BTN_INTERACT", KeyEvent.VK_F);

       
        keyRegistry.put("BTN_MAP", KeyEvent.VK_M);
        keyRegistry.put("BTN_MEDKIT", KeyEvent.VK_4);

      
        keyRegistry.put("BTN_WEAPON1", KeyEvent.VK_1);
        keyRegistry.put("BTN_WEAPON2", KeyEvent.VK_2);
        keyRegistry.put("BTN_GRENADE", KeyEvent.VK_G);
    }

    public void processCommand(ControllerCommand command) {
        if (command == null || !command.isValid())
            return;

        String action = command.getCommandAction();

       
        System.out.println("Packet Received : " + action);
        System.out.println("..");

     
        if (action.equals("MOUSE_FIRE")) {
            System.out.println("[INPUT HANDLER] FIRE CLICK");
            keyboardController.triggerMouseState(1, true);
            keyboardController.triggerMouseState(1, false);
            return;
        }

    
        if (action.equals("MOUSE_SCOPE")) {
            System.out.println("[INPUT HANDLER] RIGHT CLICK");
            keyboardController.triggerMouseState(3, true);
            keyboardController.triggerMouseState(3, false);
            return;
        }

     
        if (action.equals("MOUSE_FIRE:DOWN")) {
            keyboardController.triggerMouseState(1, true);
            return;
        }

        if (action.equals("MOUSE_FIRE:UP")) {
            keyboardController.triggerMouseState(1, false);
            return;
        }

        // =====================================================
        // Mouse Hold Scope (State Tracking)
        // =====================================================
        if (action.equals("MOUSE_SCOPE:DOWN")) {
            keyboardController.triggerMouseState(3, true);
            return;
        }

        if (action.equals("MOUSE_SCOPE:UP")) {
            keyboardController.triggerMouseState(3, false);
            return;
        }

      
        if (action.startsWith("MOUSE_MOVE:")) {
            try {
                String[] parts = action.split(":");
                int dx = Integer.parseInt(parts[1]);
                int dy = Integer.parseInt(parts[2]);

               
                keyboardController.moveMouseRelative(dx, dy);

            } catch (Exception e) {
                System.err.println("[INPUT HANDLER] Invalid Mouse Move Packet: " + action);
            }
            return;
        }

      
        boolean isPressed = true;
        boolean instantClick = true;

        if (action.endsWith(":DOWN")) {
            action = action.replace(":DOWN", "");
            isPressed = true;
            instantClick = false;
        } else if (action.endsWith(":UP")) {
            action = action.replace(":UP", "");
            isPressed = false;
            instantClick = false;
        }

        Integer key = keyRegistry.get(action);

        if (key == null) {
            System.out.println("[WARNING] Unknown Command : " + action);
            return;
        }

        System.out.println(
                "[INPUT HANDLER] " +
                action +
                " -> KeyCode : " +
                key
        );

        if (instantClick) {
            keyboardController.triggerKey(key, true);
            try {
                Thread.sleep(30);
            } catch (InterruptedException ignored) {
            }
            keyboardController.triggerKey(key, false);
        } else {
            keyboardController.triggerKey(key, isPressed);
        }
    }
}