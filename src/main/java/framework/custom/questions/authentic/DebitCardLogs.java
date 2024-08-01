package framework.custom.questions.authentic;

import framework.custom.questions.IQuestion;
import framework.custom.questions.iseries.*;
import framework.data.dynamicValues.DynamicValuesCustom;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DebitCardLogs implements IQuestion<String[]> {

    Logger logger=Logger.getLogger(DebitCardLogs.class.getName());

    private String sufijo;

    public DebitCardLogs(String sufijo) {
        this.sufijo = sufijo;
    }

    public static DebitCardLogs with(String sufijo){return new DebitCardLogs(sufijo);}

    @Override
    public String[] ask() throws SQLException {
        DynamicValuesCustom dmc = new DynamicValuesCustom();
        String tipoTransaccion = dmc.GET_TIPO_TRANSACCION();
        String[] arrLogComff;
        String[] arrLogSciff;
        String[] arrLogSti;
        String[] arrLoglgaen;
        String[] arrLogcnip;
        String flagRespuesta = "0";
        String logRespuesta = "";
        String cajero = dmc.GET_CAJERO();
        String cuenta = dmc.GET_NUMERO_CUENTA();
        String codError = dmc.GET_CODIGO_ERROR();
        String transaccion = dmc.GET_TRANSACCION();
        String trxCambioClave = dmc.GET_TRX_CAMBIO_CLAVE();
        String saldoFin = dmc.GET_SALDOFINAL();
        String tarjeta = dmc.GET_NUMERO_TARJETA();
        String nroIde = dmc.GET_NRO_IDENTIFICACION();
        String consultaDeCosto = dmc.GET_CONSULTA_DE_COSTO();
        String cuentaDestino = dmc.GET_CUENTA_DESTINO();
        String saldoFinalCtaDestino = dmc.GET_SALDO_CUENTA_DESTINO();
        String codErrorDestino = dmc.GET_CODIGO_ERROR_DESTINO();
        String transaccionDestino = dmc.GET_TRANSACCION_DESTINO();
        String cantValidaciones = dmc.GET_CANTIDAD_VALIDACIONES();
        String estadoActualPinforzado = dmc.GET_ESTADO_INICIAL_PINFORZADO();
        String estadoEsperadoPinforzado = dmc.GET_ESTADO_ESPERADO_PINFORZADO();


        logger.log(Level.INFO,() ->tipoTransaccion);
        logger.log(Level.INFO,() ->saldoFin);
        logger.log(Level.INFO,() ->transaccionDestino);
        logger.log(Level.INFO,() ->estadoActualPinforzado);
        logger.log(Level.INFO,() ->cantValidaciones);
        logger.log(Level.INFO,() ->estadoEsperadoPinforzado);

        if (cantValidaciones.equals("1") || cantValidaciones.equals("2")) {
            if (tipoTransaccion.equals("AHORRO_A_LA_MANO")
                    || tipoTransaccion.equals("AHORRO_NO_DATA")) {
                //**************************** Validación LOG ConsultaComfflgon2**********************************
                arrLogComff = LogCommfflg.
                        with(cajero, cuenta, codError, transaccion, tipoTransaccion).ask();
                logRespuesta = logRespuesta + arrLogComff[0];
                flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLogComff[1]));

                //**************************** Validación LOG ConsultaSciffsaldo **********************************
                if (!codError.equals("5053")) {
                    arrLogSciff = LogSciffsaldo.with(cuenta, saldoFin).ask();
                    logRespuesta = logRespuesta + arrLogSciff[0];
                    flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLogSciff[1]));
                }

                //**************************** Validación LOG ConsultaSTI **********************************
                arrLogSti = LogSTI.
                        with(cajero, codError, transaccion, sufijo, tipoTransaccion).ask();
                logRespuesta = logRespuesta + arrLogSti[0];
                flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLogSti[1]));
            } else if (tipoTransaccion.equals("RETIRO_CUENTA")
                    || tipoTransaccion.equals("CONSULTA_DE_SALDO")
                    || tipoTransaccion.equals("ULTIMOS_MOVIMIENTOS")
                    || tipoTransaccion.equals("TRANSFERENCIAS")
                    || tipoTransaccion.equals("PAGOS_TD_CTA_RECAUDADORA")
                    || tipoTransaccion.equals("PAGO_TD_AHORROS_CORRIENTE")
                    || tipoTransaccion.equals("PAGO_TARJETA_EMPRESARIAL")
                    || tipoTransaccion.equals("PAGO_CODIGO_DE_BARRAS")
                    || tipoTransaccion.equals("TOPES_PERSONALIZADOS")
                    || tipoTransaccion.equals("TOPES_CANAL")
                    || tipoTransaccion.equals("CONSIGNACION_MULTI")
                    || tipoTransaccion.equals("PAGO_CONVENIO_MULTI")
                    || tipoTransaccion.equals("PAGO_TARJETA_EMPRESARIAL_MULTI")
                    || tipoTransaccion.equals("PAGO_CODIGO_DE_BARRAS_MULTI")
                    || tipoTransaccion.equals("PAGO_CUENTA_RECAUDADORA_MULTI")
                    || tipoTransaccion.equals("TOPES_NO_DATA")) {

                //**************************** Validación LOG ConsultaComfflgon2**********************************
                if (tipoTransaccion.equals("PAGOS_TD_CTA_RECAUDADORA")
                        || tipoTransaccion.equals("PAGO_TD_AHORROS_CORRIENTE")
                        || tipoTransaccion.equals("PAGO_TARJETA_EMPRESARIAL")
                        || tipoTransaccion.equals("PAGO_CODIGO_DE_BARRAS")
                        || tipoTransaccion.equals("PAGO_CONVENIO_MULTI")
                        || tipoTransaccion.equals("PAGO_TARJETA_EMPRESARIAL_MULTI")
                        || tipoTransaccion.equals("PAGO_CODIGO_DE_BARRAS_MULTI")
                        || tipoTransaccion.equals("PAGO_CUENTA_RECAUDADORA_MULTI")) {
                    cuenta = dmc.GET_CODIGO_CONVENIO();
                }
                arrLogComff = LogCommfflg.
                        with(cajero, cuenta, codError, transaccion, tipoTransaccion).ask();
                logRespuesta = logRespuesta + arrLogComff[0];
                flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLogComff[1]));
                cuenta = dmc.GET_NUMERO_CUENTA();

                //**************************** Validación LOG ConsultaSciffsaldo **********************************
                arrLogSciff = LogSciffsaldo.with(cuenta, saldoFin).ask();
                logRespuesta = logRespuesta + arrLogSciff[0];
                flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLogSciff[1]));

                //**************************** Validación LOG ConsultaSTI **********************************
                arrLogSti = LogSTI.
                        with(cajero, codError, transaccion, sufijo, tipoTransaccion).ask();
                logRespuesta = logRespuesta + arrLogSti[0];
                flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLogSti[1]));

                //**************************** Validación LOG ConsultaPccfflgaen **********************************
                if (dmc.GET_ALERTA().equals("S")) {
                    arrLoglgaen = LogPccfflgaen.with(nroIde, codError).ask();
                    String lg = (!arrLoglgaen[0].isEmpty()) ? arrLoglgaen[0] : "";
                    logRespuesta = logRespuesta + lg;
                    flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLoglgaen[1]));
                }

                //***************************Validacion Estado cliente Pin Forzado*******************************
                if (estadoActualPinforzado.equals("X") || estadoActualPinforzado.equals("I")) {
                    arrLogcnip = LogMatffcnip.with(tarjeta, estadoEsperadoPinforzado).ask();
                    logRespuesta = logRespuesta + arrLogcnip[0];
                    flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLogcnip[1]));
                }
            } else if (tipoTransaccion.equals("CAMBIO_DE_CLAVE")) {
                //**************************** Validación LOG ConsultaComfflgon2**********************************
                arrLogComff = LogCommfflg.
                        with(cajero, cuenta, codError, transaccion, tipoTransaccion).ask();
                logRespuesta = logRespuesta + arrLogComff[0];
                flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLogComff[1]));

                //**************************** Validación LOG ConsultaSciffsaldo **********************************
                arrLogSciff = LogSciffsaldo.with(cuenta, saldoFin).ask();
                logRespuesta = logRespuesta + arrLogSciff[0];
                flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLogSciff[1]));

                //**************************** Validación LOG ConsultaSTI **********************************
                arrLogSti = LogSTI.
                        with(cajero, codError, trxCambioClave, sufijo, tipoTransaccion).ask();
                logRespuesta = logRespuesta + arrLogSti[0];
                flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLogSti[1]));

                if (dmc.GET_ALERTA().equals("S")) {
                    arrLoglgaen = LogPccfflgaen.with(nroIde, codError).ask();
                    String lg = (!arrLoglgaen[0].isEmpty()) ? arrLoglgaen[0] : "";
                    logRespuesta = logRespuesta + lg;
                    flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLoglgaen[1]));
                }
            }
        }
        if (cantValidaciones.equals("2") || cantValidaciones.equals("3") || consultaDeCosto.equals("S")) {

                if (tipoTransaccion.equals("PAGO_TARJETA_EMPRESARIAL_MULTI")
                        && !codErrorDestino.equals("8146")
                        && !codErrorDestino.equals("8145")
                        && !codErrorDestino.equals("8140")
                        && !codErrorDestino.equals("8147")){
                    arrLogSciff = LogSciffsaldo.with(cuentaDestino,saldoFinalCtaDestino).ask();
                    logRespuesta = logRespuesta + arrLogSciff[0];
                    flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLogSciff[1]));
                    logger.log(Level.INFO,() ->"Termine saldo Destino");
                }

                if (tipoTransaccion.equals("PAGOS_TD_CTA_RECAUDADORA")
                        || tipoTransaccion.equals("PAGO_TD_AHORROS_CORRIENTE")
                        || tipoTransaccion.equals("PAGO_TARJETA_EMPRESARIAL")
                        || tipoTransaccion.equals("PAGO_CODIGO_DE_BARRAS")
                        || tipoTransaccion.equals("PAGO_CONVENIO_MULTI")
                        || tipoTransaccion.equals("PAGO_TARJETA_EMPRESARIAL_MULTI")
                        || tipoTransaccion.equals("PAGO_CODIGO_DE_BARRAS_MULTI")
                        || tipoTransaccion.equals("PAGO_CUENTA_RECAUDADORA_MULTI")) {
                    logger.log(Level.INFO,() ->"Entro tipo transaccion PAGOS");
                    cuenta = dmc.GET_CODIGO_CONVENIO();
                }

                if (!tipoTransaccion.equals("TRANSFERENCIAS")) {
                    if (codErrorDestino.equals("0")) {
                        codErrorDestino = "0000";
                    }
                    //**************************** Validación LOG ConsultaComfflgon2**********************************
                    arrLogComff = LogCommfflg.
                            with(cajero, cuenta, codErrorDestino, transaccionDestino, tipoTransaccion).ask();
                    logRespuesta = logRespuesta + arrLogComff[0];
                    flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLogComff[1]));
                    //**************************** Validación LOG ConsultaSTI **********************************
                    if (consultaDeCosto.equals("S")) {
                        sufijo = "AT6";
                    }
                    arrLogSti = LogSTI.
                            with(cajero,codErrorDestino,transaccionDestino,sufijo,tipoTransaccion).ask();
                    logRespuesta = logRespuesta + arrLogSti[0];
                    flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLogSti[1]));
                    logger.log(Level.INFO,() ->tipoTransaccion);
                }
        }


        if (Integer.parseInt(flagRespuesta) > 0) {
            logRespuesta = logRespuesta +" ****VALIDACIÓN EN ISERIES FALLIDA**** \n";
        } else {
            logRespuesta = logRespuesta +" ****VALIDACIÓN EN ISERIES SATISFACTORIA**** \n";
        }

        logger.log(Level.INFO,() ->"termina Authentic Debito");

        return new String[]{logRespuesta, flagRespuesta};
    }
}
