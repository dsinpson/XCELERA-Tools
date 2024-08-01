package framework.custom.questions.iseries;

import framework.custom.questions.IQuestion;
import framework.data.dynamicValues.DynamicValuesCustom;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogCommfflg implements IQuestion<String []> {

    private final String cajero;
    private final String cuenta;
    private String codError;
    private final String transaccion;
    private final String tipoTransaccion;

    private int varCuenta = 0;
    private String logRespuesta = "";

    Logger logger=Logger.getLogger(LogCommfflg.class.getName());

    public LogCommfflg(String cajero,
                       String cuenta,
                       String codError,
                       String transaccion,
                       String tipoTransaccion) {
        this.cajero = cajero;
        this.cuenta = cuenta;
        this.codError = codError;
        this.transaccion = transaccion;
        this.tipoTransaccion = tipoTransaccion;
    }

    public static LogCommfflg with(String cajero,
                                   String cuenta,
                                   String codError,
                                   String transaccion,
                                   String tipoTransaccion){
        return new LogCommfflg(cajero, cuenta, codError, transaccion, tipoTransaccion);
    }

    @Override
    public String[] ask() throws SQLException {
        DynamicValuesCustom dmc = new DynamicValuesCustom();
        String flagRespuesta = "0";
        String tipoTarjeta = dmc.GET_TIPO_TARJETA();
        String indDevolucionMulti = "";
        String numCuentaDevolucion = "";
        String valorDevolucion = "";
        String valorConsignaEfec = "";

        logger.log(Level.INFO,() ->"Entro LOG comfflg");

        if (tipoTransaccion.equals("CONSIGNACION_MULTI") && (transaccion.equals("501") || transaccion.equals("401") || transaccion.equals("462") || transaccion.equals("562")) || transaccion.equals("629")) {
            indDevolucionMulti = dmc.GET_IND_DEVOLUCION_MULTI();
            numCuentaDevolucion	= dmc.GET_CUENTA_DEVOLUCION_MULTI();
            valorDevolucion = dmc.GET_VALOR_DEVOLUCION_MULTI();
            valorConsignaEfec = dmc.GET_CONSIGNACION_EFECTIVO_MULTI();

            valorConsignaEfec = valorConsignaEfec.trim();
            valorDevolucion = valorDevolucion.trim();

            logger.log(Level.INFO,() ->"Entro LOG comfflgMulti");

            ArrayList<String> respuestaComffMulti = ComfflgMultiEnquiry.
                    with(cajero, transaccion, tipoTransaccion).ask();

            if (respuestaComffMulti.isEmpty()){
                logRespuesta = logRespuesta+"La consulta al Log COMFFLGON2MUL no genero registros.\n";
                flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + 1);
            } else {
                logger.log(Level.INFO,() ->respuestaComffMulti.get(2));
                if (cuenta.equals(respuestaComffMulti.get(2)))
                    varCuenta = 1;
            }
            logger.log(Level.INFO,() -> String.valueOf(varCuenta));
            logger.log(Level.INFO,() ->respuestaComffMulti.get(3));
            if (!transaccion.equals("462") && !transaccion.equals("562")) {
                if (valorConsignaEfec.equals(respuestaComffMulti.get(3))) {
                    logRespuesta = logRespuesta+"Log COMFFLGON2 OK. Valor Consignación es "+valorConsignaEfec+"\n";
                } else {
                    logRespuesta = logRespuesta+"Log COMFFLGON2 Falló transacción. Valor consignación esperado: "+valorConsignaEfec+" Valor Transaccion desde ISeries: "+respuestaComffMulti.get(3)+".\n";
                    flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + 1);
                }
            }
            if ((!indDevolucionMulti.equals("S")) && (!transaccion.equals("462") && !transaccion.equals("562"))) {    // Cambio de Santi esta linea se debe cambiar en todos los framework

                logger.log(Level.INFO,() ->respuestaComffMulti.get(8));
                if (valorDevolucion.equals(String.valueOf(Integer.parseInt(respuestaComffMulti.get(8))))) {
                    logRespuesta = logRespuesta+"Log COMFFLGON2 OK. Valor Devolución es "+valorDevolucion+".\n";
                } else {
                    logRespuesta = "Log COMFFLGON2 Falló transacción. Valor devolución esperado: "+valorDevolucion+" Valor Transaccion desde ISeries:"+respuestaComffMulti.get(8)+".\n";
                    flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + 1);
                }
                if (indDevolucionMulti.equals("A")) {
                    if (numCuentaDevolucion.equals(respuestaComffMulti.get(6))) {
                        logRespuesta = logRespuesta+"Log COMFFLGON2 OK. Cuenta de devolución es "+numCuentaDevolucion+".";
                    } else {
                        logRespuesta = logRespuesta+"Log COMFFLGON2 Falló transacción. Cuenta de devolución esperado: *"+numCuentaDevolucion+"* Valor Transaccion desde ISeries: *"+respuestaComffMulti.get(6)+"*\n";
                        flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + 1);
                    }
                }
            }
            logger.log(Level.INFO,() ->"LOG MULTI");
            logger.log(Level.INFO,() ->logRespuesta);
        } else {

            ArrayList<String> respuestaComff = Comfflgon2Enquiry.
                    with(cajero, transaccion, tipoTransaccion).ask();

            if (respuestaComff.isEmpty()) {
                logRespuesta = logRespuesta+"La coonsulta al Log COMFFLGON2 no genero registros. \n";
                flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + 1);
            } else {
                if (codError.equals("0")) {
                    codError = "0000";
                }
                logger.log(Level.INFO,() ->respuestaComff.get(2));
                if (cuenta.equals(respuestaComff.get(2)) || tipoTransaccion.equals("CAMBIO_DE_CLAVE") || tipoTransaccion.equals("ULTIMOS_MOVIMIENTOS") || codError.equals("8310") || codError.equals("8002") ||
                        codError.equals("8312") || codError.equals("8142") || codError.equals("8006") || codError.equals("8348") || codError.equals("8140") ||
                        codError.equals("8146") || tipoTarjeta.equals("CREDITO") || tipoTarjeta.equals("CREDITO_OUTSERVICE") || (tipoTransaccion.equals("TRANSFERENCIAS") && codError.equals("8889")) || tipoTarjeta.equals("PREPAGO")) {
                    logger.log(Level.INFO,() ->codError);
                    if (codError.equals(respuestaComff.get(1))) {
                        if (transaccion.equals(respuestaComff.get(0))) {
                            logRespuesta = logRespuesta+"Log COMFFLGON2 OK. La transacción es "+ transaccion +". \n";
                            flagRespuesta = "0";
                        } else {
                            logRespuesta = logRespuesta+"Log COMFFLGON2 Falló transacción. Valor esperado: "+ transaccion +" Valor Transaccion desde ISeries: "+ respuestaComff.get(0) +". \n";
                            flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + 1);
                        }
                    } else {
                        logRespuesta = logRespuesta +" Log COMFFLGON2 Falló CodError. Valor esperado: "+ codError +" Valor Codigo Error desde ISeries: "+ respuestaComff.get(1) +". \n";
                        flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + 1);
                    }
                } else {
                    logRespuesta = logRespuesta +"Log COMFFLGON2 Falló Cuenta. Valor esperado: "+ cuenta +" Valor Cuenta desde ISeries: "+ respuestaComff.get(2) +". \n";
                    flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + 1);
                }

            }
        }

        logger.log(Level.INFO,() ->logRespuesta);
        return new String[]{logRespuesta, flagRespuesta};
    }
}
