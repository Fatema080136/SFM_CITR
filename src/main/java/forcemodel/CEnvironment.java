package forcemodel;

import java.awt.*;
import javax.swing.JPanel;
import javax.vecmath.Vector2d;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;


/**
 * environment class
 * Created by Fatema on 10/20/2016.
 */

public class CEnvironment extends JPanel {

    private static double m_timestep;
    private ArrayList<CPedestrian> m_car = new ArrayList<CPedestrian>();;
    private Graphics2D graphics2d;
    private Random rand = new Random();
    private static final double m_GaussianMean = 1.34;
    private static final double m_GaussianStandardDeviation = 0.26;
    public ArrayList<CPedestrian> m_pedestrian = new ArrayList<CPedestrian>();
    private ArrayList<CStatic> m_wall = new ArrayList<>( 2 );
    private ArrayList<CWall> m_walledge = new ArrayList<>( );
    public ArrayList<COutput> test = new ArrayList<>();
    public static final int m_pixelpermeter = 1;
    private Map<Integer,List<CPedestrian>> m_pedestrianneedtoinitialize = Collections.synchronizedMap(new HashMap<>());
    private Map<Integer,List<CPedestrian>> m_carneedtoinitialize = Collections.synchronizedMap(new HashMap<>());
    private int m_scenario;

    public CEnvironment( List<CInputFormat> p_peds,  List<CInputFormat> p_cars )
    {

        setFocusable( true );
        setBackground( Color.WHITE );
        setDoubleBuffered( true );

        p_peds.forEach( i ->
                {
                    CPedestrian l_pedestrian = new CPedestrian( new Vector2d(i.m_startx_axis*m_pixelpermeter,i.m_starty_axis*m_pixelpermeter),
                    i.m_speed*m_pixelpermeter,i.m_max_speed*m_pixelpermeter, new Vector2d(i.m_endx_axis*m_pixelpermeter,
                            i.m_endy_axis*m_pixelpermeter ), this, i.m_type, i.m_roaduser_id );
                    if (i.m_start_cycle == 0)
                        m_pedestrian.add(l_pedestrian);
                    else
                        m_pedestrianneedtoinitialize.computeIfAbsent(i.m_start_cycle, k -> new ArrayList<>()).add(l_pedestrian);

                } );

        p_cars.forEach( i -> {
            CPedestrian l_pedestrian = new CPedestrian( new Vector2d(i.m_startx_axis*m_pixelpermeter,i.m_starty_axis*m_pixelpermeter),
                    i.m_speed*m_pixelpermeter,i.m_max_speed*m_pixelpermeter, new Vector2d(i.m_endx_axis*m_pixelpermeter,
                    i.m_endy_axis*m_pixelpermeter ), this, i.m_type, i.m_roaduser_id );
            if (i.m_start_cycle == 0) m_pedestrian.add(l_pedestrian);
            else
                m_pedestrianneedtoinitialize.computeIfAbsent(i.m_start_cycle, k -> new ArrayList<>()).add(l_pedestrian);

        } );
    }
    public static double getTimestep()
    {
        return m_timestep;
    }
    public Map<Integer, List<CPedestrian>> addPedtoInitializeLater()
    {
        return m_pedestrianneedtoinitialize;
    }
    public Map<Integer,List<CPedestrian>> addCartoInitializeLater()
    {
        return m_carneedtoinitialize;
    }

    /**
     * paint all elements
     * @return
     **/
    public void paint( Graphics g ) {
        super.paint( g );
        graphics2d = ( Graphics2D ) g;
        drawCar();
        drawPedestrian();
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    /**
     * draw each static element wall
     * @return
     **/
    private void drawWall(Color color) {
        graphics2d.setColor( color ) ;
        m_wall.forEach( i -> graphics2d.fillRect( i.getX1(), i.getY1(), i.getWidth(), i.getHeight() ) );
    }

    /**
     * draw each pedestrian
     * @return
     **/
    private void drawPedestrian() {
        graphics2d.setColor( Color.BLUE ) ;

        m_pedestrian.stream().filter( i-> i.m_type == 1).forEach(i -> {
                    Ellipse2D.Double shape = new Ellipse2D.Double( i.getPosition().getX(), i.getPosition().getY(),
                            0.5*m_pixelpermeter, 0.5*m_pixelpermeter );
                    graphics2d.fill( shape );

                });
    }

    private void drawCar() {
        graphics2d.setColor( Color.GREEN ) ;

        m_pedestrian.stream().filter( i-> i.m_type == 2)
                .forEach(i -> {
                    AffineTransform rat = graphics2d.getTransform();
                    graphics2d.rotate( Math.toRadians(205),i.getPosition().x, i.getPosition().y );
                    graphics2d.fillRect( (int)(i.getPosition().x -1.5*m_pixelpermeter ), (int)(i.getPosition().y -0.85*m_pixelpermeter), (int)3*m_pixelpermeter, (int)(1.7*m_pixelpermeter) );//20,10//16, 8
                    //graphics2d.rotate( i.getPSI(),i.getPosition().x, i.getPosition().y );
                    //graphics2d.fillRect( (int)(i.getPosition().x -1.5*m_pixelpermeter ), (int)(i.getPosition().y -1*m_pixelpermeter), (int)3*m_pixelpermeter, (int)2*m_pixelpermeter );//20,10//16, 8
                    graphics2d.setTransform(rat);
                    graphics2d.setColor( Color.GREEN ) ;

                });
    }
    /**
     * get the list of pedestrian with their information
     * @return a list of pedestrian information
     **/
    public ArrayList<CPedestrian> getPedestrianinfo()
    {
        return m_pedestrian;
    }

    /**
     * get the list of walls with their information
     * @return a list of wall information
     **/
    public ArrayList<CWall> getWallinfo()
    {
        return m_walledge;
    }

    /**
     * undate environment state
     * @return
     **/
    public void update() {

            m_pedestrian.stream()
                    //.parallel()
                    .forEach( j ->
                    {
                        try
                        {
                            j.call();
                        }
                        catch ( final Exception l_exception ) {}
                    });
    }

    public void initialPedestrian(CPedestrian n)
    {
        m_pedestrian.add(n);
    }

    public void setCurrentCycle(float v)
    {
        m_timestep = v;
    }
    public int getScenario()
    {
        return m_scenario;
    }

    public void setScenario(int p_senarioid)
    {
        m_scenario = p_senarioid;
    }

    public double getCurrentCycle()
    {
        return m_timestep;
    }
}
