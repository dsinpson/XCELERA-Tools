package framework.custom.utils.robot;
import framework.custom.models.robot.RobotConfiguration;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
public class EthernetMessaging {
    Logger logger = Logger.getLogger(EthernetMessaging.class.getName());
    static final int PORT_ARDUINO = 55010;

    //private final String text;
    //private final RobotConfiguration robotConfiguration;


    public EthernetMessaging() {
       /* this.text = text;


       if(robotConfiguration.getClient()==null || !robotConfiguration.getClient().isConnected() || robotConfiguration.getClient().isClosed())
        {
            try{
                Socket client = new Socket(robotConfiguration.getServerIP(), PORT_ARDUINO);
                robotConfiguration.setClient(client);
                logger.log(Level.INFO,() ->"Conectado al "+ robotConfiguration.getServerIP());
            }catch(Exception e) {
                System.out.println ("Resete el Arduino");
                new EthernetMessaging(text,robotConfiguration);
//                logger.log(Level.SEVERE,() ->"No conecto a la IP - mensaje: " + e.getMessage());
//                throw e;
            }
        }
        this.robotConfiguration = robotConfiguration;
    }


    public static EthernetMessaging util(String text, RobotConfiguration robotConfiguration) throws Exception {
        return new EthernetMessaging(text, robotConfiguration);
    }


    public RobotConfiguration send() throws Exception {
        BufferedWriter sender;
        try {
            sender = new BufferedWriter(new OutputStreamWriter(robotConfiguration.getClient().getOutputStream()));

            sender.write(text);
            sender.write('\0');
            sender.flush();
        } catch (Exception e) {
            logger.log(Level.SEVERE,() ->"No envió el mensaje a la IP - error: " + e.getMessage());
            throw e;
        }

        return robotConfiguration;
    }

    public void waitText() throws Exception {
        BufferedReader in;
        String expectedText=text;
        String retStr = null;
        int count = 0;

        if(expectedText.equals("READY")){
            expectedText = "DESKOK";
        }

        try{
            in = new BufferedReader(new InputStreamReader(robotConfiguration.getClient().getInputStream(), StandardCharsets.UTF_8));
            while(!in.ready() && count < 100){
                Thread.sleep(100);
                count++;
            }
            if(count == 100){
                logger.log(Level.SEVERE,() ->"Time out arduino response");
            }
            while(in.ready()){
                retStr = in.readLine();
            }
            assert retStr != null;
            if(!retStr.equals("") && !retStr.contains(expectedText)){
                String finalExpectedText = expectedText;
                String finalRetStr = retStr;
                logger.log(Level.SEVERE,() ->"Expected: " + finalExpectedText + ". Received: " + finalRetStr);
            }
        }catch(IOException e) {
            logger.log(Level.SEVERE,() ->"No obtuvo mensaje desde la IP - error: " + e.getMessage());
            throw e;
        }catch (InterruptedException ie){
            logger.log(Level.SEVERE,() ->"Error durante el tiempo sleep - error: " + ie.getMessage());
            throw ie;
        }*/
    }
 }

