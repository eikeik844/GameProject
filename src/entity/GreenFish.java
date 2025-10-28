package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;

public class GreenFish extends Enemy {
	private ArrayList<Bullet> bullets = new ArrayList<>();
	private ArrayList<ImpactEffect> effects = new ArrayList<>();
	private int shootCooldown = 0;
	
	public GreenFish(GamePanel gp) {
		super(gp);
		 this.speed = 1;
	     this.damage = 1;
	     this.direction = "left";
	     getEnemyImage();
	     setDefaultLocation();
	}

	@Override
	public void setDefaultLocation() {
		x = gp.screenWidth - gp.tileSize;
		y = gp.screenHeight - gp.tileSize;
		
	}


	@Override
	public void getEnemyImage() {
	    try {
            this.standing = ImageIO.read(getClass().getResourceAsStream("/Fishes/GreenFish_Standing.gif"));
            this.up1 = ImageIO.read(getClass().getResourceAsStream("/Fishes/GreenFish_Up1.gif"));
            this.up2 = ImageIO.read(getClass().getResourceAsStream("/Fishes/GreenFish_Up2.gif"));
            this.down1 = ImageIO.read(getClass().getResourceAsStream("/Fishes/GreenFish_Down1.gif"));
            this.down2 = ImageIO.read(getClass().getResourceAsStream("/Fishes/GreenFish_Down2.gif"));
            this.left1 = ImageIO.read(getClass().getResourceAsStream("/Fishes/GreenFish_Left1.gif"));
            this.left2 = ImageIO.read(getClass().getResourceAsStream("/Fishes/GreenFish_Left2.gif"));
            this.right1 = ImageIO.read(getClass().getResourceAsStream("/Fishes/GreenFish_Right1.gif"));
            this.right2 = ImageIO.read(getClass().getResourceAsStream("/Fishes/GreenFish_Right2.gif"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
	
	@Override
	public void update(int playerX, int playerY, java.util.List<Enemy> enemies) {
		super.update(playerX, playerY, enemies);
		
		if(shootCooldown > 0) {
			shootCooldown--;
		}
		
		
		if (shootCooldown == 0) {
		    String dir = this.direction;

		    bullets.add(new Bullet(x + gp.tileSize / 2, y + gp.tileSize / 2, dir));

		    shootCooldown = 90; 
		    
		}
		
		//Update Bullets
		for(Bullet b : bullets) {
			b.update();
		}
		bullets.removeIf(b -> !b.active);
		
		for(ImpactEffect e : effects) {
	    	e.update();
	    }
	    effects.removeIf(e -> e.life <= 0);
	}
	
	@Override
	public void draw(Graphics2D g2) {
	    super.draw(g2);
	    
	    for (Bullet b : bullets) {
	        b.draw(g2);
	    }
	    
	    for(ImpactEffect e : effects) {
	    	e.draw(g2);
	    }
	}

	
	//Inner class Bullet
	private class Bullet {
		private int x,y;
		private int speed = 4;
		private String direction;
		private boolean active = true;
		
		Bullet(int x ,int y, String direction) {
			this.x = x;
			this.y = y;
			this.direction = direction;
		}
		
		void update() {
			switch(direction) {
			case "up" -> y -= speed;
			case "down" -> y += speed;
			case "left" -> x -= speed;
			case "right" -> x += speed;
			}
			
			if(x < 0 || y < 0 || x > gp.screenWidth || y > gp.screenHeight) {
				effects.add(new ImpactEffect(x ,y));
				active = false;
				return;
			}
			
			if(gp.cChecker.checkTileCollision(x, y, speed, direction)) {
				effects.add(new ImpactEffect(x ,y));
				active = false;
				return;
			}
			
			int playerLeft = gp.player.x;
			int playerRight = gp.player.x + gp.tileSize;
			int playerTop = gp.player.y;
			int playerBottom = gp.player.y + gp.tileSize;
			
			if(x > playerLeft && x < playerRight && y > playerTop && y < playerBottom) {
				gp.player.takeDamage(1);
				effects.add(new ImpactEffect(x ,y));
				active = false;
			}
		}
		
		void draw(Graphics2D g2) {
		    if (!active) return;

		    int radius = 10;
		    for (int i = 3; i >= 0; i--) {
		        int size = radius + i * 4;
		        float alpha = 0.15f * (4 - i); 
		        g2.setColor(new Color(0f, 1f, 0f, alpha)); 
		        g2.fillOval(x - size / 2, y - size / 2, size, size);
		    }

		    // Core
		    g2.setColor(new Color(0, 255, 80)); 
		    g2.fillOval(x - 4, y - 4, 8, 8);

		    // Tail effect
		    g2.setColor(new Color(0, 200, 0, 100));
		    switch (direction) {
		        case "up" -> g2.fillRect(x - 2, y + 4, 4, 12);
		        case "down" -> g2.fillRect(x - 2, y - 12, 4, 12);
		        case "left" -> g2.fillRect(x + 4, y - 2, 12, 4);
		        case "right" -> g2.fillRect(x - 12, y - 2, 12, 4);
		    }
		}
		
	}
	
	// Inner class for effect
	private class ImpactEffect {
	    private int x, y;
	    private int life = 10; // 10 frames

	    ImpactEffect(int x, int y) {
	        this.x = x;
	        this.y = y;
	    }

	    void update() {
	        life--;
	    }

	    void draw(Graphics2D g2) {
	        if (life <= 0) return;

	        float alpha = life / 10f; // slowly fading
	        g2.setColor(new Color(0f, 1f, 0f, alpha)); 
	        int size = (int) ((1.5 - alpha) * 20); 
	        g2.fillOval(x - size / 2, y - size / 2, size, size);
	    }
	}

}


