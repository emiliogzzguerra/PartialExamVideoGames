/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videogame;

import java.awt.Graphics;

/**
 *
 * @author emiliogonzalez
 */
public class Lives extends Item {

    private boolean visible;

    public Lives(int x, int y, int width, int height, boolean visible) {
        super(x, y, width, height);
        this.visible = visible;
    }
    
    @Override
    public void tick() {}

    @Override
    public void render(Graphics g) {
        if(visible){
            g.drawImage(Assets.live, getX(), getY(), getWidth(), getHeight(), null);
        }
    }
    
    public void dissapear(){
        visible = false;
    }
}
