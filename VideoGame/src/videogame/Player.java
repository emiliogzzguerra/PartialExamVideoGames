/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videogame;

import java.awt.Graphics;

/**
 *
 * @author antoniomejorado
 */
public class Player extends Item{

    private int direction;
    private Game game;
    private Animation animationRight;
    private boolean crashedBottom;
    
    public Player(int x, int y, int direction, int width, int height, Game game) {
        super(x, y, width, height);
        this.direction = direction;
        this.width = width;
        this.height = height;
        this.game = game;
        this.animationRight = new Animation(Assets.walking,100);
        this.crashedBottom = false;
    }

    public int getDirection() {
        return direction;
    }

    @Override
    public void tick() {
        this.animationRight.tick();
        if (game.getKeyManager().space) {
           setY(getY() - 5);
        } else {
           setY(getY()+ 2);
        }
        // reset x position and y position if colision
        if (getX() + getWidth() >= game.getWidth()) {
            setX(game.getWidth() - getWidth());
        }
        else if (getX() <= - getWidth()) {
            crashedBottom = true;
        }
        if (getY() + getHeight() >= game.getHeight()) {
            setY(game.getHeight() - getHeight());
        }
        else if (getY() <= 0) {
            setY(0);
        }
    }
    
    public boolean hasCrashedAgainstBottom(){
        return crashedBottom;
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(animationRight.getCurrentFrame(), getX(), getY(), getWidth(), getHeight(), null);
    }
}
