package forcemodel;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CCSVFileReaderForGA
{
    public static Map<String,ArrayList<String>> readDataFromCSV2()
    {

        Map<String,ArrayList<String>> m_realdata = new HashMap<>();
        Path pathToFile = Paths.get(System.getProperty("user.dir").concat("/real_data_citr.csv"));

        try ( BufferedReader l_br = Files.newBufferedReader( pathToFile, StandardCharsets.US_ASCII ) )
        {
            String l_line = l_br.readLine();
            while ( l_line != null )
            {
                String[] l_attributes = l_line.split(",");

                ArrayList<String> l_temp = new ArrayList<>();
                l_temp.add(l_attributes[4]);
                l_temp.add(l_attributes[5]);

                m_realdata.put( new StringBuffer(new StringBuffer(l_attributes[1]).append(l_attributes[3])).append(l_attributes[12]).toString()
                        , l_temp );

                l_line = l_br.readLine();
            }
        }
        catch ( IOException ioe) { ioe.printStackTrace(); }
        return m_realdata;
    }

}
