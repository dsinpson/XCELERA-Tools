package framework.custom.tasks.robot;

import framework.custom.models.robot.RobotConfiguration;
import framework.custom.tasks.ITask;

import static framework.custom.procedures.ProcedureConstants.*;
import static framework.custom.procedures.ProcedureConstants.MOVEMENT_SEGURIDAD;

public class ExecuteNumpad implements ITask {

    private final String nombreMovimiento;
    private final RobotConfiguration robotConfiguration;
    private final boolean annotation;

    public ExecuteNumpad(String nombreMovimiento, RobotConfiguration robotConfiguration, boolean annotation) {
        this.nombreMovimiento=nombreMovimiento;
        this.robotConfiguration = robotConfiguration;
        this.annotation = annotation;
    }

    public static ExecuteNumpad with(String nombreMovimiento,RobotConfiguration robotConfiguration, boolean annotation){
        return new ExecuteNumpad(nombreMovimiento, robotConfiguration, annotation);
    }

    @Override
    public void perform() throws Exception {
        String tecla = "";
        String numeros = nombreMovimiento;
        numeros = numeros.replace("", "#").substring(1);
        String[] arrayNumeros = numeros.split("#");

        for (int i = 0; i < arrayNumeros.length; i++) {
            tecla = arrayNumeros[i];
            if (i == 0) {
              //  SendCommandToArm.
                //        with(MOVEMENT_ACERCARATECLADO, robotConfiguration).perform();
            }
            SendCommandToArm.
                    with(MOVEMENT_TECLA + tecla, robotConfiguration).perform();
            if (i == arrayNumeros.length - 1) {
                if(annotation) {
                  //  SendCommandToArm.
                    //        with(MOVEMENT_ANOTACION, robotConfiguration).perform();
                }
                //SendCommandToArm.
                  //      with(MOVEMENT_SEGURIDAD, robotConfiguration).perform();
            }
        }
    }
}
