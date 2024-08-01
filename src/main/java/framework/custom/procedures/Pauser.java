package framework.custom.procedures;

import framework.custom.procedures.fm_pattern_procedures.IPerformableProcedure;
import framework.data.entities.Procedure;
import framework.enums.TypeError;
import framework.helpers.ExecutionStatusHelper;
import framework.helpers.GeneralHelper;
import framework.helpers.ScreenshotHelper;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Pauser implements IPerformableProcedure {

    private final Procedure procedure;
    private final ScreenshotHelper screenshotHelper;

    Logger logger=Logger.getLogger(Pauser.class.getName());

    public Pauser(Procedure procedure, ScreenshotHelper screenshotHelper) {
        this.procedure=procedure;
        this.screenshotHelper=screenshotHelper;
    }

    @Override
    public void executeCustomProcedure() throws Exception {
        ExecutionStatusHelper helper = new ExecutionStatusHelper();

        try {

            int sec;
            String value =procedure.Value;
            if (value.contains("=")){
                sec = Integer.parseInt(value.substring(value.indexOf("=")+1));
            }else {
                sec = Integer.parseInt(procedure.Value);
            }

            logger.log(Level.INFO,() ->"Espera "+ sec +" segundos...");

            Thread.sleep(sec*1000L);

        }catch (InterruptedException e) {

            String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
            String mensaje = "Falla en tiempo de espera de ejecucion manual"
                    + "mesaje obtenido de exception: " + e.getMessage();
            String comment = GeneralHelper.getCommentError(methodName, mensaje);

            screenshotHelper.takeScreenshot();
            helper.setStatusErrorOnProcedure(procedure, TypeError.OperationalError, comment);

            throw e;
        }
    }
}
