import acm.program.*;
import acm.graphics.*;
import java.awt.*;
/**
 * CannonBall.java <p>
 * 
 * A class for balls shot from cannon in ZombieDefenseGame. <p>
 */
public class CannonBall extends GCompound implements Runnable {
    // constants
    private static final double DELAY = 20;
    // instance variables
    private ZombieDefenseGame game; //the main game 
    private boolean isAlive = true;
    private double speed; // speed of the ball
    private double size;

    /** the constructor, create the ball */
    public CannonBall(double speed, double size, ZombieDefenseGame game){
        // save the parameters in instance variables
        this.game = game;
        this.speed = speed;
        this.size = size;

        // create the ball centered at the local origin
        GOval ball = new GOval(-size/2, -size/2, size, size);
        add(ball);
        ball.setFilled(true);
        ball.setFillColor(Color.YELLOW);
    }

    /** the run method, to animate the ball */
    public void run() {
        while (isAlive) {
            oneTimeStep();
            pause(DELAY);      
        }   
        explode(); // explode and disappear
    }

    /** stop the animation */
    public void die() {
        isAlive = false;
    }

    // in each time step, move the ball upwards 
    private void oneTimeStep() {
        move(0, -speed); // move the ball
        game.checkCollision(this); // check if hit anything
    }

    // show explosion and disappear
    private void explode() {
        removeAll(); // remove the ball

        // draw an explosion
        GImage explosion = new GImage("bigexplosion.gif");
        explosion.setSize(2*size, 2*size);
        add(explosion, -size, -size);
        pause(500);
        pause(500);
        removeAll();
    }
}
