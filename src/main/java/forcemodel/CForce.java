package forcemodel;

import javax.vecmath.Vector2d;
import java.util.ArrayList;

import static forcemodel.CEnvironment.m_pixelpermeter;

/**
 * calculate all forces
 * Created by Fatema on 10/22/2016.
 */
public class CForce
{

    private static final double m_detta = 0.5;
    private static double m_lamda = 0.5;//0.35;
    private static double m_repulsefactortoped = 0.6*m_pixelpermeter;//2.84 //0.1*m_pixelpermeter;
    private static double m_sigma = 0.06*m_pixelpermeter;//0.480.25*m_pixelpermeter;
    private static final double m_repulsefactortowall = 5;
    private static double m_repulsefactortopedcar = 2.1;//0.1*m_pixelpermeter;
    private static double m_sigmacar = 0.3;//0.25*m_pixelpermeter;
    private static double m_repulsefactorcartoped = 2.1;//0.1*m_pixelpermeter;
    private static double m_sigmacartoped = 0.3;//0.25*m_pixelpermeter;
    private static final double m_R = 0.2;

    /**
     * calculate seek force towards goal position
     * @return force vector
     **/
    static Vector2d drivingForce( final Vector2d p_desiredvelocity, final Vector2d p_current )
    {
        return CVector.scale(0.5,CVector.sub( p_desiredvelocity, p_current));
    }

    /*
     * helper function to calculate repulsive force to pedestrian
     * @return double value
     **/
    static double calculateb( final CPedestrian p_self, final CPedestrian p_other )
    {
        final Vector2d l_tempvector = CVector.sub( p_self.getPosition(), p_other.getPosition() );

        final double l_tempvalue = CVector.sub( l_tempvector, CVector.scale( m_detta, p_other.getVelocity() ) ).length();

        return Math.sqrt( ( l_tempvector.length() + l_tempvalue ) * ( l_tempvector.length() + l_tempvalue )
                - ( p_other.getVelocity().length()*m_detta * p_other.getVelocity().length()*m_detta ) )/2;
    }

    /**
     * calculate repulsive force towards other pedestrian
     * @return force vector
     **/
    static Vector2d repulseothers( final CPedestrian p_self, final CPedestrian p_other )
    {
        final Vector2d l_normvector = CVector.scale(-1,CVector.direction( p_self.getPosition(), p_other.getPosition() ));
        if(p_other.m_type == 2)
        {
            final double l_temp = -calculateb(p_self, p_other) / m_sigmacar;
            return CVector.scale(m_repulsefactortopedcar * Math.exp(l_temp), l_normvector);
        }
        else
        {
            final double l_temp = -calculateb(p_self, p_other) / m_sigma;
            return CVector.scale(m_repulsefactortoped * Math.exp(l_temp), l_normvector);
        }

    }

    static Vector2d fov( final CPedestrian p_self, final CPedestrian p_other )
    {
        final Vector2d l_dir = CVector.direction( p_self.getGoalposition(), p_self.getPosition() );
        final Vector2d l_repulsePed = CVector.scale(-1,repulseothers(p_self,p_other));

        if (l_dir.dot(l_repulsePed) >= l_repulsePed.length()*Math.cos(100))
            return l_repulsePed;
        else return CVector.scale(m_lamda, l_repulsePed);
    }

    static double turncate(double v_max, double v_desired)
    {
        if( v_desired <= v_max) return 1;
        else return v_max/v_desired;
    }

    /**
     * calculate repulsive force towards other pedestrian (another way)
     * @return force vector
     **/
    static Vector2d repulseotherPed( final CPedestrian p_self, final CPedestrian p_other )
    {
        if(p_other.m_type == 2)
        {
            final double l_radious = p_self.getM_radius()/2f + p_other.getM_radius()/1.5f;
            final double l_temp = l_radious - CVector.distance(p_self.getPosition(), p_other.getPosition());

            return CVector.scale(m_repulsefactortopedcar * Math.exp(l_temp / m_sigmacar) * anisotropic_character(p_self.getPosition(),
                    p_other.getPosition()), CVector.normalize(CVector.sub(p_self.getPosition(), p_other.getPosition())));
        }
        else
        {
            final double l_radious = p_self.getM_radius()/2f + p_other.getM_radius()/2f;
            final double l_temp = l_radious - CVector.distance(p_self.getPosition(), p_other.getPosition());

            return CVector.scale(m_repulsefactortoped * Math.exp(l_temp / m_sigma) * anisotropic_character(p_self.getPosition(),
                    p_other.getPosition()), CVector.normalize(CVector.sub(p_self.getPosition(), p_other.getPosition())));
        }
    }

    /**
     * calculate repulsive force towards wall
     * @return force vector
     **/
    static Vector2d repulsewall( final CPedestrian p_self, final CWall p_wall, ArrayList<COutput> test )
    {
        final Vector2d l_normposition = CVector.perpendicular_derection( p_self.getPosition(), p_wall );

        if ( CVector.check( l_normposition, p_wall.getPoint1(), p_wall.getPoint2() ) )
        {
            final double l_temp = p_self.getM_radius() - CVector.distance( p_self.getPosition(), l_normposition );
            double p = m_repulsefactortowall * Math.exp( l_temp / m_R );
            if ( l_temp >= - 4 ) {
                return CVector.scale( m_repulsefactortowall * Math.exp( l_temp / m_R ), CVector.normalize(
                        CVector.sub( p_self.getPosition(), l_normposition ) ) );
            }
        }
        return new Vector2d(0,0);
    }

    /**
     * check if wall/other pedestrians is under pedestrain's point of view or not?
     * @return double value
     **/
    static double anisotropic_character( final Vector2d p_v1, final Vector2d p_v2 )
    {
        return m_lamda + ( 1 -m_lamda )*( ( 1 + CVector.angle( p_v1, p_v2 ) ) * 0.5 );
    }

    public static void update(double p_pedtopedrf,  double p_pedtopedsigma )
    {
        m_repulsefactortopedcar = p_pedtopedrf*m_pixelpermeter;
        m_sigmacar = p_pedtopedsigma*m_pixelpermeter;
    }
}
