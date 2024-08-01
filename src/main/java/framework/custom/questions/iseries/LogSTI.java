package framework.custom.questions.iseries;

import framework.custom.questions.IQuestion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogSTI implements IQuestion<String[]> {

    Logger logger=Logger.getLogger(LogSTI.class.getName());

    private final String cajero;
    private String codError;
    private final String transaccion;
    private final String sufijo;
    private final String tipoTransaccion;
    private String logRespuesta = "";
    private String flagRespuesta = "0";

    public LogSTI(String cajero, String codError, String transaccion, String sufijo, String tipoTransaccion) {
        this.cajero = cajero;
        this.codError = codError;
        this.transaccion = transaccion;
        this.sufijo = sufijo;
        this.tipoTransaccion = tipoTransaccion;
    }

    public static LogSTI with(String cajero, String codError, String transaccion, String sufijo, String tipoTransaccion){
        return new LogSTI(cajero, codError, transaccion, sufijo, tipoTransaccion);
    }

    @Override
    public String[] ask() throws SQLException {



        logger.log(Level.INFO,() ->"Entro LOG STI");

        ArrayList<String> respuestaSTI = STiEnquiy.
                with(cajero,transaccion,sufijo,tipoTransaccion).ask();

        if (respuestaSTI.isEmpty()) {
            logRespuesta = logRespuesta +"La consulta al Log STI no generó registros. \n";
            flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + 1);
        } else {
            if (codError.equals("0")) {
                codError =  "0000";
            }
            logger.log(Level.INFO,() ->codError);
            if (!codError.equals("0000") && !respuestaSTI.get(1).equals("0000")) {
                logRespuesta = logRespuesta +"Validacion en Log STI OK. El codigo respuesta de STI es: "+ respuestaSTI.get(1) +". \n";
            } else {
                if (codError.equals("0000") && respuestaSTI.get(1).equals("0000")) {
                    logRespuesta = logRespuesta +"Validacion en Log STI OK. El codigo respuesta de STI es: "+ respuestaSTI.get(1) +". \n";
                    flagRespuesta = "0";
                } else {
                    logRespuesta = logRespuesta +"Validacion en Log STI no exitoso. El Codigo de error esperado: "+ codError +" y el codigo respuesta de STI es: "+ respuestaSTI.get(1) +". \n";
                    flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + 1);
                }
            }
        }
        logger.log(Level.INFO,() ->logRespuesta);
        logger.log(Level.INFO,() ->flagRespuesta);
        return new String[]{logRespuesta, flagRespuesta};
    }
}
