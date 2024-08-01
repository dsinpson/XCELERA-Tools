package framework.custom.procedures;

import framework.custom.procedures.fm_pattern_procedures.IPerformableProcedure;
import framework.custom.questions.authentic.AuthenticTransactionStatus;
import framework.custom.questions.authentic.CreditCardLogs;
import framework.custom.questions.authentic.DebitCardLogs;
import framework.custom.questions.iseries.LogSciffsaldo;
import framework.data.dynamicValues.DynamicValuesCustom;
import framework.data.entities.Procedure;
import framework.enums.TypeError;
import framework.helpers.ExecutionStatusHelper;
import framework.helpers.GeneralHelper;
import framework.helpers.ScreenshotHelper;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static framework.custom.procedures.ProcedureConstants.SUFIJO;

public class IseriesBalanceValidation implements IPerformableProcedure {

    Logger logger=Logger.getLogger(AuthenticValidation.class.getName());
    private String logAuthentic = "";
    private int erroresAuthentic = 0;
    private final ScreenshotHelper screenshotHelper;
    private final Procedure procedure;
    public IseriesBalanceValidation(Procedure procedure, ScreenshotHelper screenshotHelper) {
        this.screenshotHelper=screenshotHelper;
        this.procedure=procedure;
    }

    @Override
    public void executeCustomProcedure() throws Exception {


        ExecutionStatusHelper helper = new ExecutionStatusHelper();
        DynamicValuesCustom dmc = new DynamicValuesCustom();

        String authenticInternalCode = dmc.GET_AUTHENTIC_INTERNAL_CODE();
        String transactionType = dmc.GET_TIPO_TRANSACCION();
        String cajero = dmc.GET_CAJERO();
        String trxType = dmc.GET_TRX_TYPE();
        String messageType = dmc.GET_MESSAGE_TYPE();
        String actionCode = dmc.GET_ACTION_CODE();
        String cuenta = dmc.GET_NUMERO_CUENTA();
        String saldoFin = dmc.GET_SALDOFINAL();

        String flagRespuesta = "0";
        String logRespuesta = "";


        String[] arrLogComff;
        String[] arrLogSciff;
        String[] arrLogSti;

        String casoAplica =  dmc.GET_CASO_APLICA();
        logger.log(Level.INFO,() ->"* * * CASO PARA EL QUE APLICA ESTA PRUEBA: "+ casoAplica);
        logger.log(Level.INFO,() ->"En validarAmbientes TipoTransaccion "+ transactionType);

        String[] authArray = AuthenticTransactionStatus.
                with(cajero,trxType,messageType,authenticInternalCode).ask();




        try {

            if (transactionType.contains("CREDITO") || transactionType.contains("PREPAGO")) {

                logger.log(Level.INFO,() ->"Entro a validacion credito");
                logAuthentic = "Este caso require validacion por iSeries "+ logAuthentic +". ";
                String sufijo = obtenerSufijo(authArray[4]);
                logger.log(Level.INFO,() -> SUFIJO + sufijo);
                String[] arreglo = CreditCardLogs.with(sufijo).ask();
                logger.log(Level.INFO,() ->"llego de ValidacionIseriesCredito");
                logAuthentic = arreglo[0] + logAuthentic;
                erroresAuthentic = erroresAuthentic + Integer.parseInt(arreglo[1]);
                logger.log(Level.INFO,() ->"paso de ValidacionIseriesCredito");
                logger.log(Level.INFO,() ->"Valor ActionCode: "+actionCode);


            } else if (transactionType.contains("DCC")) {
                logger.log(Level.INFO, () -> "*** NO SE REQUIERE VALIDACIÓN EN ISERIES");
            } else {
                logAuthentic = " Este caso require validacion por iSeries "+ logAuthentic +". ";
                logger.log(Level.INFO,() -> "La cuenta que se valida es: "+ cuenta);
                arrLogSciff = LogSciffsaldo.with(cuenta, saldoFin).ask();
                logRespuesta = logRespuesta + arrLogSciff[0];
                flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLogSciff[1]));

            }
            if (Integer.parseInt(flagRespuesta) > 0) {
                logRespuesta = logRespuesta +" ****VALIDACIÓN EN ISERIES FALLIDA**** \n";
                String finalLogRespuesta = logRespuesta;
                logger.log(Level.INFO,() -> finalLogRespuesta);
            } else {
                logRespuesta = logRespuesta +" ****VALIDACIÓN EN ISERIES SATISFACTORIA**** \n";
                String finalLogRespuesta1 = logRespuesta;
                logger.log(Level.INFO,() -> finalLogRespuesta1);
            }
            logger.log(Level.INFO,() ->"termina Authentic Debito");

        }catch (Exception e){
            String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
            String mensaje = "Fallo la preparacion de data ->" + e.getMessage();
            String comment = GeneralHelper.getCommentError(methodName, mensaje);

            screenshotHelper.takeScreenshot();
            helper.setStatusErrorOnProcedure(procedure, TypeError.OperationalError, comment);
        }





    }
    private String obtenerSufijo(String s) {
        String resultado = "";

        if (s.contains("AT5")) {
            resultado = "AT5";
        } else if (s.contains("AT6")) {
            resultado = "AT6";
        } else if (s.contains("AT7")) {
            resultado = "AT7";
        } else if (s.contains("AT8")) {
            resultado = "AT8";
        } else if (s.contains("AT9")) {
            resultado = "AT9";
        } else {
            logger.log(Level.INFO,() ->"el autorizador no es iSeries");
        }

        return resultado;
    }
}
