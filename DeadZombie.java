import acm.program.*;
import acm.graphics.*;
import java.awt.*;
/**
 * DeadZombie.java <p>
 * 
 * A class for the zombies inside ZombieDefenseGame. <p>
 */
public class DeadZombie extends GCompound implements Runnable {
    // constants
    private static final double DELAY = 20;

    // instance variables 
    private ZombieDefenseGame game; // the main game
    private boolean gameOver = false;
    private double size, speed;
    GImage zombie;

    /** the constructor, create the bee */
    public DeadZombie(double size, double speed, ZombieDefenseGame game){
        // save the parameters in instance variables
        this.game = game;
        this.size = size;
        this.speed = speed;

        // create the zombie, centered at the local origin
        zombie = new GImage("zombieLeft.gif");
        zombie.setSize(size, size);
        add(zombie, -size/2, -size/2); 

    }

    /** the run method, to animate the zombie */
    public void run() {
        while (gameOver == false) {
            oneTimeStep();
            pause(DELAY);
            game.checkStatus();

        }      
    }

    /** return the current angle of movement */
    public double getSpeed() {
        return speed;
    }

    /** set the angle of the movement */
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    /**changes the image when it changes direction  */
    public void switchImageRight(){
        removeAll();//remove existing image 
        //draw new image 
        zombie = new GImage("zombieRight.gif");
        zombie.setSize(size, size);
        add(zombie, -size/2, -size/2); 
    }
    
    public void switchImageLeft(){
        removeAll();//remove existing image 
        //draw new image 
        zombie = new GImage("zombieLeft.gif");
        zombie.setSize(size, size);
        add(zombie, -size/2, -size/2); 
    }

    // in each time step, move the zombie and change image game boundary is hit 
    private void oneTimeStep(){       
        move(speed, 0);
        game.checkCollision(this);
    }
}
