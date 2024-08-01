package framework.custom.tasks.robot;

import framework.custom.models.robot.ArmConfiguration;
import framework.custom.models.robot.ArmGroupMovement;
import framework.custom.models.robot.RobotConfiguration;
import framework.custom.tasks.ITask;
import framework.custom.utils.robot.ArmGetHttpRequest;
import framework.custom.utils.robot.ArmReadConfiguration;
import framework.custom.utils.robot.SendArduinoCommand;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SendCommandToArmWithAxes implements ITask {

    private final String nombreMovimiento;
    private final RobotConfiguration robotConfiguration;

    Logger logger = Logger.getLogger(SendCommandToArmWithAxes.class.getName());

    public SendCommandToArmWithAxes(String nombreMovimiento, RobotConfiguration robotConfiguration) {
        this.nombreMovimiento = nombreMovimiento;
        this.robotConfiguration=robotConfiguration;
    }

    public static SendCommandToArmWithAxes with(String nombreMovimiento, RobotConfiguration robotConfiguration){
        return new SendCommandToArmWithAxes(nombreMovimiento,robotConfiguration);
    }

    @Override
    public void perform() throws Exception {

        ArmConfiguration armConfigCube = null;
        try {
            armConfigCube = ArmReadConfiguration.util().readConfig(robotConfiguration.getPathMovesAxes());
        } catch (Exception e) {

            logger.log(Level.SEVERE,() ->"No se obtuvo el archivo de configuracion de los movimientos - error: " + e.getMessage());
            throw e;
        }
        ArmGroupMovement groupMovement = armConfigCube.getGroupMovementsMap().get(nombreMovimiento);

        for (String s : groupMovement.getStepCube()) {
            System.out.println("Submovimiento: " + s);
           if (s.startsWith("X")) {
                s = s.replace("\r", "");
               // SendArduinoCommand.util().
                 //       send(s, 1, "",robotConfiguration);
            } else if (s.startsWith("(ARM)")) {
                s = s.replace("\r", "");
                s = s.substring(s.indexOf(")") + 1);
                //SendCommandToArm.
                  //      with(s, robotConfiguration).perform();
            } else if (s.startsWith("R")) {
                //SendArduinoCommand.util().
                  //      send(s, 1, "",robotConfiguration);
            }
        }
    }
}
