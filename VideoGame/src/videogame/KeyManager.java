/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videogame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author antoniomejorado
 */
public class KeyManager implements KeyListener {
    
    public boolean space;   // flag to move right the player
    public boolean p;   // flag to pause the game
    public boolean r;   // flag to resume the game

    private boolean keys[];  // to store all the flags for every key
    
    public KeyManager() {
        keys = new boolean[256];
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // set flag space of if needed
        keys[e.getKeyCode()] = false;
    }
    
    /**
     * to enable or disable moves on every tick
     */
    public void tick() {
        p = keys[KeyEvent.VK_P];
        r = keys[KeyEvent.VK_R];
        space = keys[KeyEvent.VK_SPACE];
    }
}
