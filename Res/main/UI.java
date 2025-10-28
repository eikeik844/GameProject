package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class UI {
    
	//Heart Images
	public BufferedImage heart_Full ,heart_Half ,heart_Empty;
	
    GamePanel gp;
    Graphics2D g2;
    Font maruMonica;
    
    private ArrayList<FloatingText> floatingTexts = new ArrayList<>();

    public int commandNum = 0;

    public UI(GamePanel gp) {
        this.gp = gp;
        
        //Download Font
        try {
            InputStream is = getClass().getResourceAsStream("/Font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            maruMonica = new Font("SansSerif", Font.PLAIN, 20); // fallback
        }
        
        //Download Heart
        try {
        	
        	heart_Full = ImageIO.read(getClass().getResourceAsStream("/Items/Heart_32_max.png"));
        	heart_Half = ImageIO.read(getClass().getResourceAsStream("/Items/Heart_32_half.png"));
        	heart_Empty = ImageIO.read(getClass().getResourceAsStream("/Items/Heart_32_dying.png"));
        	
        }catch (IOException e) {
        	e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
    	//For using another method
        this.g2 = g2; 
        
        this.g2.setFont(maruMonica);

        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        
        if (gp.gameState == gp.detailsState) {
            drawDetailsScreen();
        }
        
        if (gp.gameState == gp.playState) {
        	
            drawPlayScreen();
            //Draw heart icons
            drawPlayerLife();
            if (gp.gamePaused) {
                drawPauseScreen();
            }
        }
        
        if(gp.gameState == gp.loseState) {
        	drawLoseScreen();
        }
        
        if (gp.gameState == gp.winState) {
            drawWinScreen();
        }
        
        drawFloatingTexts(g2);
     
    }

	private void drawLoseScreen() {
		// Win screen
    	g2.setColor(Color.BLACK);
    	g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
    	
        g2.setColor(Color.WHITE);
        g2.setFont(maruMonica.deriveFont(Font.BOLD,136f));
        String winMsg = "YOU Lose T-T";
        drawTextCentered(winMsg, 170);
        
        //Menu
        g2.setColor(Color.WHITE);
        g2.setFont(maruMonica.deriveFont(Font.BOLD,60F));
        String MenuMsg1 = "New Game";
        drawTextCentered(MenuMsg1,gp.screenHeight / 2);
        if(commandNum == 0) {
        	g2.drawString(">" ,gp.screenWidth / 2 - 175 ,gp.screenHeight / 2);
        }
        
        g2.setColor(Color.WHITE);
        g2.setFont(maruMonica.deriveFont(Font.BOLD,60F));
        String MenuMsg2 = "EXIT";
        drawTextCentered(MenuMsg2, gp.screenHeight / 2 + 90);
        if(commandNum == 1) {
        	g2.drawString(">" ,gp.screenWidth / 2 - 85 ,gp.screenHeight / 2 + 90);
        }
		
	}

	private void drawTitleScreen() {
        // Title Screen
    	g2.setColor(Color.BLACK);
    	g2.fillRect(0, 0, gp.screenWidth,gp.screenHeight);
    	
        g2.setColor(Color.CYAN);
        g2.setFont(maruMonica.deriveFont(Font.BOLD, 136f));

        String title = "MY 2D GAME ";
        drawTextCentered(title, 170);
        
        //Shadow
        g2.setColor(Color.WHITE);
        drawTextCentered(title, 170, 5, 5);
        
        //Draw the character
        g2.drawImage(gp.player.standing, gp.screenWidth / 2 - 90, gp.screenHeight /2 - 100 
        		,gp.tileSize*3 ,gp.tileSize*3 ,null);
        
        g2.setFont(maruMonica.deriveFont(64f));
        String startMsg = "Press ENTER to Start";
        drawTextCentered(startMsg, gp.screenHeight / 2 + 200);
    }
	
    private void drawDetailsScreen() {
    	// Details Screen
    	 g2.setColor(Color.BLACK);
    	 g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

    	 g2.setColor(Color.WHITE);
    	 g2.setFont(maruMonica.deriveFont(Font.BOLD, 64f));
    	 drawTextCentered("HOW TO PLAY", 150);

    	 g2.setFont(maruMonica.deriveFont(32f));
    	 drawTextCentered("Use W A S D or Arrow keys to move", 240);
    	 drawTextCentered("Avoid these fish and collect coins within 25 seconds", 300);
    	 
    	 try {
    	 //Draw Blue fish
    	 BufferedImage bluefish = ImageIO.read(getClass().getResourceAsStream("/Fishes/BlueFish_Standing.gif"));
    	 g2.drawString("Normal Fish", 220, 370);
    	 g2.drawImage(bluefish ,220 ,380 ,gp.tileSize*2 ,gp.tileSize*2 ,null);
    	 g2.drawString("Dealt half HP damage", 170, 530);
    	 
    	 //Draw Pink fish
    	 BufferedImage Pinkfish = ImageIO.read(getClass().getResourceAsStream("/Fishes/PinkFish_Standing.gif"));
    	 g2.drawString("Strong Fish", 515, 370);
    	 g2.drawImage(Pinkfish ,510 ,380 ,gp.tileSize*2 ,gp.tileSize*2 ,null);
    	 g2.drawString("Dealt One HP damage", 465, 530);
    	 
    	 //Draw Green fish
    	 BufferedImage Greenfish = ImageIO.read(getClass().getResourceAsStream("/Fishes/GreenFish_Standing.gif"));
    	 g2.drawString("Shooter Fish", 805, 370);
    	 g2.drawImage(Greenfish ,815 ,380 ,gp.tileSize*2 ,gp.tileSize*2 ,null);
    	 g2.drawString("Dealt half HP damage", 765, 530);
    	 g2.drawString("from distance" , 805 ,570);
    	 
    	 }catch (IOException e ){
    		 e.printStackTrace();
    	 }
    	 
    	 drawTextCentered("Press ENTER to Start!", 620);
		
	}
    
    private void drawPlayScreen() {
        // HUD (Time and Level)
        long seconds = gp.elapsedTime / 1000;
        g2.setColor(Color.YELLOW);
        g2.setFont(maruMonica.deriveFont(32f));
        g2.drawString("Time: " + seconds + "s", 10, 32);
        g2.drawString("Level: " + gp.currentLevel, gp.screenWidth - 90, 32);
        
        g2.setColor(Color.YELLOW);
        g2.setFont(maruMonica.deriveFont(32f));
        g2.drawString("Coins: " + gp.player.coinCount + "/" + gp.player.requiredCoins, 10, 70);
        
        // Water Speed Down message
        if (gp.player.Onwater) {
            g2.setColor(Color.CYAN);
            g2.setFont(maruMonica.deriveFont(32f));
            String text = "Speed Down!";
            drawTextCentered(text, 70);
        }
    }

    private void drawPauseScreen() {
        // Pause Screen Overlay
        g2.setColor(new Color(0, 0, 0, 150)); 
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setColor(Color.RED);
        g2.setFont(maruMonica.deriveFont(36f));
        String pauseMsg = "25 seconds passed! Do you want to continue?";
        drawTextCentered(pauseMsg, gp.screenHeight / 2);

        String clickMsg = "Click anywhere to continue...";
        drawTextCentered(clickMsg, (gp.screenHeight / 2) + 40);
    }

    private void drawWinScreen() {
        // Win screen
    	g2.setColor(Color.BLACK);
    	g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
    	
        g2.setColor(Color.WHITE);
        g2.setFont(maruMonica.deriveFont(Font.BOLD,136f));
        String winMsg = "YOU WIN!!";
        drawTextCentered(winMsg, 170);
        
        //Menu
        g2.setColor(Color.WHITE);
        g2.setFont(maruMonica.deriveFont(Font.BOLD,60F));
        String MenuMsg1 = "New Game";
        drawTextCentered(MenuMsg1,gp.screenHeight / 2);
        if(commandNum == 0) {
        	g2.drawString(">" ,gp.screenWidth / 2 - 175 ,gp.screenHeight / 2);
        }
        
        g2.setColor(Color.WHITE);
        g2.setFont(maruMonica.deriveFont(Font.BOLD,60F));
        String MenuMsg2 = "EXIT";
        drawTextCentered(MenuMsg2, gp.screenHeight / 2 + 90);
        if(commandNum == 1) {
        	g2.drawString(">" ,gp.screenWidth / 2 - 85 ,gp.screenHeight / 2 + 90);
        }
        
    }
    
    //Draw heart icons
    private void drawPlayerLife() {
        int x = 120; 
        int y = 5;
        int i = 0;

        while (i < gp.player.maxLife / 2) {
            g2.drawImage(heart_Empty, x, y, 32, 32, null);
            i++;
            x += 36;
        }
       
        x = 120;
        int life = gp.player.life;

        while (life > 0) {
            if (life >= 2) {
                g2.drawImage(heart_Full, x, y, 32, 32, null);
                life -= 2;
            } else {
                g2.drawImage(heart_Half, x, y, 32, 32, null);
                life -= 1;
            }
            x += 36;
        }
    }

    
    // Helper method: Draw the text in the centered Screen
    public void drawTextCentered(String text, int y) {
        int x = (gp.screenWidth - g2.getFontMetrics().stringWidth(text)) / 2;
        g2.drawString(text, x, y);
    }
    
    // Helper method: Draw the text in the centered Screen with offset
    public void drawTextCentered(String text, int y, int offsetX, int offsetY) {
        int x = (gp.screenWidth - g2.getFontMetrics().stringWidth(text)) / 2 + offsetX;
        g2.drawString(text, x, y + offsetY);
    }
    
    public void showFloatingText(String text, float x, float y, Color color) {
        floatingTexts.add(new FloatingText(text, x, y, color));
    }
    
    public void drawFloatingTexts(Graphics2D g2) {
        Iterator<FloatingText> it = floatingTexts.iterator();
        while (it.hasNext()) {
            FloatingText ft = it.next();
            g2.setFont(maruMonica.deriveFont(Font.BOLD, 28f));
            g2.setColor(new Color(ft.color.getRed(), ft.color.getGreen(), ft.color.getBlue(), (int)(ft.alpha * 255)));
            g2.drawString(ft.text, ft.x, ft.y);
            
            if (ft.update()) {
                it.remove(); 
            }
        }
    }

}

class FloatingText {
    String text;
    float x, y;
    Color color;
    float alpha = 1.0f; 
    int lifetime = 60;  //1 second

    public FloatingText(String text, float x, float y, Color color) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public boolean update() {
        y -= 0.5f;      //Floating
        alpha -= 0.02f; // fading
        lifetime--;
        return (alpha <= 0 || lifetime <= 0);
    }
}
