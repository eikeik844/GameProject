package main;

import Tiles.TileManager;
import entity.BlueFish;
import entity.Enemy;
import entity.GreenFish;
import entity.Items;
import entity.PinkFish;
import entity.Player;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    private static final long serialVersionUID = 1L;

    // Screen Settings
    public final int tileSize = 64; // 64 x 64

    public final int maxScreenCol = 18; 
    public final int maxScreenRow = 12; 

    public final int screenWidth = tileSize * maxScreenCol;  //1152 px
    public final int screenHeight = tileSize * maxScreenRow; //704 px
        
    int FPS = 60;

    //System
    public TileManager tileM = new TileManager(this);
    KeyHandler KeyH = new KeyHandler(this);
    Thread gameThread;
    public Player player = new Player(this ,KeyH);
    public ArrayList<Items> items = new ArrayList<>();
    public UI ui = new UI(this);
    public ArrayList<Enemy> enemies = new ArrayList<>();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Sound sound = new Sound();
    
    // Timer
    public long startTime;
    long elapsedTime; 
    boolean gamePaused = false; //Flag for waiting respond from User
    
    int currentLevel = 0;
    int maxLevel = 3;
    boolean gamewin = false;

    // Game states
    public int gameState;
    public final int titleState = 0; 
    public final int detailsState = 3;
    public final int playState = 1;
    public final int winState = 2;
    public final int loseState = 4;
    //
    
    //Screen shake variables
    public int shakeTimer = 0;
    public int shakeStrength = 5; //The strength of shaking (pixels)
    
    
    public GamePanel() {
    	sound.loop(5);
    	
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        
//		it's can also draw the background here because super.componant(g) will use it
//      this.setBackground(Color.BLACK);
        
        this.setDoubleBuffered(true); 
        this.addKeyListener(KeyH);
        this.setFocusable(true);
        
        gameState = titleState; 
                
        //Anonymous Class
        this.addMouseListener(new MouseAdapter() {
            @Override
            //For pausing state
            public void mouseClicked(MouseEvent e) {
                if (gamePaused && gameState == playState) {
                    resetLevel(); 
                }
            }
        });
    }
    
    public void resetLevel() {
        if(gamewin) return;
        
        if(currentLevel >= maxLevel) {
        	gamewin = true;
        	gameState = winState;
        	return;
        }

        currentLevel++;
        tileM.generateRandomMap(currentLevel);
        player.setDefaultValues();
        
        enemies.clear();
        
        if(currentLevel >= 1) {
        	enemies.add(new BlueFish(this));
        } 
        if(currentLevel >= 2) {
        	enemies.add(new PinkFish(this));
        }
        if (currentLevel >= 3) {
        	enemies.add(new GreenFish(this));
        }
     
        for (Enemy e : enemies) {
            e.setDefaultLocation();
        }
        
        items.clear();
        tileM.generateRandomCoins(10);
        tileM.generateRandomItems(3);

        startTime = System.currentTimeMillis();
        elapsedTime = 0;
        gamePaused = false;

    }
    
	public void startScreenShake(int duration) {
        shakeTimer = duration; //The unit is in frames
    }
	
    
    //Beginning of Thread
    public void startGameThread() {
        gameThread = new Thread(this);
        startTime = System.currentTimeMillis(); 
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS; 
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            if (gameState == playState) {           	
                if (!gamePaused) {
                    update();
                    repaint();
                }else {
                    repaint(); 
                    
                }
            } else {
                repaint(); // Title/Win state →  repaint
            }

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000; 

                if (remainingTime < 0) remainingTime = 0;

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (gamewin) return;

        elapsedTime = System.currentTimeMillis() - startTime;
    	System.out.println(gameState);

        if (elapsedTime >= 25 * 1000) {

            if (player.coinCount >= player.requiredCoins) {
                gamePaused = true;
                if (currentLevel >= maxLevel) {
                    gamewin = true;
                    gameState = winState;
                    sound.stop(4);
                    sound.play(0);
                    sound.loop(5);
                }

            } else {
                gameState = loseState;
                System.out.println("GameLose");
                sound.stop(4);
                sound.play(1);
                sound.loop(5);
            }

            return;
        }

        player.update();
        for (Enemy e : enemies) {
            e.update(player.x, player.y, enemies);
        }

        if (player.life <= 0) {
            gameState = loseState;
            sound.stop(4);
            sound.play(1);
            sound.loop(5);
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Casting to Graphics2D for using various methods
        Graphics2D g2 = (Graphics2D) g;
        
        //If screen shake is active,randomly offset the image
        if (shakeTimer > 0) {
            int offsetX = (int) (Math.random() * shakeStrength * 2 - shakeStrength);
            int offsetY = (int) (Math.random() * shakeStrength * 2 - shakeStrength);
            g2.translate(offsetX, offsetY);
            shakeTimer--;
        }
        
        if(gameState == playState) {
        	
        	sound.stop(5);
        	sound.loop(4);
        	
        	//Draw Tiles
	        tileM.draw(g2);
	        
	        for(Items i : items) {
	        	i.draw(g2);
	        }
	        
	        //Draw the character
	        player.draw(g2);
	        
	        for(Enemy e : enemies) {
	        	e.draw(g2);
	        }
	        
        }
        //Draw interface
        ui.draw(g2);
        
        //Released resource ,Decrease Memory Leak event
        g2.dispose();
    }
    
}
