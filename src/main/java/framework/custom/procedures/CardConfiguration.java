package framework.custom.procedures;

import framework.custom.procedures.fm_pattern_procedures.IPerformableProcedure;
import framework.data.entities.Procedure;
import framework.enums.TypeError;
import framework.helpers.ExecutionStatusHelper;
import framework.helpers.GeneralHelper;
import framework.helpers.ScreenshotHelper;

import static framework.custom.procedures.ProcedureConstants.*;


public class CardConfiguration implements IPerformableProcedure {

    private final Procedure procedure;
    private final ScreenshotHelper screenshotHelper;

    public CardConfiguration(Procedure procedure, ScreenshotHelper screenshotHelper) {
        this.procedure=procedure;
        this.screenshotHelper=screenshotHelper;
    }

    @Override
    public void executeCustomProcedure() throws Exception {
        ExecutionStatusHelper helper = new ExecutionStatusHelper();
        String posicionTarjeta = procedure.Value;
        String [] params = posicionTarjeta.split(";");
        try{
            if (params.length > 0) {
                String puertoCOMReaded = params[0];
                String puertoCOM = puertoCOMReaded.replace(" ","");
                String posicionSlot = params[1];
                Runtime rt = Runtime.getRuntime();
                String cmdJar = "cmd.exe /c cd \""+ DIRECTORY_SWITCH_CARD +"\" & start cmd.exe /k \"java -jar OpenSerial.jar\" " + puertoCOM + " " +  posicionSlot;
                rt.exec(cmdJar);
                Thread.sleep(7000);
                rt.exec("taskkill /f /im cmd.exe");

                helper.setStatusLogOkProcedure(procedure,"Tarjeta configurada");
            }
        }catch (Exception e){
            String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
            String mensaje = "Faltan Parámetros de llamada. Se esperan 2 separados por ';'"
                    + "mesaje obtenido de exception: " + e.getMessage();
            String comment = GeneralHelper.getCommentError(methodName, mensaje);

            screenshotHelper.takeScreenshot();
            helper.setStatusErrorOnProcedure(procedure, TypeError.OperationalError, comment);
            throw e;
        }
    }
}
