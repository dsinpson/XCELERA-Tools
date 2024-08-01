package framework.custom.procedures;

import framework.custom.procedures.fm_pattern_procedures.IPerformableProcedure;
import framework.custom.questions.authentic.AtmStatus;
import framework.custom.questions.authentic.EnvironmentStatus;
import framework.data.dynamicValues.DynamicValuesCustom;
import framework.data.entities.Procedure;
import framework.enums.TypeError;
import framework.helpers.ExecutionStatusHelper;
import framework.helpers.GeneralHelper;
import framework.helpers.ScreenshotHelper;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static framework.custom.procedures.ProcedureConstants.*;

public class AuthenticEnvironmentValidation implements IPerformableProcedure {

    DynamicValuesCustom dmc = new DynamicValuesCustom();
    ExecutionStatusHelper helper = new ExecutionStatusHelper();
    Logger logger=Logger.getLogger(AuthenticEnvironmentValidation.class.getName());

    private final Procedure procedure;
    private final ScreenshotHelper screenshotHelper;

    private String mensaje;
    private String iap;
    private String logAmbienAuthentic = "";
    private Object[] resultadoAmbientes;
    private int erroresAmbienAuthentic = 0;

    public AuthenticEnvironmentValidation(Procedure procedure, ScreenshotHelper screenshotHelper){
        this.procedure = procedure;
        this.screenshotHelper=screenshotHelper;
        mensaje = "";
        iap = "";
    }

    @Override
    public void executeCustomProcedure() throws Exception {

        Date fechaIni = new Date();

        String tipoTransaccion = dmc.GET_TIPO_TRANSACCION();
        String casoAplica = dmc.GET_CASO_APLICA();
        String tipoTarjeta = dmc.GET_TIPO_TARJETA();
        String consultaDeCosto = dmc.GET_CONSULTA_DE_COSTO();
        String estadoActualPinforzado = dmc.GET_ESTADO_INICIAL_PINFORZADO();
        String asegurarRetiro = dmc.GET_ASEGURAR_RETIRO();
        String idCajero = dmc.GET_CAJERO();



        logger.log(Level.INFO,() ->"Fecha inicial Ejecucion "+ fechaIni);
        logger.log(Level.INFO,() ->"* * * CASO PARA EL QUE APLICA ESTA PRUEBA: "+ casoAplica);
        logger.log(Level.INFO,() ->"En validarAmbientes TipoTransaccion "+ tipoTransaccion);
        logger.log(Level.INFO,() ->"Tipo Tarjeta: "+ tipoTarjeta);

        //***** Validación del estado del cajero que va a lanzar la transaccion
        Object[] resultadoComunicCajero = AtmStatus.with(idCajero).ask();

		if ((boolean) resultadoComunicCajero[0]) {
            logger.log(Level.INFO,() ->"El resultado de validar el cajero es: "+ resultadoComunicCajero[0]);
			logAmbienAuthentic = logAmbienAuthentic +"Resultado validacion cajero: "+ resultadoComunicCajero[0] +". "+ resultadoComunicCajero[1] +". ";
		} else {
            logger.log(Level.INFO,() ->"El resultado de validar el cajero es: Sin servicio");
			logAmbienAuthentic = logAmbienAuthentic +"Resultado validacion cajero: "+ resultadoComunicCajero[0] +". "+ resultadoComunicCajero[1] +". ";
			erroresAmbienAuthentic = erroresAmbienAuthentic +1;
			mensaje = "ESTADO DE CAJERO **FUERA DE SERVICIO*";
            logger.log(Level.INFO,() -> mensaje);
            logger.log(Level.INFO,() ->"TERMINA");
		}
        //****FIN VALIDACIÓN CAJERO

        //***** Communications services validation start
        if (!tipoTransaccion.equals(TRANS_TYPE_FRONT) && !tipoTarjeta.equals(TRANS_TYPE_OTRASREDES)) {
            logger.log(Level.INFO,() ->"Validando servicios comunicacionales en ambiente");

            //****Assignation iap according transaction start
            if (tipoTransaccion.equals(TRANS_TYPE_RETIRO_CUENTA)
                    || tipoTransaccion.equals(TRANS_TYPE_CAMBIO_DE_CLAVE)
                    || tipoTransaccion.equals(TRANS_TYPE_ULTIMOS_MOVIMIENTOS)
                    || tipoTransaccion.equals(TRANS_TYPE_AHORRO_A_LA_MANO)
                    || tipoTransaccion.equals(TRANS_TYPE_TOPES_PERSONALIZADOS)
                    || tipoTransaccion.equals(TRANS_TYPE_TOPES_CANAL)
                    || tipoTransaccion.equals(TRANS_TYPE_TOPES_NO_DATA)
                    || tipoTransaccion.equals(TRANS_TYPE_AHORRO_NO_DATA)) {
                iap = "iSeries_IAP_AT7";
            } else if (tipoTransaccion.equals(TRANS_TYPE_CONSULTA_DE_SALDO)) {
                iap = "iSeries_IAP_AT6";
            } else if (tipoTransaccion.equals(TRANS_TYPE_TRANSFERENCIAS)) {
                iap = "iSeries_IAP_AT5";
            }
            else if (tipoTransaccion.equals(TRANS_TYPE_PAGOS_TD_CTA_RECAUDADORA)
                        || tipoTransaccion.equals(TRANS_TYPE_PAGO_TD_AHORROS_CORRIENTE)
                        || tipoTransaccion.equals(TRANS_TYPE_PAGO_TARJETA_EMPRESARIAL)
                        || tipoTransaccion.equals(TRANS_TYPE_PAGO_CODIGO_DE_BARRAS)
                        || tipoTransaccion.equals(TRANS_TYPE_PAGO_CONVENIO_MULTI)
                        || tipoTransaccion.equals(TRANS_TYPE_PAGO_TARJETA_EMPRESARIAL_MULTI)
                        || tipoTransaccion.equals(TRANS_TYPE_PAGO_CODIGO_DE_BARRAS_MULTI)
                        || tipoTransaccion.equals(TRANS_TYPE_PAGO_CUENTA_RECAUDADORA_MULTI)) {
                iap = "iSeries_IAP_AT8";
            } else if (tipoTarjeta.equals(CARD_TYPE_CREDIT)
                        || tipoTarjeta.equals(CARD_TYPE_PREPAGO)
                        || tipoTarjeta.equals(CARD_TYPE_CREDIT_OUTSERVICE)) {
                iap = "iSeries_IAP_AT9";
            } else if (tipoTarjeta.equals(CARD_TYPE_DCC)) {
                iap = "CIBC_IAP";
            }
            //****Assignation iap according transaction finished

            logger.log(Level.INFO,() ->"IAP "+ iap);

            //***** Communications services validation start
            if (!tipoTransaccion.equals(TRANS_TYPE_CUENTAS_INSCRITAS) && (!tipoTarjeta.equals(CARD_TYPE_DCC))){

                resultadoAmbientes = EnvironmentStatus.
                                        with("%sbmdecst02%"+"iSerieS","%sbmdecst02%"+"iSeries_ATM_ICH", "%sbmdecst02%"+iap).
                                        ask();
                logger.log(Level.INFO,() -> VALIDATION_ENVIRONMENT_RESULT_IS + resultadoAmbientes[0]);

                if ((boolean) resultadoAmbientes[0]) {
                    logAmbienAuthentic = logAmbienAuthentic +"El proceso iSeries-ATM y las conexiones "+ iap +OPERATIONAL_OK;
                } else {
                    logAmbienAuthentic = logAmbienAuthentic +"El proceso iSeries-ATM o alguna de las conexiones "+ iap + OFFLINE + resultadoAmbientes[1];
                    erroresAmbienAuthentic = erroresAmbienAuthentic +1;
                    mensaje = INACTIVE_SERVICES;
                    logger.log(Level.WARNING,() -> mensaje);
                    String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
                    String comment="El servidor " + iap + " se encuentra desconectado";
                    String commentFinal = GeneralHelper.getCommentError(methodName, comment);
                    helper.setStatusErrorOnProcedure(procedure, TypeError.OperationalError, commentFinal);
                    throw new Exception(commentFinal);
                }

                if (consultaDeCosto.equals(STATUS_S)
                        && !tipoTarjeta.equals(CARD_TYPE_CREDIT)
                        && !tipoTarjeta.equals(CARD_TYPE_CREDIT_OUTSERVICE)) {
                    iap = "iSeries_IAP_AT6";
                    validateProcces("iSerieS","iSeries_ATM_ICH",iap);
                }

                //Servidor de asegurar retiro
                if (asegurarRetiro.equals(STATUS_S)) {
                    iap = "BrokerWS_IAP";
                    validateProcces("BrokerWS", "BrokerWS_ICH", iap);
                }

                //***** Comprueba el estado de los servidores de notificaciones *******
                if (tipoTransaccion.equals(TRANS_TYPE_CAMBIO_DE_CLAVE) || estadoActualPinforzado.equals(STATUS_I)) {
                    Object[] resultadoNotificaciones = EnvironmentStatus.
                                                        with("%sbmdecst02%"+"iSeries", "%sbmdecst02%"+"iSeries_ICH","%sbmdecst02%"+"iSeries_IAP_NF2").
                                                        ask();
                    logger.log(Level.INFO,() -> VALIDATION_NOTIFICATION_RESULT_IS + resultadoNotificaciones[0]);

                    if ((boolean) resultadoNotificaciones[0]) {
                        logAmbienAuthentic = logAmbienAuthentic +"El proceso iSeries y las conexiones iSeries_ICH" +
                                " se encuentran operativos. ";
                    } else {
                        logAmbienAuthentic = logAmbienAuthentic +"El proceso iSeries o alguna de las conexiones " +
                                "iSeries_ICH se encuentran fuera de linea. Detalle: "+ resultadoNotificaciones[1];
                        erroresAmbienAuthentic = erroresAmbienAuthentic +1;
                        mensaje = "SERVIDOR NOTIFICACIÓN* *INACTIVO*";
                        logger.log(Level.INFO,() -> mensaje);
                    }
                }
            }
            else if (tipoTarjeta.equals(CARD_TYPE_DCC)) {
                Object[] resultadoNotificaciones = EnvironmentStatus.
                                                    with("%sbmdecst02%"+"CIBC", "%sbmdecst02%"+"CIBC_ICH", "%sbmdecst02%"+iap).
                                                    ask();
                logger.log(Level.INFO,() -> VALIDATION_NOTIFICATION_RESULT_IS + resultadoNotificaciones[0]);

                if ((boolean) resultadoNotificaciones[0]) {
                    logAmbienAuthentic = logAmbienAuthentic +"El proceso CIBC y las conexiones CIBC_ICH se " +
                            "encuentran operativos. ";
                } else {
                    logAmbienAuthentic = logAmbienAuthentic +"El proceso CIBC o alguna de las conexiones CIBC_ICH" +
                            " se encuentran fuera de linea. Detalle: "+ resultadoNotificaciones[1];
                    erroresAmbienAuthentic = erroresAmbienAuthentic +1;
                    mensaje = "SERVIDOR CIBC* *INACTIVO*";
                    logger.log(Level.INFO,() ->mensaje);
                }
            }
        }
        //***** Communications services validation finished
        logger.log(Level.INFO,() -> mensaje);

        //Trampa
        erroresAmbienAuthentic = 0;
        if (erroresAmbienAuthentic > 0) {
            String commentIni = mensaje +logAmbienAuthentic;
            String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
            String comment = GeneralHelper.getCommentError(methodName,  commentIni);
            screenshotHelper.takeScreenshot();
            helper.setStatusErrorOnProcedure(procedure, TypeError.OperationalError, comment);
        }else {
            helper.setStatusLogOkProcedure(procedure,logAmbienAuthentic);
        }
        //******** Fin de automatización de IAP dependiendo la transacción
    }

    private void validateProcces(String process,String interchange, String iap) throws Exception{
        resultadoAmbientes =  EnvironmentStatus.
                with("%sbmdecst02%"+process,"%sbmdecst02%"+interchange, "%sbmdecst02%"+iap).
                ask();
        logger.log(Level.INFO,() -> VALIDATION_ENVIRONMENT_RESULT_IS + resultadoAmbientes[0]);

        if ((boolean) resultadoAmbientes[0]) {
            logAmbienAuthentic = logAmbienAuthentic +"El proceso " + interchange + " y las conexiones "+ iap + OPERATIONAL_OK;
        } else {


            logAmbienAuthentic = logAmbienAuthentic +"El proceso " + interchange + " o alguna de las conexiones "+ iap + OFFLINE + resultadoAmbientes[1];
            erroresAmbienAuthentic = erroresAmbienAuthentic +1;
            mensaje = INACTIVE_SERVICES;
            logger.log(Level.INFO,() -> mensaje);
        }
    }
}
