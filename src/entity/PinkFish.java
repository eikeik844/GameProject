package entity;


import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class PinkFish extends Enemy {

	public PinkFish(GamePanel gp) {
		super(gp);
        this.speed = 2;
        this.damage = 2;//take 1 heart/hit
        this.direction = "up";
        getEnemyImage();
        setDefaultLocation();
	}

	@Override
	public void setDefaultLocation() {
		x = 0;
		y = gp.screenHeight - gp.tileSize;
	}

	@Override
	public void getEnemyImage() {
		try {
            this.standing = ImageIO.read(getClass().getResourceAsStream("/Fishes/PinkFish_Standing.gif"));
            this.up1 = ImageIO.read(getClass().getResourceAsStream("/Fishes/PinkFish_Up1.gif"));
            this.up2 = ImageIO.read(getClass().getResourceAsStream("/Fishes/PinkFish_Up2.gif"));
            this.down1 = ImageIO.read(getClass().getResourceAsStream("/Fishes/PinkFish_Down1.gif"));
            this.down2 = ImageIO.read(getClass().getResourceAsStream("/Fishes/PinkFish_Down2.gif"));
            this.left1 = ImageIO.read(getClass().getResourceAsStream("/Fishes/PinkFish_Left1.gif"));
            this.left2 = ImageIO.read(getClass().getResourceAsStream("/Fishes/PinkFish_Left2.gif"));
            this.right1 = ImageIO.read(getClass().getResourceAsStream("/Fishes/PinkFish_Right1.gif"));
            this.right2 = ImageIO.read(getClass().getResourceAsStream("/Fishes/PinkFish_Right2.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

}
