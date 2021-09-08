import forcemodel.CCSVFileReaderForGA;
import forcemodel.CEnvironment;
import forcemodel.CForce;
import forcemodel.CInputFormat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CMain_GA
{
    static Map<String, ArrayList<String>> m_realdata = CCSVFileReaderForGA.readDataFromCSV2();
    private CMain_GA()
    {
    }

    protected static ArrayList<CSampleOutput> runSimulation(double p_pedtopedrf, double p_pedtopedsigma,int p_senarioid )//double p_pedtocarrf, double p_pedtocarsigma,
    //double p_lamda, double p_cartopedrf, double p_cartopedsigma,

    {
        CForce.update(p_pedtopedrf, p_pedtopedsigma);
        List<CInputFormat> l_roadusers = CCsvFileReader.readDataFromCSV(System.getProperty("user.dir").concat("/start_end_citr_with_extended_goal.csv"), p_senarioid);
        List<CInputFormat> l_pedestrians = l_roadusers.stream().filter( n -> n.m_roaduser_id.startsWith("p") ).collect(Collectors.toList());
        List<CInputFormat> l_cars = l_roadusers.stream().filter( n -> n.m_roaduser_id.startsWith("t") ).collect(Collectors.toList());
        CEnvironment l_env = new CEnvironment(l_pedestrians,l_cars);

        int l_start = l_roadusers.stream().map( i -> i.m_start_cycle ).sorted().collect(Collectors.toList()).get(0);
        int l_end = l_roadusers.stream().map( i -> i.m_numberofcycle ).sorted(Comparator.reverseOrder()).collect(Collectors.toList()).get(0);
        l_env.setScenario(p_senarioid);
        ArrayList<CSampleOutput> m_output = new ArrayList<>();
        final float[] l_timestep = {l_start/2f};
        IntStream.range(l_start, l_end+1)//l_end-9 l_start, l_end -8
                .forEach( j ->
                {
                    if ( l_env.addPedtoInitializeLater().get(j) != null )
                    {
                        l_env.addPedtoInitializeLater().get(j)
                                .forEach( n -> l_env.initialPedestrian( n ));
                    }
                    l_env.m_pedestrian.stream()
                            .parallel()
                            .forEach( i ->
                            {
                                try
                                {
                                    i.call();
                                    ArrayList<String> l_temp = m_realdata.get( new StringBuffer(new StringBuffer(String.valueOf(l_timestep[0])).append(i.m_name)).append(p_senarioid).toString());
                                    if(l_temp != null)
                                    {
                                        double l_speed =  i.getVelocity().length() == 0.2 ? 0.0 : i.getVelocity().length();
                                        m_output.add( new CSampleOutput( l_timestep[0], i.m_name,i.getPosition().x, i.getPosition().y, l_speed ) );
                                    }
                                    Thread.sleep(5);
                                }
                                catch ( final Exception l_exception ) {}

                            });
                    l_timestep[0] = (float) (l_timestep[0] + 0.5);
                    l_env.setCurrentCycle(l_timestep[0]);
                });
        return m_output;
    }
}
