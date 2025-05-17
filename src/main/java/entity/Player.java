package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
 // Make sure this is your ControllerInput class's package

public class Player extends entity {
    GamePanel gp;
    KeyHandler keyH;
    ControllerInput controllerInput;

    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        this.controllerInput = new ControllerInput(); // Initialize controller input

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/res/player/car3.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/res/player/car3.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/res/player/car5.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/res/player/car5.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/res/player/car2.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/res/player/car2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/res/player/car1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/res/player/car1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        // Poll controller input
        controllerInput.pollInput();
        float xAxis = 0f;
        float yAxis = 0f;

        // For Y axis, you might want to add similar logic inside ControllerInput if you haven't yet
        // For now, let's assume you have getYAxis() implemented
        if (controllerInput != null) {
            xAxis = controllerInput.getXAxis();
            // Assuming you have this method implemented in ControllerInput:
            // yAxis = controllerInput.getYAxis();
        }

        boolean moved = false;

        // Controller input - horizontal movement
        if (xAxis < -0.2f) {
            direction = "left";
            worldX -= speed;
            moved = true;
        } else if (xAxis > 0.2f) {
            direction = "right";
            worldX += speed;
            moved = true;
        }

        // Controller input - vertical movement
        // If you don't have Y axis in ControllerInput yet, comment this part out or implement it
        /*
        if (yAxis < -0.2f) {
            direction = "up";
            worldY -= speed;
            moved = true;
        } else if (yAxis > 0.2f) {
            direction = "down";
            worldY += speed;
            moved = true;
        }
        */

        // Keyboard input fallback or combine with controller
        if (!moved) { // Only move by keyboard if controller not moving player
            if (keyH.upPressed) {
                direction = "up";
                worldY -= speed;
            } else if (keyH.downPressed) {
                direction = "down";
                worldY += speed;
            } else if (keyH.leftPressed) {
                direction = "left";
                worldX -= speed;
            } else if (keyH.rightPressed) {
                direction = "right";
                worldX += speed;
            }
        }

        // Animate sprite if moving
        if (moved || keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up":
                image = (spriteNum == 1) ? up1 : up2;
                break;
            case "down":
                image = (spriteNum == 1) ? down1 : down2;
                break;
            case "left":
                image = (spriteNum == 1) ? left1 : left2;
                break;
            case "right":
                image = (spriteNum == 1) ? right1 : right2;
                break;
        }
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}