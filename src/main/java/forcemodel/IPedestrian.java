package forcemodel;
import javax.vecmath.Vector2d;
import java.util.concurrent.*;

/**
 * pedestrian interface
 * Created by Fatema on 10/21/2016.
 */
public interface IPedestrian extends Callable<IPedestrian>{


    /**
     * returns the current goal position
     * @return goal position
     **/
    Vector2d getGoalposition();

    /**
     * set the goal position
     * @return the object itself
     **/
    IPedestrian setGoalposition( Vector2d p_position );

    /**
     * returns the current position
     * @return current position
     **/
    Vector2d getPosition();

    /**
     * set the current position
     * @return the object itself
     **/
    IPedestrian setPosition( final double p_x, final double p_y );

    /**
     * returns the current velocity
     * @return current velocity
     **/
    Vector2d getVelocity();

    /**
     * returns the current speed
     * @return current speed
     **/
    double getSpeed();

    /**
     * set the current position's X
     * @return the object itself
     **/
    IPedestrian setposX( final double p_posX );

    /**
     * set the current position's Y
     * @return the object itself
     **/
    IPedestrian setposY( final double p_posY );

    /**
     * calculate accelaration
     * @return accelaration
     **/
    Vector2d accelaration();

}
