package framework.custom.questions.authentic;

import framework.custom.questions.IQuestion;
import framework.custom.utils.BaseDatosAplicacion;
import framework.custom.utils.ConexionAuthentic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AtmStatus implements IQuestion<Object[]> {

    private final String idCajero;
    Connection conexion;
    ResultSet rs;
    Logger logger=Logger.getLogger(AtmStatus.class.getName());

    public AtmStatus(String idCajero) {
        this.idCajero=idCajero;
    }

    public static AtmStatus with(String idCajero){return new AtmStatus(idCajero);}

    public Object[] ask() throws SQLException {
        BaseDatosAplicacion bda = new BaseDatosAplicacion();
        logger.log(Level.INFO,() ->"validarEstadoCajero");
        SimpleDateFormat year = new SimpleDateFormat("dd/MM/yy");
        String fechaHoy = year.format(new Date());

        String terminal = "";
        String operationStatus = "";
        java.sql.Date businessDate = null;
        String logcajero = "";
        boolean resultado = false;
        String strQuery = "";

        conexion = ConexionAuthentic.util().conectar();
        strQuery = "SELECT ASN.AST_MACCING_ENABLED, ASN.AST_LOGGING, ASN.AST_TERMINAL_ID,"+
                "ASN.AST_ACTIVE, ASN.AST_ID, ASN.AST_MACHINE_NUMBER,"+
                "STTS.ATS_OPERATION_STATUS, STTS.ATS_COMM_STATUS, STTS.ATS_BUSINESS_DAY "+
                "FROM BCOLOMBIA_OWNER.ATM_STATIONS ASN "+
                "LEFT JOIN BCOLOMBIA_OWNER.ATM_STATUS STTS ON ASN.AST_ID = STTS.ATS_AST_ID "+
                "WHERE AST_TERMINAL_ID = '%s'";

        String sentenciaSQLCompleta = String.format(strQuery, idCajero);
        rs = bda.ejecutarConsulta(sentenciaSQLCompleta, conexion);
        logger.log(Level.INFO,() ->sentenciaSQLCompleta);

        try {
            while(rs.next()) {
                terminal = rs.getString("AST_TERMINAL_ID");
                operationStatus = rs.getString("ATS_OPERATION_STATUS");
                businessDate = rs.getDate("ATS_BUSINESS_DAY");
            }
        } catch(Exception e) {
            logger.log(Level.SEVERE,() -> "falló lectura del resulSet - mensaje: " + e.getMessage());
        }
        ConexionAuthentic.util().desconectar(conexion);

        logcajero = logcajero +"Cajero con ID TERMINAL: "+ terminal +". ";

        if (operationStatus.equals("In service")) {
            logcajero = logcajero +"Estado Cajero: "+ operationStatus +". ";
            String fecha = year.format(businessDate);
            if (fecha.compareTo(fechaHoy) < 0) {
                logcajero = logcajero +"Fecha diferente a hoy: BusinessDate del cajero: "+ businessDate +". ";
                logcajero = logcajero +"Consulta SQL: "+ strQuery +". ";
            } else {
                resultado = true;
                logcajero = logcajero +"Fecha corresponde a hoy: BusinessDate del cajero: "+ businessDate +". ";
            }
        } else {
            logcajero = logcajero +"Estado Cajero: "+ operationStatus +". ";
            logcajero = logcajero +"Consulta SQL: "+ strQuery +". ";
        }

        Object[] array = new Object[2];
        array[0] = resultado;
        array[1] = logcajero;
        return array;
    }
}
