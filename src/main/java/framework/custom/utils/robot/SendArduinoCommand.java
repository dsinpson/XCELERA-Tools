package framework.custom.utils.robot;

import framework.custom.models.robot.RobotConfiguration;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SendArduinoCommand {

    Logger logger=Logger.getLogger(SendArduinoCommand.class.getName());

    public static SendArduinoCommand util(){return new SendArduinoCommand();}


   /* public String send(String command, int maxTries, String waitString, RobotConfiguration robotConfiguration) throws Exception {

       RobotConfiguration robotConfigurationWithClientUpdated;

        if (maxTries == 0) {
            throw new RuntimeException("Maximum number of tries sending command: " + command);
        } else {
            try {
                command = command.toUpperCase();
                robotConfigurationWithClientUpdated= EthernetMessaging
                        .util(command, robotConfiguration)
                        .send();
                EthernetMessaging
                        .util(waitString, robotConfigurationWithClientUpdated)
                        .waitText();
                return waitString;
            } catch (Exception e) {
                logger.log(Level.SEVERE,() ->"No se envio el mensaje al Arduino - error: " + e.getMessage());
                throw e;
            }
        }
    }*/
}
