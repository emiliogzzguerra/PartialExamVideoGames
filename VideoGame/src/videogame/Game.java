/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videogame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author antoniomejorado
 */
public class Game implements Runnable {
    private BufferStrategy bs;      // to have several buffers when displaying
    private Graphics g;             // to paint objects
    private Display display;        // to display in the game
    String title;                   // title of the window
    private int width;              // width of the window
    private int height;             // height of the window
    private Thread thread;          // thread to create the game
    private boolean running;        // to set the game
    private boolean gameOver;        // to finish the game
    private Player player;          // to use a player
    private ArrayList<Enemy> enemies; // Array list of enemies
    private ArrayList<Bullet> bullets; // Array list of enemies
    private KeyManager keyManager;  // to manage the keyboard
    private Lives lives[]; // lives of the player
    private int numberOfChances; // Amount of lives the user is going to get
    private Random generator; 
    private SoundManager soundManager; 
    private boolean restore;
    private boolean paused;
    private int numberOfEnemies;
    private int points;
    
    
    /**
     * to create title, width and height and set the game is still not running
     * @param title to set the title of the window
     * @param width to set the width of the window
     * @param height  to set the height of the window
     */
    public Game(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.running = false;
        this.gameOver = false;
        this.keyManager = new KeyManager();
        this.player = new Player(0,getHeight()-100,1,100,100, this);
        this.enemies = new ArrayList<Enemy>();
        this.bullets = new ArrayList<Bullet>();
        this.generator = new Random();
        this.numberOfChances = 10;
        this.lives = new Lives[this.numberOfChances];
        this.restore = false;
        this.paused = false;
        this.numberOfEnemies = 6;
        this.points = 0;
    }

    /**
     * To get the width of the game window
     * @return an <code>int</code> value with the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * To get the height of the game window
     * @return an <code>int</code> value with the height
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * initializing the display window of the game
     */
    private void init() {
        soundManager.playSound("cocoloco.wav");
        display = new Display(title, getWidth(), getHeight());  
        Assets.init();
        player = new Player(0, 0, 1, 100, 100, this);
        display.getJframe().addKeyListener(keyManager);
        int xPosition = 800;
        int space = 10;
        for(int i = 0; i<numberOfChances; i++){
            space += 30;
            lives[i] = new Lives(space,10,20,20,true);
        }
        for(int i = 0; i<numberOfEnemies; i++){
            int randHeight = generator.nextInt(300);
            enemies.add(new Enemy(xPosition, 0, 100, randHeight, this));
            enemies.add(new Enemy(xPosition, (randHeight + 200), 100, (getHeight()-randHeight-200), this));
            xPosition = xPosition + 300;
        }
    }
    
    @Override
    public void run() {
        init();
        // frames per second
        int fps = 50;
        // time for each tick in nano segs
        double timeTick = 1000000000 / fps;
        // initializing delta
        double delta = 0;
        // define now to use inside the loop
        long now;
        // initializing last time to the computer time in nanosecs
        long lastTime = System.nanoTime();
        while (numberOfChances > 0 && !gameOver) {
            if(restore){
                enemies = new ArrayList<Enemy>();
                int xPosition = 800;
                for(int i = 0; i<6; i++){
                    int randHeight = generator.nextInt(300);
                    enemies.add(new Enemy(xPosition, 0, 100, randHeight, this));
                    enemies.add(new Enemy(xPosition, (randHeight + 200), 100, (getHeight()-randHeight-200), this));
                    xPosition = xPosition + 300;
                }
                player.setPosition(new Point(0,0));
                restore = false;
                paused = true;
            }
            if(!paused){
                // setting the time now to the actual time
                now = System.nanoTime();
                // acumulating to delta the difference between times in timeTick units
                delta += (now - lastTime) / timeTick;
                // updating the last time
                lastTime = now;
                
                // if delta is positive we tick the game
                if (delta >= 1) {
                    tick();
                    render();
                    delta --;
                }
            } else {
                getKeyManager().tick();
                if(getKeyManager().p || getKeyManager().r){
                    paused = false;
                    delta = 0;
                    lastTime = System.nanoTime();
                }
            }
        } 
        running = false;
        render();
        stop();
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }
    
    private void tick() {
        keyManager.tick();
        // avancing player with colision
        player.tick();
        int iKeepCount = 1;
        int rand = generator.nextInt(3);
        
        for(int i = 0; i<numberOfEnemies; i++){
            Enemy enemy = enemies.get(i);
            if(enemy.getX() <= -100){
                System.out.println(i);
                System.out.println(i+1);
                int randHeight = generator.nextInt(300);
                enemy.setPosition(new Point(800,0));
                enemy.setHeight(randHeight);

                Enemy enemy2 = enemies.get(i+1);
                enemy2.setPosition(new Point(800, (randHeight + 200)));
                enemy2.setHeight((getHeight()-randHeight-200));
            }
            if(iKeepCount < 3){
                enemy.tick(rand);
                iKeepCount++;
            } else {
                rand = generator.nextInt(3);
                iKeepCount = 1;
                enemy.tick(rand);
            }
            if(player.intersects(enemy) || player.hasCrashedAgainstBottom()){
                soundManager.playSound("puenteDeFlores.wav");
                numberOfChances --;
                lives[numberOfChances].dissapear();
                restore = true;
                if(numberOfChances == -1){
                    gameOver = true;
                }
            }
        }      
    }
    
    private void render() {
        // get the buffer strategy from the display
        bs = display.getCanvas().getBufferStrategy();
        /* if it is null, we define one with 3 buffers to display images of
        the game, if not null, then we display every image of the game but
        after clearing the Rectanlge, getting the graphic object from the 
        buffer strategy element. 
        show the graphic and dispose it to the trash system
        */
        if (bs == null) {
            display.getCanvas().createBufferStrategy(3);
        }
        else
        {
            g = bs.getDrawGraphics();
            g.drawImage(Assets.background, 0, 0, width, height, null);
            player.render(g);
            for(int i = 0; i<numberOfChances; i++){
                lives[i].render(g);
            }
            Iterator itr = enemies.iterator();
            while(itr.hasNext()){
                ((Enemy) itr.next()).render(g);
            }
            bs.show();
            g.dispose();
        }
       
    }
    
    /**
     * setting the thread for the game
     */
    public synchronized void start() {
        if (!running) {
            running = true;
            thread = new Thread(this);
            thread.start();
        }
    }
    
    /**
     * stopping the thread
     */
    public synchronized void stop() {
        if (running) {
            running = false;
            try {
                thread.join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }           
        }
    }
    
    // Credits of this function to Tareq Salaheldeen
    // https://stackoverflow.com/questions/20798391/java-isprime-function
    private static boolean isPrime(int num) {
        if (num < 2) return false;
        if (num == 2) return true;
        if (num % 2 == 0) return false;
        for (int i = 3; i * i <= num; i += 2)
            if (num % i == 0) return false;
        return true;
    }
    
}
