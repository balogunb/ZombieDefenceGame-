import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.*;
import java.util.*;
/**
 * ZombieDefenseGame.java <p>
 *
 * Basit Balogun
 * The main class of the game where the graphics are drawn 
 */
public class ZombieDefenseGame extends GraphicsProgram {
    // initial size of the window
    public static int 
    APPLICATION_WIDTH = 800,
    APPLICATION_HEIGHT = 500;

    // constants
    private static final double
    SPEED = 10, // speed of the cannon balls 
    BASE_SIZE = 40,
    CANNON_SIZE = BASE_SIZE * 0.8;

    //instance variables 
    private double width, height;
    //private GOval cannonBase;
    double baseHeight = getHeight()/6;
    GLabel zombiesKilled;
    GLabel gameOverLabel;
    int kills; //gives number of zombies killed 
    //private GPolygon cannon;
    private GImage cannon;
    private boolean gameOver = false;
    public DeadZombie[] zombie = new DeadZombie[10];//an array of 10 zombies
    private boolean cannonReady = true;//allows pacing of bullets 

    /** the run method, draw the inital graphics */
    public void run() {
        drawGraphics();
        addMouseListeners();// tell the program to pay attention to mouse events
    }

    // initially draw the background, draws cannon and labels 
    private void drawGraphics() {
        width = getWidth(); 
        height = getHeight()/6; 
        double distance = 40;
        double zombieSize = 70;
        double speed = -2;

        //draw background lanes
        // for (int i = 0; i < 5; i = i + 2) {
        // GRect rect1 = new GRect(0,i*height,width, height);
        // rect1.setFilled(true);
        // rect1.setFillColor(Color.BLUE);
        // add(rect1);
        // }

        // for (int i = 1; i <= 5; i = i + 2) {
        // GRect rect2 = new GRect(0,i*height,width, height);
        // rect2.setFilled(true);
        // rect2.setFillColor(Color.GREEN);
        // add(rect2);
        // }    

        // create the zombie, centered at the local origin
        GImage background = new GImage("background.gif");
        background.setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
        add(background, 0, 0); 

        //Replaced cannon with image file
        cannon = new GImage("cannon.png");
        cannon.setSize(width/3, -height);
        add(cannon, getWidth()/2 -width/6, getHeight()); 

        /**
        //draw cannon base 
        cannonBase = new GOval(BASE_SIZE,BASE_SIZE);
        cannonBase.setFilled(true);
        cannonBase.setFillColor(Color.RED);
        add(cannonBase, getWidth()/2-BASE_SIZE/2 , 
        getHeight() - BASE_SIZE);

        // draw a GPolygon as the cannon
        createCannon(CANNON_SIZE);
        add(cannon, cannonBase.getX() + BASE_SIZE/2, cannonBase.getY()+BASE_SIZE/2 );
        cannon.setFilled(true);
        cannon.setFillColor(Color.WHITE);
        cannon.setColor(Color.BLACK);

         */

        //draw labels 
        zombiesKilled = new GLabel("Zombies killed: "+ kills);
        zombiesKilled.setFont(new Font("Sanserif", Font.BOLD, 15));
        zombiesKilled.setColor(Color.WHITE);
        add(zombiesKilled, getWidth()- 1.5*zombiesKilled.getWidth(),20);

        gameOverLabel = new GLabel("Game Over!  "+ kills+ " zombies killed. Click to restart");
        gameOverLabel.setFont(new Font("Sanserif", Font.BOLD, 30));
        gameOverLabel.setColor(Color.WHITE);
        add(gameOverLabel, width/2-gameOverLabel.getWidth()/2,getHeight()/2);
        gameOverLabel.setVisible(false);

        //draw zombies with arrays using zombie class
        for (int i = 0; i < 10 ; i++) {
            zombie[i] = new DeadZombie(zombieSize,speed,
                this);
            // add the zombie to first track
            // the exact location depends on the index i
            add(zombie[i], getWidth()/7+i*zombieSize, distance); 
            new Thread(zombie[i]).start(); // animate the zombie 
        } 

    }

    // draw a polygon as the cannon
    // private void createCannon(double size) {
    // cannon = new GPolygon();
    // // add the four corners of the cannon
    // cannon.addVertex(-size/3,0);
    // cannon.addVertex(size/3,0);
    // cannon.addVertex(size/6,-size);
    // cannon.addVertex(-size/6, -size);

    // }

    /** create a bouncy projectile when mouse is pressed */
    public void mousePressed(GPoint point) {
        double ballSpeed = 7;
        double ballSize = 20;
        
        // create a cannon ball when mouse is pressed and there is no ball on the screen
        if(cannonReady && !gameOver ){ 
            CannonBall ball = new CannonBall(ballSpeed,ballSize, this);
            add(ball, point.getX(), getHeight() - getHeight()/6); 
            new Thread(ball).start(); // start the animation
            cannonReady = false;
        }
        
        
        
        if (gameOver) {//if game is over use mouse pressed to restart game
            removeAll();
            kills = 0;
            gameOver = false;
            drawGraphics();
            gameOverLabel.setVisible(false);
            cannonReady = true;
            
        }

        
    }

    /** move the cannon as mouse moves */
    public void mouseMoved(GPoint point) { 
        if (gameOver) return;
        //move cannon and cannonbase  horizontally using the positionn of mouse cursor 
        //cannonBase.setLocation(point.getX()-cannonBase.getWidth()/2, cannonBase.getY());
        cannon.setLocation(point.getX()-cannon.getWidth()/2, cannon.getY());
    }

    /** check if projectile hits zombie */
    public void checkCollision(CannonBall ball) {
        for (int i = 0; i < 10; i++) {

            // check if ball hits zombie[i]
            if( ball.getBounds().intersects(zombie[i].getBounds())) {
                double moveUp = -getHeight()/6;
                ball.die(); //  kill the projectile
                cannonReady = true;
                zombiesKilled.setLabel("Zombies killed: "+ ++kills); //increase score by 1 
                if(zombie[i].getY() > getHeight()/6){
                    zombie[i].move(zombie[i].getSpeed(), moveUp);
                }
                //if ball hits zombie kill ball, set cannonReady to true  and move zombie[i] up and increase zombie kill by 1

            }
        }

        //if ball passed game top boundary set cannonReady to true
        if(ball.getY() < 0) {
            cannonReady = true;
            ball.die();
        }
    }

    /** check if zombie hits cannon */
    public void checkCollision(DeadZombie zombie) {
        double moveDown = getHeight()/6; 
        //check if zombie[i] hits cannon base 
        //if zombie[i] hits cannon setgame of over to true 
        if (zombie.getY() >  5 * moveDown){
            gameOver = true;
            gameOverLabel.setLabel("Game Over!  "+ kills+ " zombies killed. Click to restart");
            gameOverLabel.setVisible(true);

        }

        //make gameOver = true and set gameOver label to visible

        if(cannon.getX() == zombie.getX() && zombie.getY() == -40 + (-5*getHeight())){
            gameOver = true;
            gameOverLabel.setLabel("Game Over!  "+ kills+ " zombies killed. Click to restart");
            gameOverLabel.setVisible(true);
        }

        //check if zombie collides with boundary 
        if (zombie.getX() - zombie.getWidth()/2 < 0){
            zombie.setSpeed(-zombie.getSpeed());
            zombie.setLocation(zombie.getX(),zombie.getY() + moveDown);
            zombie.switchImageRight();

        }

        if (zombie.getX() + zombie.getWidth()/2 > width){
            zombie.setSpeed(-zombie.getSpeed());
            zombie.setLocation(zombie.getX(),zombie.getY() + moveDown);
            zombie.switchImageLeft();

        }

    }

    public boolean checkStatus(){
        return gameOver;
    }
}
