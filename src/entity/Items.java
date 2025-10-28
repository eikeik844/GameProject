package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public abstract class Items {
	public int x ,y;
	public boolean collected = false;
	public BufferedImage image;
	protected GamePanel gp;
	
	public Items(GamePanel gp, int x ,int y) {
		this.gp = gp;
		this.x = x;
		this.y = y;
	}
	
	public abstract void onPickup();
	
	public void draw(Graphics2D g2) {
		if(!collected && image != null) {
			g2.drawImage(image, x, y, gp.tileSize ,gp.tileSize ,null);
		}
	}
}
