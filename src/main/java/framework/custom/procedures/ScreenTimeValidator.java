package framework.custom.procedures;

import framework.custom.procedures.fm_pattern_procedures.IPerformableProcedure;
import framework.data.entities.Procedure;
import framework.enums.TypeError;
import framework.helpers.ExecutionStatusHelper;
import framework.helpers.GeneralHelper;
import framework.helpers.ScreenshotHelper;

import java.util.logging.Level;
import java.util.logging.Logger;

import static framework.custom.procedures.ProcedureConstants.*;

public class ScreenTimeValidator implements IPerformableProcedure {

    Logger logger=Logger.getLogger(ScreenTimeValidator.class.getName());

    private final Procedure procedure;
    private final ScreenshotHelper screenshotHelper;

    public ScreenTimeValidator(Procedure procedure, ScreenshotHelper screenshotHelper) {
        this.procedure=procedure;
        this.screenshotHelper=screenshotHelper;
    }

    @Override
    public void executeCustomProcedure() throws Exception {

        ExecutionStatusHelper helper = new ExecutionStatusHelper();

        int valorTiempo = Integer.parseInt(procedure.Value);
        boolean result;

        result = valorTiempo == OCRCapturer.getDiff() || Math.abs(valorTiempo - OCRCapturer.getDiff()) < 4;

        if(result) {
            logger.log(Level.INFO,() ->"\n*****************************" +
                    "***\nOK: Mensaje encontrado en el tiempo correcto\n****" +
                    "****************************\n");
            String comment ="El texto: "+valorTiempo+" ha sido encontrado. ";
            helper.setStatusLogOkProcedure(procedure,comment);
        }
        else {
            logger.log(Level.WARNING,() ->"\n****************************" +
                    "****\n>>> ERROR: ERROR, Mensaje no encontrado en el tiempo correcto\n**" +
                    "******************************\n");

            if(RobotExecutor.getStatusSacarTarjeta()){
                String tipo= RobotExecutor.getTipoTarjeta();
                RobotExecutor.getInstance(MOVEMENT_SACARTARJETA, tipo).executeCustomProcedure();
            }

            String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
            String comment="No se encontr� el texto en el tiempo establecido: "+valorTiempo+" seg. ";
            String commentFinal = GeneralHelper.getCommentError(methodName, comment);

            screenshotHelper.takeScreenshot();
            helper.setStatusErrorOnProcedure(procedure, TypeError.OperationalError, commentFinal);
            wait(10000);
        }
    }
}
