package framework.custom.questions.iseries;

import framework.custom.questions.IQuestion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogPccfflgaen implements IQuestion<String[]> {

    Logger logger=Logger.getLogger(LogPccfflgaen.class.getName());

    private final String nroIde;
    private final String codError;

    public LogPccfflgaen(String nroIde, String codError) {
        this.nroIde = nroIde;
        this.codError = codError;
    }

    public static LogPccfflgaen with(String nroIde, String codError) {
        return new LogPccfflgaen(nroIde, codError);
    }

    @Override
    public String[] ask() throws SQLException {

        String logRespuesta = "";
        String flagRespuesta = "0";
        logger.log(Level.INFO,() ->"Entro LOG PCCFFLGAEN");
        ArrayList<String> respuestaPccfflgaen = PccfflgaenEnquiry.with(nroIde).ask();
        if (respuestaPccfflgaen.isEmpty() && codError.equals("0")) {
            logRespuesta = logRespuesta +"La consulta al objeto Pccfflgaen no generó registros. \n";
            flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + 1);
        } else {
            if (!respuestaPccfflgaen.get(0).equals("0")) {
                logRespuesta = logRespuesta +"Log Pccfflgaen OK. Mensaje esperado: "+ respuestaPccfflgaen.get(0) +". \n";
            } else {
                logRespuesta = logRespuesta +"Log Pccfflgaen Falló Mensaje. Mesaje esperado: CONSULTAR LOS MENSAJES ESPERADOS. Mensaje desde ISeries: "+ respuestaPccfflgaen.get(0)+". \n";
                flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + 1);
            }
        }
        return new String[]{logRespuesta, flagRespuesta};
    }
}
