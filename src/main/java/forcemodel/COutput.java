package forcemodel;

/**
 * Created by Fatema on 11/6/2016.
 */
public class COutput
{
    public double m_timestep;
    public String m_id;
    public double m_selfX;
    public double m_selfY;
    public double m_speed;
    public int m_scenarioid;
    public COutput( int p_scenarioid, double p_timestep, String p_id, double p_selfX, double p_selfY, double speed )
    {
        m_timestep = p_timestep;
        m_id = p_id;
        m_selfX = p_selfX;
        m_selfY = p_selfY;
        m_speed = speed;
        m_scenarioid = p_scenarioid;
    }

    public COutput( double p_timestep, String p_id, double p_selfX, double p_selfY )
    {
        m_timestep = p_timestep;
        m_id = p_id;
        m_selfX = p_selfX;
        m_selfY = p_selfY;
    }
}
