package framework.custom.utils;

import framework.data.entities.ExecutionData;
import framework.dataProviders.ConfigFileReader;
import framework.helpers.GeneralHelper;
import framework.localAgent.ScheduledExecution;
import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import static framework.helpers.GeneralHelper.now;

public class Evidencia {

    public static String _url;
    public static void  getLog(ExecutionData testToExecute)  {

        try {
            String dir="dirDefaultLogs";
            String logPathFinal = GeneralHelper.getLogPath(testToExecute);// ruta donde se va guardar el log
            String carpeta = GeneralHelper.buildPath(testToExecute,dir);
            String logPathOrigen= "C:/XCELERA/EVIDENCE/Log.txt";
            // Ruta del archivo original
            FileReader fr = new FileReader(logPathOrigen);
            BufferedReader br = new BufferedReader(fr);

            // Ruta de la copia del archivo
            FileWriter fw = new FileWriter(logPathFinal);
            BufferedWriter bw = new BufferedWriter(fw);

            // Leer el archivo original y escribir la copia
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.replaceAll("\0","");
                bw.write(linea);
                bw.newLine();
            }


            // Cerrar los flujos de lectura y escritura
            br.close();
            fr.close();
            bw.close();
            fw.close();


            Desktop.getDesktop().open(Paths.get(carpeta).toFile());
            System.out.println("Copia del archivo creada con éxito en la ruta: " + logPathFinal );
        } catch (IOException e) {
            System.out.println("Error: " + e);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            throw new RuntimeException(e);
        }
    }

    public static void clearText(){
        String logPathOrigen= "C:/XCELERA/EVIDENCE/Log.txt";

        try {
            FileWriter pathClearText = new FileWriter(logPathOrigen);
            BufferedWriter deleteOrigenText = new BufferedWriter(pathClearText);

            deleteOrigenText.write(" ");

            deleteOrigenText.close();
            pathClearText.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void crearUrl(JSONObject json){
        String id= json.getString("Id");
        ConfigFileReader reader = new ConfigFileReader("configs/config.properties");
        String api = reader.getPropertyByKey("STARCApiURL");
        _url = api + "v1/executions/" + id +"/evidence/1";

    }

      public static void  savePDF(ExecutionData testToExecute) throws Exception {
        String pdfPath = GeneralHelper.getPdfPath(testToExecute);// ruta donde se va guardar el log

        try {
            ScheduledExecution sh = new ScheduledExecution();
            FileOutputStream fl = new FileOutputStream(pdfPath);
            byte[] buffer = new byte[4096];
            InputStream ipt = sh.getPdf(_url);
            int bytesRead = -1;
            while ((bytesRead= ipt.read(buffer))!=-1){
                fl.write(buffer,0,bytesRead);
            }
            fl.close();
            ipt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
    public static void compressPdf(ExecutionData testToExecute) throws Exception {
        String dir="dirDefaultLogs";
        String carpeta = GeneralHelper.buildPath(testToExecute,dir);
        // Ruta del archivo original
        carpeta = carpeta.replaceAll("\\\\","\\\\\\\\");
        try {
            carpeta="cmd.exe /C cd " + carpeta;
            String cmdCompress = "cmd.exe /C gswin64c.exe  -sDEVICE=pdfwrite -dCompatibilityLevel=1.4 -dPDFSETTINGS=/ebook -dNOPAUSE -dQUIET -dBATCH -sOutputFile=Evidencia_" + now()  + ".pdf Evidencia.pdf";
            String cmdcd="cmd.exe /k cd";
            String removeCmd = "cmd.exe /C DEL /F  Evidencia.pdf";
            String cmd= carpeta+" && "+ cmdCompress + " & " + cmdcd + " & " + removeCmd + "&" + "exit\n";

            Runtime rt = Runtime.getRuntime();
            rt.exec(cmd);

            //rt.exec("taskkill /f /im cmd.exe");
            //rt.exec(cmd);

        } catch (IOException ioe) {
            System.out.println (ioe);
        }



    }


}
