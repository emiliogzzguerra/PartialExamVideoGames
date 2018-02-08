/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videogame;

import java.awt.image.BufferedImage;

/**
 *
 * @author antoniomejorado
 */
public class Assets {
    public static BufferedImage background; // to store background image
    public static BufferedImage player;     // to store the player image
    public static BufferedImage enemy;     // to store the enemy image
    public static BufferedImage live; // to store the player image
    public static BufferedImage sprites;     // to store the enemy image
    public static BufferedImage[] walking; // bear walking
    
    /**
     * initializing the images of the game
     */
    public static void init() {
        background = ImageLoader.loadImage("/assets/bg1.png");
        player = ImageLoader.loadImage("/assets/guitarra.png");
        enemy = ImageLoader.loadImage("/assets/hector.png");
        sprites = ImageLoader.loadImage("/assets/sprite.png");
        SpriteSheet spritesheet = new SpriteSheet(sprites);
        walking = new BufferedImage[6];
        int j = 0;
        for(int i = 2; i<8; i++){
            walking[j] = spritesheet.crop(i*255,260,255,255);
            j++;
        }
        live = ImageLoader.loadImage("/assets/bone.png");
    }
}
