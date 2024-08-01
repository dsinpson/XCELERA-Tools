package framework.custom.tasks.robot;

import framework.custom.models.robot.ArmConfiguration;
import framework.custom.models.robot.ArmMovement;
import framework.custom.models.robot.ArmStep;
import framework.custom.models.robot.RobotConfiguration;
import framework.custom.tasks.ITask;
import framework.custom.utils.robot.ArmReadConfiguration;
import framework.custom.utils.robot.SendArduinoCommand;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SendCommandToArm implements ITask {
    Logger logger=Logger.getLogger(SendCommandToArm.class.getName());

    private final String nombreMovimiento;
    private final RobotConfiguration robotConfiguration;

    public SendCommandToArm(String nombreMovimiento, RobotConfiguration robotConfiguration) {
        this.nombreMovimiento = nombreMovimiento;
        this.robotConfiguration = robotConfiguration;
    }

    public static SendCommandToArm with(String nombreMovimiento, RobotConfiguration robotConfiguration){
        return new SendCommandToArm(nombreMovimiento, robotConfiguration);
    }

    @Override
    public void perform() throws Exception {
        ArmConfiguration armConfig = null;

        try {
            armConfig = ArmReadConfiguration.util().readConfig(robotConfiguration.getPathMovesArm());
        } catch (Exception e) {
            logger.log(Level.SEVERE,() ->"No se obtuvo el archivo de configuracion de los movimientos - error: " + e.getMessage());
            throw e;
        }

        ArmMovement movement = armConfig.getMovementsMap().get(nombreMovimiento);

        for (ArmStep s : movement.getSteps())
        {
           // SendArduinoCommand.util().
             //       send(s.stringToSend(), 3,"OKARM",robotConfiguration);
        }

    }
}
