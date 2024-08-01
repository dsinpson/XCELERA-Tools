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

public class Comfflgon2Enquiry implements IQuestion<ArrayList<String>> {

    private final String cajero;
    private final String transaccion;
    private final String tipoTransaccion;

    Connection conexion;
    ResultSet rs;
    Logger logger=Logger.getLogger(Comfflgon2Enquiry.class.getName());

    public Comfflgon2Enquiry(String cajero, String transaccion, String tipoTransaccion) {
        this.cajero = cajero;
        this.transaccion = transaccion;
        this.tipoTransaccion = tipoTransaccion;
    }

    public static Comfflgon2Enquiry with(String cajero, String transaccion, String tipoTransaccion){
        return new Comfflgon2Enquiry(cajero, transaccion, tipoTransaccion);
    }

    @Override
    public ArrayList<String> ask() throws SQLException {

        BaseDatosAplicacion bda = new BaseDatosAplicacion();

        int tiempo = 5;
        String HoraInicial = "";
        Calendar calendar = Calendar.getInstance();
        ArrayList<String> array = new ArrayList<String>();
        SimpleDateFormat horaf = new SimpleDateFormat("HHmmss");
        SimpleDateFormat fmes = new SimpleDateFormat("MM");
        SimpleDateFormat fdia = new SimpleDateFormat("dd");

        String HoraFinal = horaf.format(new Date());
        String mes = fmes.format(new Date());
        String dia = fdia.format(new Date());

        conexion= ConexionIseries.util().conectar();

        if (cajero.equals("1022") || cajero.equals("1026") || cajero.equals("1027"))
            tiempo = 5;
        calendar.add(Calendar.MINUTE, -tiempo);
        HoraInicial = horaf.format(calendar.getTime());

        logger.log(Level.INFO,() -> "Entro consulta");
        String strQuery = "";
        if (tipoTransaccion.equals("PAGOS_TD_CTA_RECAUDADORA") || tipoTransaccion.equals("PAGO_TD_AHORROS_CORRIENTE") || tipoTransaccion.equals("PAGO_CODIGO_DE_BARRAS") || tipoTransaccion.equals("PAGO_TARJETA_EMPRESARIAL")|| tipoTransaccion.equals("PAGO_CUENTA_RECAUDADORA_MULTI") || tipoTransaccion.equals("PAGO_CODIGO_DE_BARRAS_MULTI") || tipoTransaccion.equals("PAGO_TARJETA_EMPRESARIAL_MULTI") || tipoTransaccion.equals("PAGO_CONVENIO_MULTI")) {
            strQuery = "SELECT  SubstrING(A.LSDATOS,23,3) , SUBSTRING(A.LSDATOS, 27, 4) , "+
                    " SUBSTRING(A.LSDATOS, 117,5) , LSHRARCB FROM CABLIBRAMD.CABFFLGATM A "+
                    " WHERE SUBSTRING(A.LSDATOS, 8, 4)='%s' AND SUBSTRING(A.LSDATOS, 1,2)='TC' AND A.LSHRARCB >= %s00 "+
                    " and A.LSHRARCB <= %s00  and a.LSMES=  %s and a.LSDIA = %s and SubstrING(A.LSDATOS,23,3) = %s ORDER BY A.LSHRARCB DESC";
        } else {
            strQuery = "SELECT  SubstrING(A.LSDATOS,23,3) , SUBSTRING(A.LSDATOS, 27, 4) , "+
                    " SUBSTRING(A.LSDATOS, 36,11) , LSHRARCB FROM CABLIBRAMD.CABFFLGATM A "+
                    " WHERE SUBSTRING(A.LSDATOS, 8, 4)='%s' AND SUBSTRING(A.LSDATOS, 1,2)='TC' AND A.LSHRARCB >= %s00 "+
                    " and A.LSHRARCB <= %s00  and a.LSMES= %s and a.LSDIA = %s and SubstrING(A.LSDATOS,23,3) = %s  ORDER BY A.LSHRARCB DESC ";
        }

        String sentenciaSQLCompleta = String.format(strQuery, cajero, HoraInicial, HoraFinal, mes, dia, transaccion);

        logger.log(Level.INFO,() -> sentenciaSQLCompleta);
        rs = bda.ejecutarConsulta(sentenciaSQLCompleta, conexion);

        while (rs.next()) {
            array.add(rs.getString(1));
            array.add(rs.getString(2));
            array.add(rs.getString(3));
            array.add(rs.getString(4));
        }
        ConexionIseries.util().desconectar(conexion);
        logger.log(Level.INFO,() -> String.valueOf(array));
        return array;
    }
}
