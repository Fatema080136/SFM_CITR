package forcemodel;

import javax.vecmath.Vector2d;
import java.util.ArrayList;

/**
 * to create goal list
 * Created by Fatema on 10/26/2016.
 */
public class CGoal {

    private ArrayList<Vector2d> m_goals = new ArrayList<>();

    public CGoal( final double l_x, final double l_x1, final double l_x2, final double l_x3  )
    {
        //m_goals.add( new Vector2d( l_x , 20 + ( Math.random() * 100 ) ) );
        m_goals.add( new Vector2d( l_x , 160 + ( Math.random() * 60 ) ) );//20
        m_goals.add( new Vector2d( l_x, 265 ) );
        m_goals.add( new Vector2d( l_x2 + ( 20 + Math.random() * l_x3 ) , 400 + ( Math.random() * 140 ) ) );
    }

    /**
     * returns get list of goals
     * @return list of goals
     **/
    public ArrayList<Vector2d> get_goals()
    {
        return m_goals;
    }
}
