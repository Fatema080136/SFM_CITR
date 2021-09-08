package forcemodel;

import javax.vecmath.Vector2d;

/**
 * all vector calculations
 * Created by Fatema on 10/22/2016.
 */
final class CVector {

    /**
     * calculate direction between two points
     * @return direction
     **/
    static Vector2d direction( final Vector2d p_v1, final Vector2d p_v2 )
    {
        return normalize( sub( p_v1, p_v2 ) );
    }

    /**
     * to scale vector
     * @return scaled vector
     **/
    static Vector2d scale( final double p_speed, final Vector2d p_v1 )
    {
        return new Vector2d( p_v1.getX() * p_speed, p_v1.getY() * p_speed );
    }

    /**
     * vector add operation of 2 given vectors
     * @return result of addition
     **/
    static Vector2d add( final Vector2d p_v1, final Vector2d p_v2 )
    {
        return new Vector2d( p_v1.getX() + p_v2.getX(), p_v1.getY() + p_v2.getY() );
    }

    /**
     * vector add operation of 3 given vectors
     * @return result of addition
     **/
    static Vector2d add( final Vector2d p_v1, final Vector2d p_v2, final Vector2d p_v3 )
    {
        return new Vector2d( p_v1.getX() + p_v2.getX() + p_v3.getX(), p_v1.getY() + p_v2.getY() + p_v3.getY() );
    }

    /**
     * vector subtract operation of 2 given vectors
     * @return result of subtraction
     **/
    static Vector2d sub( final Vector2d p_v1, final Vector2d p_v2 )
    {
        return new Vector2d( p_v1.getX() - p_v2.getX(), p_v1.getY() - p_v2.getY() );
    }

    /**
     * calculate distance between 2 points
     * @return distance
     **/
    static double distance( final Vector2d p_v1, final Vector2d p_v2 )
    {
        return sub( p_v1, p_v2 ).length();
    }

    /**
     * calculate the center of any line
     * @return center point
     **/
    static Vector2d staticObjectCentre( final Vector2d p_v1, final Vector2d p_v2 )
    {
        return new Vector2d( ( p_v1.getX() + p_v2.getX() )/2.0 , ( p_v1.getY() + p_v2.getY() )/2.0 );
    }

    /**
     * calculate cos of any angle theta
     * @return cos value of any angle
     **/
    static double cosdheta( final Vector2d p_v1, final Vector2d p_v2, final Vector2d p_v3 )
    {
        return ( direction( p_v1, p_v2 ).dot( sub( p_v3, p_v2 ) ) )/
                ( direction( p_v1, p_v2 ).length() * sub( p_v3, p_v2 ).length() );

    }

    /**
     * calculate angle between 2 vectors
     * @return angle
     **/
    static double angle( final Vector2d p_v1, final Vector2d p_v2 )
    {
        return p_v1.dot( p_v2 ) / p_v1.length() * p_v2.length();

    }

    /**
     * perpendicular direction from a point to a line
     * @return direction vector
     **/
    static Vector2d perpendicular_derection( final Vector2d p_position, final CWall l_wall )
    {
        final Vector2d l_wallPoint = l_wall.getPoint1();
        final Vector2d l_walldirection = CVector.normalize( CVector.sub( l_wall.getPoint2(), l_wall.getPoint1() ) );
        final double l_check = CVector.sub( p_position, l_wallPoint ).dot( l_walldirection );

        return CVector.add( l_wallPoint, CVector.scale( l_check, l_walldirection ) );
    }

    /**
     * truncate a vector's magnitude comparing with a given double value
     * @return truncated vector
     **/
    static Vector2d truncate( final Vector2d p_vector, final double p_scalefactor ) {
        double l_check;

        l_check = p_scalefactor / p_vector.length();
        l_check = l_check < 1.0 ? 1.0 : 1/l_check;

        return CVector.scale( l_check, p_vector );
    }

    /**
     * check if wall is under pedestrain point of view or not?
     * @return true or false
     **/
    static boolean check( final Vector2d p_point, final Vector2d p_wallpoint1, final Vector2d p_wallpoint2 )
    {
        final double l_wall2Towall1 = CVector.sub( p_wallpoint2, p_wallpoint1 ).length();
        final double l_pointTowall1 = CVector.sub( p_point, p_wallpoint1 ).length();
        final double l_pointTowall2 = CVector.sub( p_wallpoint2, p_point ).length();

        return ( l_wall2Towall1 == l_pointTowall1 + l_pointTowall2 ) ? true : false;
    }

    /**
     * calculate normalized vector
     * @return normalized vector
     **/
    static Vector2d normalize( final Vector2d p_vector ) {

        Vector2d l_temp = new Vector2d( 0, 0 );

        if ( p_vector.length() != 0 )
        {
            l_temp.x = p_vector.x / p_vector.length();
            l_temp.y = p_vector.y / p_vector.length();
        }

        return l_temp;
    }

    /**
     * calculatee distance from a point to a line
     * @return distance vector
     **/
    public static Vector2d distanceToWall( final Vector2d p_position, final Vector2d p_wall1, final Vector2d p_wall2 )
    {
        if( CVector.sub( p_position, p_wall1 ).dot( CVector.direction( p_wall2, p_wall1 ) ) <= 0 )
        {
            return CVector.sub( p_wall1, p_position );
        }
        else if( CVector.sub( p_position, p_wall1 ).dot( CVector.direction( p_wall2, p_wall1 ) ) > 0 && CVector.sub( p_position,
                            p_wall1 ).dot( CVector.direction( p_wall2, p_wall1 ) ) <= CVector.sub( p_wall2, p_wall1 ).length() )
        {
            return CVector.sub( CVector.sub( p_wall1, p_position ), CVector.scale( CVector.sub( p_wall1, p_position )
                            .dot( CVector.direction( p_wall2, p_wall1 ) ), CVector.direction( p_wall2, p_wall1 ) ) );
        }
        else return CVector.sub( p_wall2, p_position );

    }

}
