package entity;

import java.awt.Color;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Shoe extends Items{

	public Shoe(GamePanel gp, int x, int y) {
		super(gp, x, y);
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/Items/Shoes.png"));
			
		}catch(IOException  e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPickup() {
		gp.player.speed = gp.player.boostSpeed;
		gp.player.speedBoosted = true;
		gp.player.speedBoostTimer = 120;
		collected = true;
		gp.ui.showFloatingText("+Speed", x, y, Color.YELLOW);
		gp.sound.play(3);
	}

}
