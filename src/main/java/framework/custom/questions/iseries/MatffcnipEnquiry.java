package framework.custom.questions.iseries;

import framework.custom.questions.IQuestion;
import framework.custom.utils.BaseDatosAplicacion;
import framework.custom.utils.ConexionIseries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MatffcnipEnquiry implements IQuestion<ArrayList<String>> {

    Connection conexion;
    ResultSet rs;
    Logger logger=Logger.getLogger(MatffcnipEnquiry.class.getName());

    private final String tarjeta;
    private String sentenciaSQLCompleta = "";

    public MatffcnipEnquiry(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public static MatffcnipEnquiry with(String tarjeta){return new MatffcnipEnquiry(tarjeta);}

    @Override
    public ArrayList<String> ask() throws SQLException {
        BaseDatosAplicacion bda = new BaseDatosAplicacion();
        ArrayList<String> array = new ArrayList<>();
        String strQuery ="SELECT NIPESTADO FROM MATLIBRAMD.MATFFCNIP  where NIPNROTRJ = %s";


        conexion= ConexionIseries.util().conectar();

        try {
            sentenciaSQLCompleta = String.format(strQuery, tarjeta);
            rs = bda.ejecutarConsulta(sentenciaSQLCompleta, conexion);
        } catch (Exception e) {
            logger.log(Level.SEVERE,() ->"**Error Consulta HDI ConsultaMatffcnip** - mensaje: " + e.getMessage());
        }
        logger.log(Level.INFO,() ->sentenciaSQLCompleta);
        while (rs.next()) {
            array.add(rs.getString(1));
        }
        logger.log(Level.INFO,() ->array.get(0));
        return array;
    }
}
