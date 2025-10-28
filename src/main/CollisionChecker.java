package main;

import entity.Enemy;
import entity.Items;
import entity.Player;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public boolean checkTileCollision(int worldX, int worldY, int speed, String direction) {

        int tileSize = gp.tileSize;
        int maxCol = gp.maxScreenCol;
        int maxRow = gp.maxScreenRow;
        
        // Increase margin around hitbox
        int margin = 10;

        // The border of hitbox (Retract inward)
        int leftCol = (worldX + margin) / tileSize;
        int rightCol = (worldX + tileSize - 1 - margin) / tileSize;
        int topRow = (worldY + margin) / tileSize;
        int bottomRow = (worldY + tileSize - 1 - margin) / tileSize;

        int tileNum1, tileNum2;

        switch (direction) {
            case "up":
                topRow = (worldY - speed + margin) / tileSize;
                if (topRow >= 0) {
                    tileNum1 = gp.tileM.mapTileNum[
                        Math.max(0, Math.min(maxCol - 1, leftCol))
                    ][
                        Math.max(0, Math.min(maxRow - 1, topRow))
                    ];

                    tileNum2 = gp.tileM.mapTileNum[
                        Math.max(0, Math.min(maxCol - 1, rightCol))
                    ][
                        Math.max(0, Math.min(maxRow - 1, topRow))
                    ];

                    if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision)
                        return true;
                }
                break;

            case "down":
                bottomRow = (worldY + tileSize - 1 - margin + speed) / tileSize;
                if (bottomRow < maxRow) {
                    tileNum1 = gp.tileM.mapTileNum[
                        Math.max(0, Math.min(maxCol - 1, leftCol))
                    ][
                        Math.max(0, Math.min(maxRow - 1, bottomRow))
                    ];

                    tileNum2 = gp.tileM.mapTileNum[
                        Math.max(0, Math.min(maxCol - 1, rightCol))
                    ][
                        Math.max(0, Math.min(maxRow - 1, bottomRow))
                    ];

                    if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision)
                        return true;
                }
                break;

            case "left":
                leftCol = (worldX - speed + margin) / tileSize;
                if (leftCol >= 0) {
                    tileNum1 = gp.tileM.mapTileNum[
                        Math.max(0, Math.min(maxCol - 1, leftCol))
                    ][
                        Math.max(0, Math.min(maxRow - 1, topRow))
                    ];

                    tileNum2 = gp.tileM.mapTileNum[
                        Math.max(0, Math.min(maxCol - 1, leftCol))
                    ][
                        Math.max(0, Math.min(maxRow - 1, bottomRow))
                    ];

                    if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision)
                        return true;
                }
                break;

            case "right":
                rightCol = (worldX + tileSize - 1 - margin + speed) / tileSize;
                if (rightCol < maxCol) {
                    tileNum1 = gp.tileM.mapTileNum[
                        Math.max(0, Math.min(maxCol - 1, rightCol))
                    ][
                        Math.max(0, Math.min(maxRow - 1, topRow))
                    ];

                    tileNum2 = gp.tileM.mapTileNum[
                        Math.max(0, Math.min(maxCol - 1, rightCol))
                    ][
                        Math.max(0, Math.min(maxRow - 1, bottomRow))
                    ];

                    if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision)
                        return true;
                }
                break;
        }

        return false;
    }
    
    //Checking for player and enemy collision
    public void checkEnemyCollision(Player player) {
        // Calculate hitbox of player from the center point
        int playerCenterX = player.x + gp.tileSize / 2;
        int playerCenterY = player.y + gp.tileSize / 2;
        int playerHalfSize = gp.tileSize / 2 - 12; // Shorten 

        int playerLeft = playerCenterX - playerHalfSize;
        int playerRight = playerCenterX + playerHalfSize;
        int playerTop = playerCenterY - playerHalfSize;
        int playerBottom = playerCenterY + playerHalfSize;

        for (Enemy enemy : gp.enemies) {
            // Calculate hitbox of player from the center point
            int enemyCenterX = enemy.x + gp.tileSize / 2;
            int enemyCenterY = enemy.y + gp.tileSize / 2;
            int enemyHalfSize = gp.tileSize / 2 - 12;

            int enemyLeft = enemyCenterX - enemyHalfSize;
            int enemyRight = enemyCenterX + enemyHalfSize;
            int enemyTop = enemyCenterY - enemyHalfSize;
            int enemyBottom = enemyCenterY + enemyHalfSize;

            // AABB Method
            if (playerRight > enemyLeft && playerLeft < enemyRight &&
                playerBottom > enemyTop && playerTop < enemyBottom) {

                player.takeDamage(enemy.damage);
            }
        }
    }

    public void checkItemPickup(Player player) {
    	for(Items item : gp.items) {
    		if(!item.collected) {
    			int px = player.x;
    			int py = player.y;
    			int ix = item.x;
    			int iy = item.y;
    			
    			if(Math.abs(px - ix) < gp.tileSize / 2 && Math.abs(py - iy) < gp.tileSize / 2) {
    				item.onPickup();
    			}
    		}
    	}
    }

}
