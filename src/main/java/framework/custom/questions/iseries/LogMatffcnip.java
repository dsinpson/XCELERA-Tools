package framework.custom.questions.iseries;

import framework.custom.questions.IQuestion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogMatffcnip implements IQuestion<String[]> {

    Logger logger=Logger.getLogger(LogMatffcnip.class.getName());

    private final String tarjeta;
    private final String estadoEsperadoPinForzado;

    public LogMatffcnip(String tarjeta, String estadoEsperadoPinForzado) {
        this.tarjeta = tarjeta;
        this.estadoEsperadoPinForzado = estadoEsperadoPinForzado;
    }

    public static LogMatffcnip with(String tarjeta, String estadoEsperadoPinForzado){
        return new LogMatffcnip(tarjeta, estadoEsperadoPinForzado);
    }

    @Override
    public String[] ask() throws SQLException {

        String logRespuesta = "";
        String flagRespuesta = "0";

        logger.log(Level.INFO,() ->"Entro LOG ValidarMattcnip");
        ArrayList<String> respuestaMatffcnip = MatffcnipEnquiry.with(tarjeta).ask();

        if (respuestaMatffcnip.isEmpty()) {
            logRespuesta = logRespuesta + "La consulta a MATLIBRAMD.MATFFCNIP no generó registros. \n";
            flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + 1);
        } else {
            if (estadoEsperadoPinForzado.equals(respuestaMatffcnip.get(0))) {
                logRespuesta = logRespuesta +"Log MATFFCNIP OK. (Pin Forzado) Estado Cliente esperado: "+ respuestaMatffcnip.get(0) +". \n";
            } else {
                logRespuesta = logRespuesta +"Log MATFFCNIP Falló Estado Cliente. (Pin Forzado) Estado Cliente esperado: "+ estadoEsperadoPinForzado +" Estado Cliente desde ISeries: "+ respuestaMatffcnip.get(0) +". \n";
                flagRespuesta = Integer.toString(Integer.parseInt(flagRespuesta) + 1);
            }
        }
        return new String[]{logRespuesta, flagRespuesta};
    }
}
