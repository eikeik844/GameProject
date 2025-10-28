package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List; // Add for using Separation method
import main.GamePanel;

public abstract class Enemy {

    protected GamePanel gp;

    //Default values of each fish
    public int x ,y;
    public int speed;
    public int damage;
    public String direction;

    public BufferedImage standing, up1, up2, down1, down2, left1, left2, right1, right2;
    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Enemy(GamePanel gp) {
        this.gp = gp;
    }

    public abstract void setDefaultLocation();
    public abstract void getEnemyImage(); //For loading the own image

    public boolean tryMove(String testDirection, int offset) {
        int nextX = x;
        int nextY = y;
        
        switch (testDirection) {
            case "up": nextY -= offset; break;
            case "down": nextY += offset; break;
            case "left": nextX -= offset; break;
            case "right": nextX += offset; break;
        }
        
        // Check collision at the new position
        // Use Math.abs(offset) to be the speed for checking collision
        if (!gp.cChecker.checkTileCollision(x, y, Math.abs(offset), testDirection)) {
            // No collision: Apply movement
            x = nextX;
            y = nextY;
            direction = testDirection;
            return true;
        }
        return false;
    }

    //Add an overload for the update method that accepts 
    //all enemies for the purpose of Separation
    public void update(int playerX, int playerY, List<Enemy> allEnemies) {

        // PREDICTIVE MOVEMENT: Anticipate the player's movement slightly
        // Assume the player' speed is around 4 pixels/frame 
        // Predict the player's position 0.3 seconds in advance
        double predictionTime = 0.3;
        double playerVelocityX = gp.player.lastDeltaX * 4; 
        double playerVelocityY = gp.player.lastDeltaY * 4;

        double predictedX = playerX + playerVelocityX * predictionTime;
        double predictedY = playerY + playerVelocityY * predictionTime;
        // ---------------------------------------------------------------------

        double deltaX = predictedX - x;
        double deltaY = predictedY - y;
        
        //Calculate all distance (Vector Math) for finding a constant speed
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        boolean moving = false;
        
        if (distance > 1) { 
            
            // 1. Vector Normalization: Calculate movement steps for X and Y axes
            int moveX = (int)Math.round((deltaX / distance) * speed);
            int moveY = (int)Math.round((deltaY / distance) * speed);
            
            // Flags to track if movement was successful in either axis
            boolean movedX = false;
            boolean movedY = false;

            // 2. Collision Check and Sliding Logic
            
            // a) Check X-axis movement
            if (moveX != 0) {
                String xDirection = moveX > 0 ? "right" : "left";
                if (!gp.cChecker.checkTileCollision(x, y, Math.abs(moveX), xDirection)) {
                    x += moveX;
                    movedX = true;
                }
            }
            
            // b) Check Y-axis movement
            if (moveY != 0) {
                String yDirection = moveY > 0 ? "down" : "up";
                if (!gp.cChecker.checkTileCollision(x, y, Math.abs(moveY), yDirection)) {
                    y += moveY;
                    movedY = true;
                }
            }
            
            moving = movedX || movedY;
            
            // 3. Wall Hugging & Diagonal Escape Logic
            // Check if movement was intended (moveX or moveY is not 0) but failed (not moving).
            // This prevents jittering when close to the player (due to rounding to 0) but not actually blocked.
            boolean triedMoving = (Math.abs(moveX) > 0 || Math.abs(moveY) > 0);

            if (triedMoving && !moving) {

                // --- Step 1: Try diagonal escapes (corner release) ---
                boolean escaped = false;
                // Note: tryMove updates position/direction if successful
                if (tryMove("up", speed) && tryMove("left", speed)) escaped = true;
                else if (tryMove("up", speed) && tryMove("right", speed)) escaped = true;
                else if (tryMove("down", speed) && tryMove("left", speed)) escaped = true;
                else if (tryMove("down", speed) && tryMove("right", speed)) escaped = true;

                // --- Step 2: If still stuck, try perpendicular sliding (wall hug) ---
                if (!escaped) {
                    String dir1, dir2;
                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        dir1 = "up"; dir2 = "down";
                    } else {
                        dir1 = "left"; dir2 = "right";
                    }

                    int offset = speed + 2; // a bit more push
                    if (tryMove(dir1, offset)) escaped = true;
                    else if (tryMove(dir2, offset)) escaped = true;
                }

                // --- Step 3: Final micro-nudge if still cornered ---
                if (!escaped) {
                    String[] tinyDirs = {"up","down","left","right"};
                    for (String dir : tinyDirs) {
                        if (tryMove(dir, 1)) { // move by 1 pixel
                            escaped = true;
                            break;
                        }
                    }
                }

                if (escaped) moving = true;
            }

            
            // 4. Direction Priority (for animation)
            if (moving) {
                if (Math.abs(deltaX) > Math.abs(deltaY) && movedX) {
                    // Priority X only if X actually moved
                    direction = moveX > 0 ? "right" : "left";
                } else if (movedY){
                    // Priority Y (or if X didn't move)
                    direction = moveY > 0 ? "down" : "up";
                } 
                // Note: If 'moving' is true from Wall Hugging (step 3), 
                // the direction is already set by tryMove()
            }
        } 
        // Note: We don't set moving=false here, as Separation logic might still move the enemy.

        // 6. SEPARATION: Prevent enemies from overlapping
        // Reduced force to minimize jittering/oscillation
        double separationForce = 0.1; 
        
        for (Enemy other : allEnemies) {
            if (other != this) {
                double dx = x - other.x;
                double dy = y - other.y;
                double dist = Math.sqrt(dx * dx + dy * dy);
                
                // If the distance is less than a tile size (gp.tileSize)
                if (dist < gp.tileSize && dist > 0) {
                    // Apply small push force away from the 'other' enemy
                    double newX = x + (dx / dist) * separationForce;
                    double newY = y + (dy / dist) * separationForce;
                    
                    // Update integer position, converting back from double
                    x = (int) Math.round(newX);
                    y = (int) Math.round(newY);

                    // Since separation caused movement, update the moving flag
                    moving = true; 
                }
            }
        }

        // Final check to stop movement if the enemy is close to the player AND not moved by Separation
        if (distance <= 1 && !moving) {
             moving = false; 
        }

        // 5. Animation Logic
        if (moving) {
            spriteCounter++;
            if (spriteCounter > 30) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        } else {
            spriteNum = 1; 
        }

        // Prevent going out of screen boundaries
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > gp.screenWidth - gp.tileSize) x = gp.screenWidth - gp.tileSize;
        if (y > gp.screenHeight - gp.tileSize) y = gp.screenHeight - gp.tileSize;
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        
        switch(direction) {
            case "up" :
                if(spriteNum == 1) {
                    image = up1;
                }
                if(spriteNum == 2) {
                    image = up2;
                }
                break;
            case "down" :
                if(spriteNum == 1) {
                    image = down1;
                }
                if(spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left" :
                if(spriteNum == 1) {
                    image = left1;
                }
                if(spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right" :
                if(spriteNum == 1) {
                    image = right1;
                }
                if(spriteNum == 2) {
                    image = right2;
                }
                break;
        }
        
        g2.drawImage(image ,x ,y ,gp.tileSize ,gp.tileSize ,null);
    }
}