package framework.custom.utils.cobot;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class ClientCobotExecutor {
    public void cobotExecutor(String nomMovimiento, String tipo ) throws IOException {
        if(tipo.equals("TC") || tipo.equals("TE") || tipo.equals("TE2") || tipo.equals("TE3")){
            nomMovimiento=nomMovimiento+tipo;
        }
        //System.out.println("Ingreso a la clase de enviar al robot");
        String nombreFuncion = nomMovimiento; // Este sería el nombre que ingresas por terminal en tu caso

        ProcessBuilder pb = new ProcessBuilder("python", "C://xArm-Python-SDK/WINCOR_1011.py", nombreFuncion);
        Process p = pb.start();
        //System.out.println("Proceso de Python iniciado");
        if(!(nomMovimiento.equals("SACARTARJETA")))
        {

            // Leer la salida del proceso de Python si es necesario
            InputStream is = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
            }
        }

        // Esperar a que el proceso termine
        try {
            if(!(nomMovimiento.equals("SACARTARJETA")))
            {
                p.waitFor();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
