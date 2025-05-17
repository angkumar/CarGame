package entity;

import net.java.games.input.*;

public class ControllerInput {
    private Controller controller;
    private Component xAxisComponent;
    private Component yAxisComponent;
    private float xAxis;
    private float yAxis;

    private static final float DEADZONE = 0.2f;

    public ControllerInput() {
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        System.out.println("Available controllers:");
        for (Controller c : controllers) {
            System.out.println(" - " + c.getName() + " (" + c.getType() + ")");
            if (controller == null && (c.getType() == Controller.Type.GAMEPAD || c.getType() == Controller.Type.STICK)) {
                controller = c;
            }
        }
        if (controller == null && controllers.length > 0) {
            controller = controllers[0];
            System.out.println("No GAMEPAD/STICK found, defaulting to: " + controller.getName());
        }

        if (controller != null) {
            xAxisComponent = controller.getComponent(Component.Identifier.Axis.X);
            yAxisComponent = controller.getComponent(Component.Identifier.Axis.Y);
            System.out.println("Using controller: " + controller.getName());
        } else {
            System.out.println("No controller found!");
        }
    }

    public void pollInput() {
        if (controller == null) {
            xAxis = 0f;
            yAxis = 0f;
            return;
        }

        try {
            controller.poll();

            if (xAxisComponent != null) {
                float value = xAxisComponent.getPollData();
                xAxis = (Math.abs(value) < DEADZONE) ? 0f : value;
            } else {
                xAxis = 0f;
            }

            if (yAxisComponent != null) {
                float value = yAxisComponent.getPollData();
                yAxis = (Math.abs(value) < DEADZONE) ? 0f : value;
            } else {
                yAxis = 0f;
            }
        } catch (Exception e) {
            System.err.println("Error polling controller: " + e.getMessage());
            xAxis = 0f;
            yAxis = 0f;
        }
    }

    public float getXAxis() {
        return xAxis;
    }

    public float getYAxis() {
        return yAxis;
    }
}