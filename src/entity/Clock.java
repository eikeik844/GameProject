package entity;

import java.awt.Color;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Clock extends Items{

	public Clock(GamePanel gp, int x, int y) {
		super(gp, x, y);
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/Items/Clock.png"));
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPickup() {
		gp.startTime -= 3000; //increase 3 seconds
		collected = true;
		gp.ui.showFloatingText("+3 Seconds", x, y, Color.CYAN);
		gp.sound.play(3);

	}
	
}
