package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
	public boolean UpPressed ,DownPressed ,LeftPressed ,RightPressed;
	GamePanel gp;
	
	public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		//Title State
		if (gp.gameState == gp.titleState) {
            if (code == KeyEvent.VK_ENTER) {
            	gp.gameState = gp.detailsState;
            }
        }
		//Details state
		else if (gp.gameState == gp.detailsState) {
            if (code == KeyEvent.VK_ENTER) {
                gp.gameState = gp.playState;
                gp.currentLevel = 0;
                gp.resetLevel();
        		gp.startTime = System.currentTimeMillis();
        		gp.elapsedTime = 0;
        		gp.gamePaused = false;
            }
        }
		
		//WinState
		if(gp.gameState == gp.winState || gp.gameState == gp.loseState) {
			if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
				gp.ui.commandNum--;
				if(gp.ui.commandNum < 0) {
					gp.ui.commandNum = 1;
				}
			}
			if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
				gp.ui.commandNum++;
				if(gp.ui.commandNum > 1) {
					gp.ui.commandNum = 0;
				}
			}
			
			if(code == KeyEvent.VK_ENTER ) {
				if(gp.ui.commandNum == 0) {
					gp.gamewin = false;
					gp.currentLevel = 0;
					gp.player.life = gp.player.maxLife;
					gp.enemies.clear();
					gp.items.clear();
					gp.sound.stop(4);
					gp.sound.loop(5);
					gp.gameState = gp.titleState;
					gp.gamePaused = false;
					
					gp.startTime = System.currentTimeMillis();
					gp.elapsedTime = 0;
					
				}
				if(gp.ui.commandNum == 1) {
					System.exit(0);
				}
			}
		}
		
		
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			UpPressed = true;
		}
		if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			LeftPressed = true;
		}
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			DownPressed = true;
		}
		if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			RightPressed = true;
		}
	}
	@Override
	//Reset Flag
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			UpPressed = false;
		}
		if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			LeftPressed = false;
		}
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			DownPressed = false;
		}
		if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			RightPressed = false;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
}
