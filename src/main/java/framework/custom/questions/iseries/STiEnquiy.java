package framework.custom.questions.iseries;

import framework.custom.questions.IQuestion;
import framework.custom.utils.BaseDatosAplicacion;
import framework.custom.utils.ConexionIseries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class STiEnquiy implements IQuestion<ArrayList<String>> {

    Connection conexion;
    ResultSet rs;
    Logger logger=Logger.getLogger(STiEnquiy.class.getName());

    private final String cajero;
    private final String transaccion;
    private final String sufijo;
    private final String tipoTransaccion;

    public STiEnquiy(String cajero, String transaccion, String sufijo, String tipoTransaccion) {
        this.cajero = cajero;
        this.transaccion = transaccion;
        this.sufijo = sufijo;
        this.tipoTransaccion = tipoTransaccion;
    }

    public static  STiEnquiy with(String cajero, String transaccion, String sufijo, String tipoTransaccion){
        return new STiEnquiy(cajero, transaccion, sufijo, tipoTransaccion);
    }

    @Override
    public ArrayList<String> ask() throws SQLException {

        BaseDatosAplicacion bda = new BaseDatosAplicacion();
        Calendar calendar = Calendar.getInstance();
        ArrayList<String> array = new ArrayList<>();
        SimpleDateFormat hoy = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat hor = new SimpleDateFormat("HHmmss");

        String fechaHoy = hoy.format(new Date());
        String hora1 = hor.format(new Date());
        String strQuery = "";
        int tiempo = 6;

        conexion= ConexionIseries.util().conectar();

        if (cajero.equals("1022") || cajero.equals("1026") || cajero.equals("1027"))
            tiempo = 6;
        calendar.add(Calendar.MINUTE, -tiempo);
        String hora2 = hor.format(calendar.getTime());

        if (tipoTransaccion.equals("AHORRO_A_LA_MANO")) {
            strQuery = "SELECT IDTRANS, CODRESPU, OBSERVA, MENSAJE "+
                    "FROM CABLIBRAMD.CABFFLT%s "+
                    "WHERE Substr(IDTRANS,2,3) = '%s' "+
                    "AND  FECHAREG = %s AND HORAREG BETWEEN %s00 "+
                    "AND %s00 and TIPOMSG ='TR' "+
                    "AND SUBSTR(MENSAJE, 370,30) LIKE '%%%s%%' "+
                    "ORDER BY HORAREG DESC";
        } else {
            strQuery = "SELECT IDTRANS, CODRESPU, OBSERVA, MENSAJE "+
                    "FROM CABLIBRAMD.CABFFLT%s "+
                    "WHERE Substr(IDTRANS,2,3) = '%s' "+
                    "AND  FECHAREG = %s AND HORAREG BETWEEN %s00 "+
                    "AND %s00 and TIPOMSG ='TR' "+
                    "AND SUBSTR(MENSAJE, 360,48) LIKE '%%%s%%' "+
                    "ORDER BY HORAREG DESC ";
        }

        String sentenciaSQLCompleta = String.format(strQuery, sufijo, transaccion, fechaHoy, hora2, hora1, cajero);

        rs = bda.ejecutarConsulta(sentenciaSQLCompleta, conexion);
        logger.log(Level.INFO,() ->sentenciaSQLCompleta);

        while (rs.next()) {
            array.add(rs.getString(1));
            array.add(rs.getString(2));
            array.add(rs.getString(3));
            array.add(rs.getString(4));
        }
        return array;
    }

}
