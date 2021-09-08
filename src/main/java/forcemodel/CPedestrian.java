package forcemodel;

import javax.vecmath.Vector2d;
import java.util.ArrayList;
import java.util.Map;


import static forcemodel.CEnvironment.m_pixelpermeter;

/**
 * pedestrian class
 * Created by Fatema on 10/21/2016.
 */
public class CPedestrian implements IPedestrian{

    private static final double m_maxspeedfactor = 2.5;
    private static final double m_maxforce = 3.0;
    private static double m_radius;
    public int m_type;
    public String m_name;
    private Vector2d m_position;
    private Vector2d m_goal;
    private ArrayList<Vector2d> m_goals = new ArrayList<Vector2d>();
    private Vector2d m_velocity ;
    private double m_speed;
    private CEnvironment l_env;
    private double m_maxspeed;
    static Map<String, ArrayList<String>> m_realdata = CCSVFileReaderForGA.readDataFromCSV2();

    public CPedestrian(Vector2d p_start, double p_speed, double p_maxspeed, Vector2d p_goal, CEnvironment p_env, int p_type, String p_name)
    {
        m_goals.add(p_goal);
        m_goal = m_goals.remove(0);
        m_position = p_start;
        m_speed = p_speed;
        m_velocity = CVector.scale( m_speed, CVector.direction( m_goal, m_position ) );
        l_env = p_env;
        m_maxspeed = p_maxspeed;
        m_radius = p_type == 2 ? 1.5* m_pixelpermeter : 0.5* m_pixelpermeter;
        m_type = p_type;
        m_name = p_name;
    }

    @Override
    public Vector2d getGoalposition() {
        return m_goal;
    }

    @Override
    public IPedestrian setGoalposition( final Vector2d p_position )
    {
        this.m_goal = p_position;
        return this;
    }

    @Override
    public Vector2d getPosition() {
        return m_position;
    }

    @Override
    public IPedestrian setPosition( final double p_x, final double p_y ) {
        this.m_position = new Vector2d( p_x, p_y );
        return this;
    }

    @Override
    public Vector2d getVelocity() {
        return m_velocity;
    }

    @Override
    public double getSpeed() {
        return m_speed;
    }


    @Override
    public IPedestrian setposX( final double p_posX ) {
        this.m_position.x = p_posX;
        return this;
    }

    @Override
    public IPedestrian setposY( final double p_posY ) {
        this.m_position.y = p_posY;
        return this;
    }

    @Override
    public Vector2d accelaration()
    {
        //Vector2d l_repulsetoWall = new Vector2d( 0, 0 );
        Vector2d l_repulsetoOthers = new Vector2d( 0, 0 );
        Vector2d l_desiredVelocity = CVector.scale( this.m_maxspeed, CVector.direction( this.getGoalposition(), this.getPosition() ) );

        /*for ( int i = 0; i < l_env.getWallinfo().size(); i++ )
        {
            l_repulsetoWall = CVector.add( l_repulsetoWall, CForce.repulsewall( this, l_env.getWallinfo().get( i ), l_env.test ) );
        }*/


        for ( int i = 0; i < l_env.getPedestrianinfo().size(); i++ )
        {
            if( !l_env.getPedestrianinfo().get(i).equals( this ) )
            {
                l_repulsetoOthers = CVector.add( l_repulsetoOthers, CForce.repulseotherPed( this, l_env.getPedestrianinfo().get( i ) ) );
            }
        }

        final Vector2d l_temp = CVector.add( CForce.drivingForce( l_desiredVelocity, this.getVelocity() ), l_repulsetoOthers );

        return l_temp;
    }

    /**
     * returns pedestrian's radius
     * @return radius
     **/
    public double getM_radius()
    {
        return m_radius;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public IPedestrian call() throws Exception {

        final double l_check = CVector.sub( this.getGoalposition(), this.getPosition() ).length();

        if(this.m_name.startsWith("t"))
        {
            ArrayList<String> l_temp =m_realdata.get( new StringBuffer(new StringBuffer(String.valueOf(l_env.getCurrentCycle()))
                    .append(this.m_name).append(l_env.getScenario())).toString());
            if( l_temp != null )
            {
                this.m_position = new Vector2d( Double.parseDouble( l_temp.get(0) )*m_pixelpermeter,
                        Double.parseDouble( l_temp.get(1) )*m_pixelpermeter);
                return this;
            }

        }
        else{
        if ( l_check <= 1*m_pixelpermeter)//this.getM_radius() )
        {
            this.m_velocity = new Vector2d(0, 0);
            if ( this.m_goals.size() > 0 )
            {
                this.m_goal = this.m_goals.remove( 0 );
                Vector2d l_vel = CVector.add( this.m_velocity, CVector.scale(0.5,this.accelaration())) ;
                this.m_velocity = CVector.scale(CForce.turncate( m_maxspeed, l_vel.length() ), l_vel);
                this.m_position = CVector.add( m_position, CVector.scale(0.5,m_velocity ) );
            }
        }
        else
        {
            Vector2d l_vel = CVector.add( this.m_velocity, CVector.scale(0.5,this.accelaration())) ;
            this.m_velocity = CVector.scale(CForce.turncate( m_maxspeed, l_vel.length() ), l_vel);
            this.m_position = CVector.add( m_position, CVector.scale(0.5,m_velocity ) );
        }

        /*if( m_position.getX() > 120*m_pixelpermeter ) {
            setposX( 0.0 );
        }
        if( m_position.getX() < 0.0 ) {
            setposX( 120*m_pixelpermeter );
        }
        if( m_position.getY() > 100*m_pixelpermeter ) {
            setposY( 0.0 );
        }
        if( m_position.getY() < 0.0 ) {
            setposY( 100*m_pixelpermeter );
        }*/
        }

        return this;
    }
}
