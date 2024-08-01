package framework.custom.procedures;

import framework.custom.procedures.fm_pattern_procedures.IPerformableProcedure;
import framework.custom.questions.authentic.AuthenticTransactionStatus;
import framework.custom.questions.authentic.AuthenticTransactionWithVentasStatus;
import framework.custom.questions.authentic.CreditCardLogs;
import framework.custom.questions.authentic.DebitCardLogs;
import framework.data.dynamicValues.DynamicValuesCustom;
import framework.data.entities.Procedure;
import framework.enums.TypeError;
import framework.helpers.ExecutionStatusHelper;
import framework.helpers.GeneralHelper;
import framework.helpers.ScreenshotHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static framework.custom.procedures.ProcedureConstants.*;

public class AuthenticValidation implements IPerformableProcedure {

    Logger logger=Logger.getLogger(AuthenticValidation.class.getName());
    private final Procedure procedure;
    private final ScreenshotHelper screenshotHelper;

    private String logAuthentic = "";
    private String logRespuesta = "";
    private int erroresAuthentic = 0;

    public AuthenticValidation(Procedure procedure, ScreenshotHelper screenshotHelper) {
        this.procedure = procedure;
        this.screenshotHelper=screenshotHelper;
    }

    @Override
    public void executeCustomProcedure() throws Exception {

        DynamicValuesCustom dmc = new DynamicValuesCustom();
        ExecutionStatusHelper helper = new ExecutionStatusHelper();

        String tipoTarjeta = dmc.GET_TIPO_TARJETA();
        String cajero = dmc.GET_CAJERO();
        String trxType = dmc.GET_TRX_TYPE();
        String actionCode = dmc.GET_ACTION_CODE();
        String authorisedBy = dmc.GET_AUTORISED_BY();
        String messageType = dmc.GET_MESSAGE_TYPE();
        String authenticInternalCode = dmc.GET_AUTHENTIC_INTERNAL_CODE();
        String trxTypeCosto = dmc.GET_TRX_TYPE_COSTO();
        String messageTypeCosto = dmc.GET_MESSAGE_TYPE_COSTO();
        String authenticInternalCodeCosto = dmc.GET_AUTHENTIC_INTERNAL_CODE_COSTO();
        String consultaCosto = dmc.GET_CONSULTA_DE_COSTO();
        String actionCodeCosto = dmc.GET_ACTION_CODE_COSTO();
        String authorisedByCosto = dmc.GET_AUTORISED_BY_COSTO();
        String actionCodePinForzado = dmc.GET_ACTION_CODE_PIN_FORZADO();
        String estadoActualPinforzado = dmc.GET_ESTADO_INICIAL_PINFORZADO();
        String estadoEsperadoPinForzado= dmc.GET_ESTADO_ESPERADO_PINFORZADO();
        String asegurarRetiro = dmc.GET_ASEGURAR_RETIRO();
        String trxTypeAr = dmc.GET_TRX_TYPE_AR();
        String actionCodeAr = dmc.GET_ACTION_CODE_AR();
        String authorisedByAr = dmc.GET_AUTORISED_BY_AR();
        String messageTypeAr = dmc.GET_MESSAGE_TYPE_AR();
        String authenticInternalCodeAr = dmc.GET_AUTHENTIC_INTERNAL_CODE_AR();


        String ventasATM = dmc.GET_VENTAS_ATM();
        String trxTypeVentas = dmc.GET_TRX_TYPE_VENTAS();
        String actionCodeVentas = dmc.GET_ACTION_CODE_VENTAS();
        String authorisedByVentas = dmc.GET_AUTORISED_BY_VENTAS();
        String messageTypeVentas = dmc.GET_MESSAGE_TYPE_VENTAS();
        String authenticInternalCodeVentas = dmc.GET_AUTHENTIC_INTERNAL_CODE_VENTAS();

        String[] authArray = AuthenticTransactionStatus.
                with(cajero,trxType,messageType,authenticInternalCode).ask();
        String[] authArrayCC;
        String[] authArrayPinForzado;

        logger.log(Level.INFO,() ->"entró a validación authentic");
        logger.log(Level.INFO,() ->"Estado Actual Pin Forzado: "+ estadoActualPinforzado);
        logger.log(Level.INFO,() ->"Estado Esperado Pin Forzado: "+ estadoEsperadoPinForzado);

	    if (authArray[0].equals(EMPTY)) {
            logAuthentic = logAuthentic +"La consulta de la transaccion en el Sistema Authentic no ha generado registros. Transaccion no realizada o hecha fuera de los parámetros de consulta. ";
            logAuthentic = logAuthentic + QUERY_SQL + authArray[5];
            erroresAuthentic = erroresAuthentic +1;
        } else {
            logAuthentic = logAuthentic + VALOR_TRX_TYPE + authArray[0] +". ";
            logAuthentic = logAuthentic + VALOR_MESSAGE_TYPE + authArray[1] +". ";
            logAuthentic = logAuthentic + VALOR_AUTHENTIC_INTERNAL_CODE + authArray[2] +". ";

            if (actionCode.equals(authArray[3])) {
                logAuthentic = logAuthentic + VALOR_ACTION_CODE + authArray[3] +". ";
            } else {
                logAuthentic = logAuthentic + INFORMATION_ACTION_CODE + actionCode + GOT_AUTHENTIC_FROM + authArray[3] +". \n";
                erroresAuthentic = erroresAuthentic +1;
            }

            if (authorisedBy.equals(authArray[4])) {
                logAuthentic = logAuthentic + VALOR_AUTHORIZED_BY + authArray[4] +". \n";
            } else {
                logAuthentic = logAuthentic + UNEXPECTED_VALUE + authorisedBy + GOT_AUTHENTIC_FROM + authArray[4] +". \n";
                erroresAuthentic = erroresAuthentic +1;
            }
        }

        //************** Validación si hay consulta de costo
        if (consultaCosto.equals(STATUS_S)) {
            authArrayCC = AuthenticTransactionStatus.
                    with(cajero,trxTypeCosto,messageTypeCosto,authenticInternalCodeCosto).ask();
            logAuthentic = logAuthentic +" Validacion de datos de consulta de costo: ";

            if (authArrayCC[0].equals(EMPTY)) {
                logAuthentic = logAuthentic +"La transaccion de consulta de costo en el Sistema Authentic no ha sido encontrada. Registro inexistente o query hecho fuera de los parametros de consulta. ";
                logAuthentic = logAuthentic + QUERY_SQL + authArrayCC[5];
                erroresAuthentic = erroresAuthentic +1;
            } else {
                logAuthentic = logAuthentic + VALOR_TRX_TYPE + authArrayCC[0];
                logAuthentic = logAuthentic + VALOR_MESSAGE_TYPE +authArrayCC[1];
                logAuthentic = logAuthentic + VALOR_AUTHENTIC_INTERNAL_CODE +authArrayCC[0];

                if (actionCodeCosto.equals(authArrayCC[3])) {
                    logAuthentic = logAuthentic + VALOR_ACTION_CODE  + authArrayCC[3] +". ";
                } else {
                    logAuthentic = logAuthentic + INFORMATION_ACTION_CODE + actionCodeCosto + GOT_AUTHENTIC_FROM + authArrayCC[3] +". ";
                    erroresAuthentic = erroresAuthentic +1;
                }

                if(authorisedByCosto.equals(authArrayCC[4])) {
                    logAuthentic = logAuthentic + VALOR_AUTHORIZED_BY + authArrayCC[4] +". \n";
                } else {
                    logAuthentic = logAuthentic + UNEXPECTED_VALUE + authorisedByCosto + GOT_AUTHENTIC_FROM + authArrayCC[4] +". \n";
                    erroresAuthentic = erroresAuthentic +1;
                }
            }
        }
        //DCC

        if (tipoTarjeta.equals(CARD_TYPE_DCC)) {
            String trxTypeDCC = "3";
            String messageTypeDCC = "11";
            String authenticInternalCodeDCC = "86";
            String actionCodeDCC = "00";
            String authorisedByDCC = "CIBC_ICH/CIBC_IAP";
            authArrayCC = AuthenticTransactionStatus.
                    with(cajero,trxTypeDCC,messageTypeDCC,authenticInternalCodeDCC).ask();
            logAuthentic = logAuthentic +" Validacion de datos de consulta de oferta DCC: ";

            if (authArrayCC[0].equals(EMPTY)) {
                logAuthentic = logAuthentic +"La transaccion de consulta de oferta DCC en el Sistema Authentic no ha sido encontrada. Registro inexistente o query hecho fuera de los parametros de consulta. ";
                logAuthentic = logAuthentic + QUERY_SQL + authArrayCC[5];
                erroresAuthentic = erroresAuthentic +1;
            } else {
                logAuthentic = logAuthentic + VALOR_TRX_TYPE +authArrayCC[0];
                logAuthentic = logAuthentic + VALOR_MESSAGE_TYPE +authArrayCC[1];
                logAuthentic = logAuthentic + VALOR_AUTHENTIC_INTERNAL_CODE +authArrayCC[2];

                if (actionCodeDCC.equals(authArrayCC[3])) {
                    logAuthentic = logAuthentic + VALOR_ACTION_CODE + authArrayCC[3] +". ";
                } else {
                    logAuthentic = logAuthentic + INFORMATION_ACTION_CODE + actionCodeDCC + GOT_AUTHENTIC_FROM + authArrayCC[3] +". ";
                    erroresAuthentic = erroresAuthentic +1;
                }

                if(authorisedByDCC.equals(authArrayCC[4])) {
                    logAuthentic = logAuthentic + VALOR_AUTHORIZED_BY + authArrayCC[4] +". \n";
                } else {
                    logAuthentic = logAuthentic + UNEXPECTED_VALUE + authorisedByDCC + GOT_AUTHENTIC_FROM + authArrayCC[4] +". \n";
                    erroresAuthentic = erroresAuthentic +1;
                }
            }
        }
//
        //Asegurar Retiro

        if (asegurarRetiro.equals(STATUS_S)) {

            authArrayCC = AuthenticTransactionStatus.
                    with(cajero,trxTypeAr,messageTypeAr,authenticInternalCodeAr).ask();
            logAuthentic = logAuthentic +" Validacion de datos de consulta de oferta AR: ";

            if (authArrayCC[0].equals(EMPTY)) {
                logAuthentic = logAuthentic +"La transaccion de consulta de oferta AR en el Sistema Authentic no ha sido encontrada. Registro inexistente o query hecho fuera de los parametros de consulta. ";
                logAuthentic = logAuthentic + QUERY_SQL + authArrayCC[5];
                erroresAuthentic = erroresAuthentic +1;
            } else {
                logAuthentic = logAuthentic + VALOR_TRX_TYPE +authArrayCC[0];
                logAuthentic = logAuthentic + VALOR_MESSAGE_TYPE +authArrayCC[1];
                logAuthentic = logAuthentic + VALOR_AUTHENTIC_INTERNAL_CODE +authArrayCC[2];

                if (actionCodeAr.equals(authArrayCC[3])) {
                    logAuthentic = logAuthentic + VALOR_ACTION_CODE + authArrayCC[3] +". ";
                } else {
                    logAuthentic = logAuthentic + INFORMATION_ACTION_CODE + actionCodeAr + GOT_AUTHENTIC_FROM + authArrayCC[3] +". ";
                    erroresAuthentic = erroresAuthentic +1;
                }

                if(authorisedByAr.equals(authArrayCC[4])) {
                    logAuthentic = logAuthentic + VALOR_AUTHORIZED_BY + authArrayCC[4] +". \n";
                } else {
                    logAuthentic = logAuthentic + UNEXPECTED_VALUE + authorisedByAr + GOT_AUTHENTIC_FROM + authArrayCC[4] +". \n";
                    erroresAuthentic = erroresAuthentic +1;
                }
            }
        }
//
        //VentasATM

        if (ventasATM.equals(STATUS_S)) {


            authArrayCC = AuthenticTransactionWithVentasStatus.
                    with(cajero,trxTypeVentas,messageTypeVentas,authenticInternalCodeVentas,TRX_QUALIFIER_VENTAS).ask();
            logAuthentic = logAuthentic +" Validacion de datos de oferta de venta por ATM: ";

            if (authArrayCC[0].equals(EMPTY)) {
                logAuthentic = logAuthentic +"La transaccion de oferta de venta por ATM en el Sistema Authentic no ha sido encontrada. Registro inexistente o query hecho fuera de los parametros de consulta. ";
                logAuthentic = logAuthentic + QUERY_SQL + authArrayCC[5];
                erroresAuthentic = erroresAuthentic +1;
            } else {
                logAuthentic = logAuthentic + VALOR_TRX_TYPE +authArrayCC[0];
                logAuthentic = logAuthentic + VALOR_MESSAGE_TYPE +authArrayCC[1];
                logAuthentic = logAuthentic + VALOR_AUTHENTIC_INTERNAL_CODE +authArrayCC[2];

                if (actionCodeVentas.equals(authArrayCC[3])) {
                    logAuthentic = logAuthentic + VALOR_ACTION_CODE + authArrayCC[3] +". ";
                } else {
                    logAuthentic = logAuthentic + INFORMATION_ACTION_CODE + actionCodeVentas + GOT_AUTHENTIC_FROM + authArrayCC[3] +". ";
                    erroresAuthentic = erroresAuthentic +1;
                }

                if(authorisedByVentas.equals(authArrayCC[4])) {
                    logAuthentic = logAuthentic + VALOR_AUTHORIZED_BY + authArrayCC[4] +". \n";
                } else {
                    logAuthentic = logAuthentic + UNEXPECTED_VALUE + authorisedByVentas + GOT_AUTHENTIC_FROM + authArrayCC[4] +". \n";
                    erroresAuthentic = erroresAuthentic +1;
                }
            }
        }

        //Validación para PIN Forzado (si EstadoActualPinforzado = I el caso exigirá cambio de pin durante la transacción)
        if (estadoActualPinforzado.equals(STATUS_I) && !actionCodePinForzado.equals(EMPTY)) {
                authArrayPinForzado = AuthenticTransactionStatus.
                        with(cajero,"4","11","70").ask();
                logAuthentic = logAuthentic +" Validacion de datos de cambio de pin forzado: ";

                if (authArrayPinForzado[0].equals(EMPTY)) {
                    logAuthentic = logAuthentic +"La transaccion de cambio de pin forzado en el Sistema Authentic no ha sido encontrada. Registro inexistente o query hecho fuera de los parametros de consulta. ";
                    logAuthentic = logAuthentic + QUERY_SQL + authArrayPinForzado[5];
                    erroresAuthentic = erroresAuthentic +1;
                } else {
                    logAuthentic = logAuthentic + VALOR_TRX_TYPE + authArrayPinForzado[0] +". ";
                    logAuthentic = logAuthentic + VALOR_MESSAGE_TYPE + authArrayPinForzado[1] +". ";
                    logAuthentic = logAuthentic + VALOR_AUTHENTIC_INTERNAL_CODE + authArrayPinForzado[2] +". ";

                    if (actionCodePinForzado.equals(authArrayPinForzado[3])) {
                        logAuthentic = logAuthentic + VALOR_ACTION_CODE + authArrayPinForzado[3] +". ";
                    } else {
                        logAuthentic = logAuthentic + INFORMATION_ACTION_CODE + actionCodePinForzado + GOT_AUTHENTIC_FROM + authArrayPinForzado[3] +". ";
                        erroresAuthentic = erroresAuthentic +1;
                    }

                    if ((authArrayPinForzado[4].equals("iSeries_ATM_ICH/iSeries_IAP_AT6"))
                            || (authArrayPinForzado[4].equals("authentic")
                            && authArrayPinForzado [3].equals("117"))) {
                        logAuthentic = logAuthentic + VALOR_AUTHORIZED_BY + authArrayPinForzado[4] +". \n";
                    } else {
                        logAuthentic = logAuthentic + UNEXPECTED_VALUE + "authentic" + GOT_AUTHENTIC_FROM + authArrayPinForzado[4] +". \n";
                        erroresAuthentic = erroresAuthentic +1;
                    }
                }

        }

        if (erroresAuthentic > 0) {
            logAuthentic = logAuthentic +" **** VALIDACION AUTHENTIC FALLIDA **** . ";
        } else {
            logAuthentic = logAuthentic +" **** VALIDACION AUTHENTIC OK **** . ";
        }

        if (authArray[4].contains(ISERIES)) {
            if (tipoTarjeta.equals(CARD_TYPE_DEBIT) ){
                logAuthentic = " Este caso require validacion por iSeries "+ logAuthentic +". ";
                logger.log(Level.INFO,() ->authArray[4]);
                String sufijo = obtenerSufijo(authArray[4]);
                logger.log(Level.INFO,() -> SUFIJO + sufijo);
                String[] arreglo = DebitCardLogs.with(sufijo).ask();

                logAuthentic = arreglo[0] + logAuthentic;
                erroresAuthentic = erroresAuthentic + Integer.parseInt(arreglo[1]);
            }

            if (tipoTarjeta.equals(CARD_TYPE_CREDIT) || tipoTarjeta.equals(CARD_TYPE_CREDIT_OUTSERVICE)) {

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

//			    ****VALIDACION DE FIRST DATA
                if (!actionCode.equals("8500")) {
                    logger.log(Level.INFO,() ->"Validación por First Data para: "+ trxType);
                    logRespuesta = logRespuesta +"Este caso realiza validación en FirstData";
                    //TODO pending FirstData validations
                    //Arreglo = validacionFisrtsData();
                    //logAuthentic = Arreglo[0] + logAuthentic;
                    //erroresAuthentic = erroresAuthentic + Integer.parseInt(Arreglo[1]);
                }
            }

            if (tipoTarjeta.equals(CARD_TYPE_PREPAGO)) {

                logger.log(Level.INFO,() ->"Entro a validación PREPAGO");
                logAuthentic = "Este caso require validacion por iSeries "+ logAuthentic +". ";
                String sufijo = obtenerSufijo(authArray[4]);

                //***** NO SE HACE VALIDACIONES EN ISERIES
                logger.log(Level.INFO,() -> SUFIJO + sufijo);
                String[] arreglo = CreditCardLogs.with(sufijo).ask();
                logger.log(Level.INFO,() ->"llego de ValidacionIseriesCredito");
                logAuthentic = arreglo[0] + logAuthentic;
                erroresAuthentic = erroresAuthentic + Integer.parseInt(arreglo[1]);
                logger.log(Level.INFO,() ->"paso de ValidacionIseriesCredito");

                //****VALIDACION DE FIRST DATA
                logger.log(Level.INFO,() ->"Validación por First Data para: "+ trxType);
                logRespuesta = logRespuesta +"Este caso realiza validación en FirstData";
                //TODO pending FirstData validations
                //Arreglo = validacionFisrtsData();
                //logAuthentic = Arreglo[0] + logAuthentic;
                //erroresAuthentic = erroresAuthentic + Integer.parseInt(Arreglo[1]);
                //****FIN DE VALIDACION DE FIRST DATA
            }
        }else {
            logAuthentic = "Transaccion no autorizada por iSeries "+ logAuthentic +". ";
        }


        String logResumen = logAuthentic.replaceAll("SELECT.*WHERE ROWNUM = 1","");
        if (erroresAuthentic > 0) {
            String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
            String comment = GeneralHelper.getCommentError(methodName, logResumen);

            screenshotHelper.takeScreenshot();
            helper.setStatusErrorOnProcedure(procedure, TypeError.OperationalError, comment);
        }else{
            helper.setStatusLogOkProcedure(procedure,logResumen);
        }

        logger.log(Level.INFO,() ->"LOG_AUTHENTIC "+ logAuthentic);
        logger.log(Level.INFO,() ->"ERRORES TOTALES = "+ erroresAuthentic);
        SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yy HH:mm:ss a");
        String fechaFin = fecha.format(new Date());
        logger.log(Level.INFO,() ->"Fecha Final Ejecucion "+ fechaFin);
        logger.log(Level.INFO,() ->"*****************************************************************************************");

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
