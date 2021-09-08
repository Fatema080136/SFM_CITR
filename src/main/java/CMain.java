import forcemodel.*;
import forcemodel.CCSVFileReaderForGA;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * main class
 * Created by Fatema on 10/20/2016.
 */
public class CMain extends JFrame
{
    private static final String FILE_HEADER = "scenarioid,time,id,x_sim,y_sim,velocity";
    static Map<String, ArrayList<String>> m_realdata = CCSVFileReaderForGA.readDataFromCSV2();

    public CMain()
    {
    }

    public static void main( String []args ) throws InterruptedException
    {
        long startTime = System.currentTimeMillis();
        CForce.update(3.02, 0.09);

         //to write simulation output
        ArrayList<ArrayList<COutputFormat>> m_finaloutput = new ArrayList<>();

        //to write simulation output
        final String l_name = System.getProperty("user.dir").concat("/CITR_dataset" +
                "circular.csv");

        for(int s = 1; s <=26; s++)
        {
            ArrayList<COutputFormat> m_output = new ArrayList<>();
            JFrame frame = new JFrame("Social Force Model");
            List<CInputFormat> l_roadusers = CCsvFileReader.readDataFromCSV(System.getProperty("user.dir").concat("/start_end_citr_with_extended_goal.csv"), s);
            List<CInputFormat> l_pedestrians = l_roadusers.stream().filter( n -> n.m_roaduser_id.startsWith("p") ).collect(Collectors.toList());
            List<CInputFormat> l_cars = l_roadusers.stream().filter( n -> n.m_roaduser_id.startsWith("t") ).collect(Collectors.toList());
            CEnvironment l_env = new CEnvironment(l_pedestrians,l_cars);
            int l_start = l_roadusers.stream().map( i -> i.m_start_cycle ).sorted().collect(Collectors.toList()).get(0);
            int l_end = l_roadusers.stream().map( i -> i.m_numberofcycle ).sorted(Comparator.reverseOrder()).collect(Collectors.toList()).get(0);
            l_env.setScenario(s);
            frame.add(l_env);
            frame.setSize(120*CEnvironment.m_pixelpermeter, 100*CEnvironment.m_pixelpermeter);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //System.out.println(l_start+ " " +l_end);

            final float[] l_timestep = {l_start/2f};
                int finalS = s;
                IntStream.range(l_start, l_end+1)//l_end-9 l_start, l_end -8
                .forEach( j ->
                {
                    if ( l_env.addPedtoInitializeLater().get(j) != null )
                    {
                        l_env.addPedtoInitializeLater().get(j)
                                .forEach( n -> l_env.initialPedestrian( n ));
                    }
                    l_env.repaint();
                    l_env.m_pedestrian
                            //.parallel()
                            .forEach( i ->
                            {
                                try
                                {
                                    i.call();
                                    ArrayList<String> l_temp = m_realdata.get( new StringBuffer(new StringBuffer(String.valueOf(l_timestep[0])).append(i.m_name)).append(finalS).toString());
                                    if((i.m_type == 1) && (l_temp != null))
                                    {
                                        double l_speed =  i.getVelocity().length() == 0.2 ? 0.0 : i.getVelocity().length();
                                        m_output.add( new COutputFormat(finalS,l_timestep[0], i.m_name,i.getPosition().x, i.getPosition().y, l_speed ) );
                                    }
                                    Thread.sleep(25);
                                }
                                catch ( final Exception l_exception ) {}

                            });
                    l_timestep[0] = (float) (l_timestep[0] + 0.5);
                    l_env.setCurrentCycle(l_timestep[0]);
                });

                m_finaloutput.add(m_output);
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println(elapsedTime);

        ArrayList<COutputFormat> bla = new ArrayList<>();
        for( ArrayList<COutputFormat> out: m_finaloutput )
        {
            for (COutputFormat outt : out)
            {
                bla.add(outt);
            }
        }

        CCsvReadWrite.writeCsvFile( l_name, bla, FILE_HEADER);

    }
}
