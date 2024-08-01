package framework.custom.utils.robot;

import com.google.gson.Gson;
import framework.custom.models.robot.ArmConfiguration;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArmReadConfiguration {

    Logger logger=Logger.getLogger(ArmReadConfiguration.class.getName());

    public static ArmReadConfiguration util(){return new ArmReadConfiguration();}

    public ArmConfiguration readConfig(String filePath) throws Exception
    {
        ArmConfiguration config = null;
        File myFile = new File(filePath);
        FileInputStream fIn = new FileInputStream(myFile);
        InputStreamReader isr = new InputStreamReader(fIn);

        try (BufferedReader bufferedReader = new BufferedReader(isr)) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            Gson gson = new Gson();
            config = gson.fromJson(json, ArmConfiguration.class);
        } catch (IOException e) {
            logger.log(Level.SEVERE, () -> "Falló la lectura del file de configuracion del robot: " + e.getMessage());
        }
        return config;
    }
}
