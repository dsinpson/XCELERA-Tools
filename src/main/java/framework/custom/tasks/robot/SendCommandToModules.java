package framework.custom.tasks.robot;

import framework.custom.models.robot.ArmConfiguration;
import framework.custom.models.robot.ArmGroupMovement;
import framework.custom.models.robot.RobotConfiguration;
import framework.custom.tasks.ITask;
import framework.custom.utils.robot.ArmGetHttpRequest;
import framework.custom.utils.robot.ArmReadConfiguration;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SendCommandToModules implements ITask {

    Logger logger=Logger.getLogger(SendCommandToModules.class.getName());

    private final String nombreMovimiento;
    private final RobotConfiguration robotConfiguration;

    public SendCommandToModules(String nombreMovimiento, RobotConfiguration robotConfiguration) {
        this.nombreMovimiento = nombreMovimiento;
        this.robotConfiguration=robotConfiguration;
    }

    public static SendCommandToModules with(String nombreMovimiento,RobotConfiguration robotConfiguration){
        return new SendCommandToModules(nombreMovimiento,robotConfiguration);
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

        for (String s : groupMovement.getStepCube())
        {
            if(s!=null)
            {
                s = s.replace("\r", "");
                if(s.startsWith("(ARM)"))
                {
                    s = s.substring(s.indexOf(")") + 1);
                    System.out.println("Nombre submovimiento: "+s);
                    String url;

                    if (s.startsWith("BOTON")) {
                        int numBoton = Character.getNumericValue(s.charAt(5));
                        if(numBoton>=1 && numBoton<=4){
                            url = "http://" + robotConfiguration.getIpModBotones1() + "/" + s;
                        }
                        else{
                            url = "http://" + robotConfiguration.getIpModBotones2() + "/" + s;
                        }
                        ArmGetHttpRequest.util().getHttp(url);
                    }

                    else if (s.toUpperCase().contains("TARJETA"))
                    {
                        url = "http://" + robotConfiguration.getIpModTarjeta() + "/" + s;
                        ArmGetHttpRequest.util().getHttp(url);
                    }

                    else if (s.toUpperCase().contains("RETIRARRECIBO"))
                    {
                        System.out.println("RETIRA RECIBO");
                        url = "http://" + robotConfiguration.getIpModRecibo() + "/" + s;
                        ArmGetHttpRequest.util().getHttp(url);
                    }

                    else if (s.startsWith("TECLA") || s.contains("ANOTACION") || s.contains("CANCELAR"))
                    {
                        SendCommandToArm.
                                with(s, robotConfiguration).perform();
                    }
                }
            }
        }
    }
}
