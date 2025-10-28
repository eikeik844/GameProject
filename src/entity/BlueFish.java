package entity;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class BlueFish extends Enemy {
    
    public BlueFish(GamePanel gp) {
        super(gp);
        this.speed = 2;
        this.damage = 1;
        this.direction = "left";
        getEnemyImage();
        setDefaultLocation();
    }

    @Override
    public void setDefaultLocation() {
        x = gp.screenWidth - gp.tileSize;
        y = 0;
    }

    @Override
    public void getEnemyImage() {
        try {
            this.standing = ImageIO.read(getClass().getResourceAsStream("/Fishes/BlueFish_Standing.gif"));
            this.up1 = ImageIO.read(getClass().getResourceAsStream("/Fishes/BlueFish_Up1.gif"));
            this.up2 = ImageIO.read(getClass().getResourceAsStream("/Fishes/BlueFish_Up2.gif"));
            this.down1 = ImageIO.read(getClass().getResourceAsStream("/Fishes/BlueFish_Down1.gif"));
            this.down2 = ImageIO.read(getClass().getResourceAsStream("/Fishes/BlueFish_Down2.gif"));
            this.left1 = ImageIO.read(getClass().getResourceAsStream("/Fishes/BlueFish_Left1.gif"));
            this.left2 = ImageIO.read(getClass().getResourceAsStream("/Fishes/BlueFish_Left2.gif"));
            this.right1 = ImageIO.read(getClass().getResourceAsStream("/Fishes/BlueFish_Right1.gif"));
            this.right2 = ImageIO.read(getClass().getResourceAsStream("/Fishes/BlueFish_Right2.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
