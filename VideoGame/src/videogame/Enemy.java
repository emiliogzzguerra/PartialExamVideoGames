/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videogame;

import java.awt.Graphics;
import java.util.Random;

/**
 *
 * @author antoniomejorado
 */
public class Enemy extends Item{

    private Game game;
    private Random generator; 
    
    public Enemy(int x, int y, int width, int height, Game game) {
        super(x, y, width, height);
        this.width = width;
        this.height = height;
        this.game = game;
        this.generator = new Random();
    }
    
    @Override
    public void tick() {}

    public void tick(int rand) {
        setX(getX() - (3 + rand));
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Assets.enemy, getX(), getY(), getWidth(), getHeight(), null);
    }
}
