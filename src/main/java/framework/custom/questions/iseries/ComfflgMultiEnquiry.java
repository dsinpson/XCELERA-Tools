package framework.custom.questions.iseries;

import framework.custom.questions.IQuestion;
import framework.custom.utils.BaseDatosAplicacion;
import framework.custom.utils.ConexionIseries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComfflgMultiEnquiry implements IQuestion<ArrayList<String>> {

    private final String cajero;
    private final String transaccion;
    private final String tipoTransaccion;

    Connection conexion;
    ResultSet rs;
    Logger logger=Logger.getLogger(ComfflgMultiEnquiry.class.getName());

    public ComfflgMultiEnquiry(String cajero, String transaccion, String tipoTransaccion) {
        this.cajero = cajero;
        this.transaccion = transaccion;
        this.tipoTransaccion = tipoTransaccion;
    }

    public static ComfflgMultiEnquiry with(String cajero, String transaccion, String tipoTransaccion){
        return new ComfflgMultiEnquiry(cajero, transaccion, tipoTransaccion);
    }

    @Override
    public ArrayList<String> ask() throws SQLException {

        BaseDatosAplicacion bda = new BaseDatosAplicacion();
        String horaInicial;
        String horaFinal ;
        String strQuery ;
        ArrayList<String> array = new ArrayList<>();
        SimpleDateFormat hora = new SimpleDateFormat("HH");
        SimpleDateFormat min = new SimpleDateFormat("mm");
        SimpleDateFormat sec = new SimpleDateFormat("ss");
        SimpleDateFormat fmes = new SimpleDateFormat("MM");
        SimpleDateFormat fdia = new SimpleDateFormat("dd");

        String fhor = hora.format(new Date());
        String fmin = min.format(new Date());
        String fsec = sec.format(new Date());
        String mes = fmes.format(new Date());
        String dia = fdia.format(new Date());

        horaInicial = fhor+(Integer.parseInt(fmin)-5)+fsec;
        horaFinal = fhor+fmin+fsec;

        conexion= ConexionIseries.util().conectar();
        logger.log(Level.INFO,() -> "Entro consulta Multi");

        if (tipoTransaccion.equals("CONSIGNACION_MULTI")) {
            strQuery = "SELECT  SubstrING(A.LSDATOS,23,3) , SUBSTRING(A.LSDATOS, 27, 4) , "+
                    " SUBSTRING(A.LSDATOS, 306,11) , SUBSTRING(A.LSDATOS, 319,11),"+
                    " LSHRARCB, SUBSTRING(A.LSDATOS, 49,1), SUBSTRING(A.LSDATOS, 50,11),"+
                    "SUBSTRING(A.LSDATOS, 66,1), SUBSTRING(A.LSDATOS, 67,13) FROM CABLIBRAMD.CABFFLGATM A "+
                    " WHERE SUBSTRING(A.LSDATOS, 8, 4)='%s' AND SUBSTRING(A.LSDATOS, 1,2)='TR' AND A.LSHRARCB >= %s00 "+
                    " and A.LSHRARCB <= %s00  and a.LSMES=  %s and a.LSDIA = %s and SubstrING(A.LSDATOS,23,3) = %s "+
                    " ORDER BY A.LSHRARCB DESC";
        } else {
            strQuery = "SELECT  SubstrING(A.LSDATOS,23,3) , SUBSTRING(A.LSDATOS, 27, 4) , "+
                    " SUBSTRING(A.LSDATOS, 117,5) , SUBSTRING(A.LSDATOS, 319,11), LSHRARCB, "+
                    "SUBSTRING(A.LSDATOS, 49,1), SUBSTRING(A.LSDATOS, 50,11),SUBSTRING(A.LSDATOS, 66,1), "+
                    "SUBSTRING(A.LSDATOS, 67,13) FROM CABLIBRAMD.CABFFLGATM A "+
                    " WHERE SUBSTRING(A.LSDATOS, 8, 4)='%s' AND SUBSTRING(A.LSDATOS, 1,2)='TR' AND A.LSHRARCB >= %s00 "+
                    " and A.LSHRARCB <= %s00  and a.LSMES=  %s and a.LSDIA = %s and SubstrING(A.LSDATOS,23,3) = %s  "+
                    "ORDER BY A.LSHRARCB DESC";
        }
        String sentenciaSQLCompleta = String.format(strQuery, cajero, horaInicial, horaFinal, mes, dia, transaccion);

        logger.log(Level.INFO,() ->sentenciaSQLCompleta);

        try {
            rs = bda.ejecutarConsulta(sentenciaSQLCompleta, conexion);

            while (rs.next()) {
                array.add(rs.getString(1));
                array.add(rs.getString(2));
                array.add(rs.getString(3));
                array.add(rs.getString(4));
                array.add(rs.getString(5));
                array.add(rs.getString(6));
                array.add(rs.getString(7));
                array.add(rs.getString(8));
                array.add(rs.getString(9));
            }

            ConexionIseries.util().desconectar(conexion);
        } catch (Exception e) {
            logger.log(Level.SEVERE,() ->"**Error Consulta HDI en ConsultaComfflgMulti ** "+e);
        }
        logger.log(Level.INFO,() -> String.valueOf(array));
        return array;
    }
}
