package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player {
	
	public int x ,y;
	public int speed;
	public int maxLife = 6;
	public int life = maxLife;
	
	//Speed boost system
	public boolean speedBoosted = false;
	public int speedBoostTimer = 0;
	public int normalSpeed = 3; //Default speed
	public int boostSpeed = 6;  //Boost Speed
	
	//For finding the last frame rate
	public double lastDeltaX;
	public double lastDeltaY;

	public BufferedImage standing, up1, up2, down1, down2, left1, left2, right1, right2;
	public String direction;
	
	public int spriteCounter = 0;
	public int spriteNum = 1;
	
	public int coinCount = 0;
	public int requiredCoins = 10;
	
	//On water state
	public boolean Onwater;
	
	GamePanel gp;
	KeyHandler keyH;
	
	public int invincibleCounter = 0;
	public boolean invincible = false;
	
	
	public Player(GamePanel gp ,KeyHandler keyH) {
		this.gp = gp;
		this.keyH = keyH;
		setDefaultValues();
		getPlayerImage();

	}
	
	public void setDefaultValues() {
		x = 0;
		y = 0;
		speed = normalSpeed;
		direction = "right";
		coinCount = 0;
	}
	
	private void getPlayerImage() {
		try {
			this.standing = ImageIO.read(getClass().getResourceAsStream("/Character/Character_Standing.gif"));
			this.up1 = ImageIO.read(getClass().getResourceAsStream("/Character/Character_Up1.gif"));
			this.up2 = ImageIO.read(getClass().getResourceAsStream("/Character/Character_Up2.gif"));
			this.down1 = ImageIO.read(getClass().getResourceAsStream("/Character/Character_Down1.gif"));
			this.down2 = ImageIO.read(getClass().getResourceAsStream("/Character/Character_Down2.gif"));
			this.left1 = ImageIO.read(getClass().getResourceAsStream("/Character/Character_Left1.gif"));
			this.left2 = ImageIO.read(getClass().getResourceAsStream("/Character/Character_Left2.gif"));
			this.right1 = ImageIO.read(getClass().getResourceAsStream("/Character/Character_Right1.gif"));
			this.right2 = ImageIO.read(getClass().getResourceAsStream("/Character/Character_Right2.gif"));
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		double oldX = x; 
		double oldY = y;

		 //Find the position's character based on the character's center point
        int playerCol = (x + gp.tileSize / 2) / gp.tileSize;
        int playerRow = (y + gp.tileSize / 2) / gp.tileSize;

        int tileNum = gp.tileM.mapTileNum[playerCol][playerRow];

        if (tileNum == 1) {
            if(!speedBoosted) {
            	speed = 1;
            }
            Onwater = true;
        } else {
        	if(!speedBoosted) {
            	speed = normalSpeed;
            }
        	Onwater = false;
        }
        
        //For moving the character
        if (keyH.UpPressed || keyH.DownPressed || keyH.LeftPressed || keyH.RightPressed) {
            
            if (keyH.UpPressed) {
                direction = "up";
                boolean collision = gp.cChecker.checkTileCollision(x, y, speed, direction);
                if (!collision) {
                    y -= speed;
                }
            }
            if (keyH.DownPressed) {
                direction = "down";
                boolean collision = gp.cChecker.checkTileCollision(x, y, speed, direction);
                if (!collision) {
                    y += speed;
                }
            }
            if (keyH.LeftPressed) {
                direction = "left";
                boolean collision = gp.cChecker.checkTileCollision(x, y, speed, direction);
                if (!collision) {
                    x -= speed;
                }
            }
            if (keyH.RightPressed) {
                direction = "right";
                boolean collision = gp.cChecker.checkTileCollision(x, y, speed, direction);
                if (!collision) {
                    x += speed;
                }
            }

            // Animation 
            spriteCounter++;
            if (spriteCounter > 20) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
            
        }
        
        //Preventing The character from going over the edge of the map   
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > gp.screenWidth - gp.tileSize) x = gp.screenWidth - gp.tileSize;
        if (y > gp.screenHeight - gp.tileSize) y = gp.screenHeight - gp.tileSize;
        
        lastDeltaX = x - oldX;
        lastDeltaY = y - oldY;
        
        //Counter time for invincible state
        if(invincibleCounter > 0) {
        	invincibleCounter--;
        	if(invincibleCounter <= 0) {
        		invincible = false;
        	}
        }
        
        if(speedBoosted) {
        	speedBoostTimer--;
        	if(speedBoostTimer <= 0) {
        		speedBoosted = false;
        		if(Onwater) {
        			speed = 1;
        		}else {
        			speed = normalSpeed;
        		}
        	}
        }
        
        gp.cChecker.checkEnemyCollision(this);
        gp.cChecker.checkItemPickup(this);
	}
	
	public void takeDamage(int amount) {
		if(!invincible) {
			life -= amount;
			if(life < 0 ) {
				life = 0;
			}
			invincible = true;
			invincibleCounter = 60; //Immune for 1 second
			
			gp.sound.play(2);
			gp.startScreenShake(15);
			
			//Boost speed for 1 second
			speed = boostSpeed;
			speedBoosted = true;
			speedBoostTimer = 60;
		}
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
	    if (invincible) {
	        g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.5f));
	    }
	    g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
	    g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1f));
	}
	
}
