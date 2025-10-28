package entity;

import main.GamePanel;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;

public class Coin extends Items {

    public Coin(GamePanel gp, int x, int y) {
        super(gp, x, y);
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Items/Coin.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPickup() {
        collected = true;
        gp.player.coinCount++;
        gp.ui.showFloatingText("+1 Coin!", x, y, Color.YELLOW);
        gp.sound.play(3); 
    }
}
