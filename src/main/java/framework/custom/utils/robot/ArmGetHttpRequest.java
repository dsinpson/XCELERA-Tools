package framework.custom.utils.robot;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ArmGetHttpRequest {

    public static ArmGetHttpRequest util(){return new ArmGetHttpRequest();}

    public void getHttp(String url) {
        System.out.println(url);

        boolean perfecto = false;
        int reps = 0;
        while (!perfecto && reps<5) {
            try {
                Thread.sleep(500);
                peticionHttpGet(url);
                perfecto = true;
                System.out.println("Peticion enviada: '" + url+"' ");
            } catch (IOException e ) {
                System.out.println("No encontró el servidor : '" + url+"' en intento "+reps);
                reps++;
            }catch (InterruptedException ex){
                Thread.currentThread().interrupt();
            }
        }
    }
    private void peticionHttpGet(String urlParaVisitar) throws IOException {
        try {
            // Crear un objeto de tipo URL
            URL url = new URL(urlParaVisitar);
            // Abrir la conexión e indicar que será de tipo GET
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setDoInput(true);
            conexion.setRequestMethod("GET");
            conexion.getInputStream();

            Thread.sleep(200);
            conexion.disconnect();
        }catch (InterruptedException ex){
            Thread.currentThread().interrupt();
            }
    }
}
