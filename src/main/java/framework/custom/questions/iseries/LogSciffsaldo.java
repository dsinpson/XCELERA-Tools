package framework.custom.questions.iseries;

import framework.custom.questions.IQuestion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogSciffsaldo implements IQuestion<String[]> {

    Logger logger=Logger.getLogger(LogSciffsaldo.class.getName());

    private final String cuenta;
    private String saldoFin;
    private String logRespuesta = "";
    private String flagRespuesta = "0";


    public LogSciffsaldo(String cuenta, String saldoFin) {
        this.cuenta = cuenta;
        this.saldoFin = saldoFin;
    }

    public static LogSciffsaldo with(String cuenta, String saldoFin){
        return new LogSciffsaldo(cuenta, saldoFin);
    }

    @Override
    public String[] ask() throws SQLException {
        logger.log(Level.INFO,() ->"Entro LOG SCIFFSALDO");

        ArrayList<String> respuestaSciffsaldo = SciffsaldoEnquiry.with(cuenta).ask();
        saldoFin = saldoFin + ".00";
        if (respuestaSciffsaldo.isEmpty()) {
            logRespuesta = logRespuesta +"La consulta al objeto SCIFFSALDO no generó registros. \n";
            flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + 1);
        } else {
            if (saldoFin.equals(respuestaSciffsaldo.get(0))) {
                logRespuesta = logRespuesta +"Log SCIFFSALDO OK. Saldo esperado: "+ respuestaSciffsaldo.get(0) +". \n";
                flagRespuesta = "0";
            } else {
                logRespuesta = logRespuesta +"Log SCIFFSALDO Falló saldo. Valor esperado: "+ saldoFin +" Valor Saldo desde ISeries: "+ respuestaSciffsaldo.get(0) +". \n";
                flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + 1);
            }
        }

        String[] array = {logRespuesta, flagRespuesta};
        logger.log(Level.INFO,() ->logRespuesta);
        logger.log(Level.INFO,() ->flagRespuesta);
        return array;
    }
}
