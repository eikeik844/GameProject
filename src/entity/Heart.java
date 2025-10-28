package entity;

import java.awt.Color;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Heart extends Items{

	public Heart(GamePanel gp, int x, int y) {
		super(gp, x, y);
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/Items/Hearth_64.png"));
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void onPickup() {
		if(gp.player.life < gp.player.maxLife) {
			gp.player.life += 2;
			gp.ui.showFloatingText("+Life", x, y, Color.PINK);
			if(gp.player.life > gp.player.maxLife) {
				gp.player.life = gp.player.maxLife;
			}
			collected = true;
			gp.sound.play(3);

		}
		
	}
	
}
