package framework.custom.questions.authentic;

import framework.custom.questions.IQuestion;
import framework.custom.questions.iseries.LogCommfflg;
import framework.custom.questions.iseries.LogMatffcnip;
import framework.custom.questions.iseries.LogPccfflgaen;
import framework.custom.questions.iseries.LogSTI;
import framework.data.dynamicValues.DynamicValuesCustom;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreditCardLogs implements IQuestion<String[]> {

    Logger logger=Logger.getLogger(CreditCardLogs.class.getName());

    private final String sufijo;
    private String logRespuesta = "";
    private String flagRespuesta = "0";

    public CreditCardLogs(String sufijo) {
        this.sufijo = sufijo;
    }

    public static CreditCardLogs with(String sufijo){return new CreditCardLogs(sufijo);}

    @Override
    public String[] ask() throws SQLException {

        DynamicValuesCustom dmc = new DynamicValuesCustom();
        String[] arrLoglgaen;
        String[] arrLogcnip;
        String[] arrLogSTI;
        String tipoTransaccion = dmc.GET_TIPO_TRANSACCION();
        String cajero = dmc.GET_CAJERO();
        String cuenta = dmc.GET_NUMERO_CUENTA();
        String codigoError = dmc.GET_CODIGO_ERROR();
        String transaccion = dmc.GET_TRANSACCION();
        String tarjeta = dmc.GET_NUMERO_TARJETA();
        String nroIde = dmc.GET_NRO_IDENTIFICACION();
        String consultaDeCosto = dmc.GET_CONSULTA_DE_COSTO();
        String codigoErrorDestino = dmc.GET_CODIGO_ERROR_DESTINO();
        String transaccionDestino = dmc.GET_TRANSACCION_DESTINO();
        String estadoInicialPinforzado = dmc.GET_ESTADO_INICIAL_PINFORZADO();
        String estadoEsperadoPinforzado = dmc.GET_ESTADO_ESPERADO_PINFORZADO();
        String estadoAlerta=dmc.GET_ALERTA();
        String trxCambioClave = dmc.GET_TRX_CAMBIO_CLAVE();


        logger.log(Level.INFO,() ->"llegue ValidacionIseriesCredito");
        logger.log(Level.INFO,() ->tipoTransaccion);
        logger.log(Level.INFO,() ->estadoInicialPinforzado);
        logger.log(Level.INFO,() ->estadoEsperadoPinforzado);

        //**************************** Validación LOG ConsultaComfflgon2**********************************
        String[] arrLogComff = LogCommfflg.
                with(cajero, cuenta, codigoError, transaccion, tipoTransaccion).ask();
        logRespuesta = logRespuesta + arrLogComff[0];
        flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLogComff[1]));

        //**************************** Validación LOG ConsultaSTI **********************************
        if (tipoTransaccion.equals("CAMBIO_PIN_CREDITO")){

            arrLogSTI = LogSTI.
                    with(cajero, codigoError, trxCambioClave, sufijo, tipoTransaccion).ask();
            logRespuesta = logRespuesta + arrLogSTI[0];
            flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLogSTI[1]));

        }
        else {
            arrLogSTI = LogSTI.
                    with(cajero, codigoError, transaccion, sufijo, tipoTransaccion).ask();
            logRespuesta = logRespuesta + arrLogSTI[0];
            flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLogSTI[1]));
        }

        //**************************** Validación LOG ConsultaPccfflgaen **********************************
        if (estadoAlerta.equals("S")) {
            arrLoglgaen = LogPccfflgaen.with(nroIde, codigoError).ask();
            String lg = (!arrLogComff[0].isEmpty()) ? arrLogComff[0]:"";
            logRespuesta = logRespuesta + lg;
            flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLoglgaen[1]));
        }

        if (estadoInicialPinforzado.equals("X") || estadoInicialPinforzado.equals("I")) {
            arrLogcnip = LogMatffcnip.with(tarjeta, estadoEsperadoPinforzado).ask();
            logRespuesta = logRespuesta + arrLogcnip[0];
            flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLogcnip[1]));
        }

        if (consultaDeCosto.equals("S")) {
            if (codigoErrorDestino.equals("0")) {
                codigoErrorDestino = "0000";
            }
            //**************************** Validación LOG ConsultaComfflgon2**********************************
            arrLogComff = LogCommfflg.
                    with(cajero, cuenta, codigoErrorDestino, transaccionDestino,tipoTransaccion).ask();
            logRespuesta = logRespuesta + arrLogComff[0];
            flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLogComff[1]));
            //**************************** Validación LOG ConsultaSTI **********************************
            arrLogSTI = LogSTI.
                    with(cajero,codigoErrorDestino,transaccionDestino,sufijo,tipoTransaccion).ask();
            logRespuesta = logRespuesta + arrLogSTI[0];
            flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + Integer.parseInt(arrLogSTI[1]));
            logger.log(Level.INFO,() ->tipoTransaccion);
        }

        if (Integer.parseInt(flagRespuesta) > 0) {
            logRespuesta = logRespuesta +" ****VALIDACIÓN EN ISERIES FALLIDA**** \n";
        } else {
            logRespuesta = logRespuesta +" ****VALIDACIÓN EN ISERIES SATISFACTORIA**** \n";
        }
        logger.log(Level.INFO,() ->"termina Authentic Credito");
        logger.log(Level.INFO,() ->logRespuesta);
        logger.log(Level.INFO,() ->flagRespuesta);
        logger.log(Level.INFO,() ->"Saliendo Authentic Credito");

        return new String[]{logRespuesta, flagRespuesta};
    }
}
