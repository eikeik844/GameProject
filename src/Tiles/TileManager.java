package Tiles;

import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

import entity.Coin;
import entity.Clock;
import entity.Heart;
import entity.Items;
import entity.Shoe;
import main.GamePanel;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;   // Tile index of Map
    int maxWater = 8; 

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow]; // Create a map according to screen size
        getTileImage();
    }

    public void getTileImage() {
        try {
            tile[0] = new Tile(); // Brick
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/Brick.png"));
            
            tile[1] = new Tile(); // Water
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/Water.png"));
            
            tile[2] = new Tile(); // Prohibited Block
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/Prohibited_Brick.gif"));
            tile[2].collision = true;
            
            
        } catch (IOException e) {
        	System.out.print("Twst");
            e.printStackTrace();
            
        }
    }
    
    // Random Map Method (safe for Player and Fishes) from my brother chatgpt
    public void generateRandomMap(int level) {
        Random rand = new Random();

        // 1.Fill the brick until full
        for (int col = 0; col < gp.maxScreenCol; col++) {
            for (int row = 0; row < gp.maxScreenRow; row++) {
                mapTileNum[col][row] = 0; // Brick
            }
        }

        // 2. Assign the number of obstruction
        maxWater = 16 + (level - 1) * 2;  
        int maxBlock = 20;   //Amount of Block           

        // 3. Find the prohibited block (Around player and the all 3 fishes )
        boolean[][] forbidden = new boolean[gp.maxScreenCol][gp.maxScreenRow];

        // Player begin's position
        int playerCol = 0;
        int playerRow = 0;

        // Mark around player for avoiding water block
        for (int c = playerCol; c <= playerCol + 1; c++) {
            for (int r = playerRow; r <= playerRow + 1; r++) {
                if (c >= 0 && c < gp.maxScreenCol && r >= 0 && r < gp.maxScreenRow)
                    forbidden[c][r] = true;
            }
        }

        // Mark around the position's fishes for avoiding the blocks
        int[][] fishPos = {
            {gp.maxScreenCol - 1, 0},  // Blue Fish
            {0, gp.maxScreenRow - 1},  // Pink Fish
            {gp.maxScreenCol - 1, gp.maxScreenRow - 1}  // Green Fish
        };

        for (int[] pos : fishPos) {
            int fc = pos[0];
            int fr = pos[1];
            for (int c = fc - 1; c <= fc + 1; c++) {
                for (int r = fr - 1; r <= fr + 1; r++) {
                    if (c >= 0 && c < gp.maxScreenCol && r >= 0 && r < gp.maxScreenRow)
                        forbidden[c][r] = true;
                }
            }
        }

        // 4. Random Water
        int currentWater = 0;
        while (currentWater < maxWater) {
            int col = rand.nextInt(gp.maxScreenCol);
            int row = rand.nextInt(gp.maxScreenRow);

            if (!forbidden[col][row] && mapTileNum[col][row] == 0) {
                mapTileNum[col][row] = 1; // Water
                currentWater++;
            }
        }

        // 5. Random Prohibited block (12 blocks)
        int currentBlock = 0;
        while (currentBlock < maxBlock) {
            int col = rand.nextInt(gp.maxScreenCol);
            int row = rand.nextInt(gp.maxScreenRow);

            if (!forbidden[col][row] && mapTileNum[col][row] == 0) {
                mapTileNum[col][row] = 2; // Block
                currentBlock++;
            }
        }
        
        System.out.println(" MAP GENERATED ");
        for (int row = 0; row < gp.maxScreenRow; row++) {
            for (int col = 0; col < gp.maxScreenCol; col++) {
                System.out.print(mapTileNum[col][row]);
            }
            System.out.println();
        }
    }
    
    public void generateRandomItems(int count) {
  		java.util.Random rand = new java.util.Random();
  		int generated = 0;
  		
  		while(generated < count ) {
  			int col = rand.nextInt(gp.maxScreenCol);
  			int row = rand.nextInt(gp.maxScreenRow);
  			
  			int tileNum = gp.tileM.mapTileNum[col][row];
  			
  			if(tileNum == 0) {
  				int x = col * gp.tileSize;
  				int y = row * gp.tileSize;
  				
  				boolean overlap = false;
  				for(Items item : gp.items) {
  					if(Math.abs(item.x - x) < gp.tileSize && Math.abs(item.y - y) < gp.tileSize ) {
  						overlap = true;
  						break;
  					}
  				}
  				
  				if(!overlap ) {
  					int type = generated % 3;
  					switch(type) {
  					case 0 -> gp.items.add(new Heart(gp ,x ,y));
  					case 1 -> gp.items.add(new Clock(gp ,x ,y));
  					case 2 -> gp.items.add(new Shoe(gp ,x ,y));
  					}
  					
  					System.out.println(
  					        "Item placed at col=" + col +
  					        ", row=" + row +
  					        ", tileNum=" + gp.tileM.mapTileNum[col][row]
  					    );
  					generated++;
  				}
  			}
  		}
  	}

    public void generateRandomCoins(int count) {
       Random rand = new Random();
        int generated = 0;

        while (generated < count) {
            int col = rand.nextInt(gp.maxScreenCol);
            int row = rand.nextInt(gp.maxScreenRow);

            int tileNum = mapTileNum[col][row];
            int x = col * gp.tileSize;
            int y = row * gp.tileSize;

            boolean safeTile = (tileNum == 0);

            boolean overlap = false;
            for (Items item : gp.items) {
                if (Math.abs(item.x - x) < gp.tileSize && Math.abs(item.y - y) < gp.tileSize) {
                    overlap = true;
                    break;
                }
            }

            if (safeTile && !overlap) {
                gp.items.add(new Coin(gp, x, y));
                generated++;
            }
        }

        System.out.println(count + " coins generated safely.");
    }
    
    public void draw(Graphics2D g2) {
        for (int col = 0; col < gp.maxScreenCol; col++) {
            for (int row = 0; row < gp.maxScreenRow; row++) {
                int tileIndex = mapTileNum[col][row];
                int x = col * gp.tileSize;
                int y = row * gp.tileSize;

                g2.drawImage(tile[tileIndex].image, x, y, gp.tileSize, gp.tileSize, null);
            }
        }
    }
}
