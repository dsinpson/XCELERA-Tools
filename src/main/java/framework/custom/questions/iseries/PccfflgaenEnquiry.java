package framework.custom.questions.iseries;

import framework.custom.questions.IQuestion;
import framework.custom.utils.BaseDatosAplicacion;
import framework.custom.utils.ConexionIseries;
import framework.data.dynamicValues.DynamicValuesCustom;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PccfflgaenEnquiry implements IQuestion<ArrayList<String>> {

    Connection conexion;
    ResultSet rs;
    Logger logger=Logger.getLogger(PccfflgaenEnquiry.class.getName());

    private final String nroIde;

    public PccfflgaenEnquiry(String nroIde) {
        this.nroIde = nroIde;
    }

    public static PccfflgaenEnquiry with(String nroIde){
        return new PccfflgaenEnquiry(nroIde);
    }

    @Override
    public ArrayList<String> ask() throws SQLException {

        Calendar calendar = Calendar.getInstance();
        DynamicValuesCustom dmc = new DynamicValuesCustom();
        BaseDatosAplicacion bda = new BaseDatosAplicacion();
        ArrayList<String> array = new ArrayList<>();

        int tiempo = 5;

        conexion= ConexionIseries.util().conectar();

        if (dmc.GET_CAJERO().equals("1022") || dmc.GET_CAJERO().equals("1026") || dmc.GET_CAJERO().equals("1027"))
            tiempo = 5;
        calendar.add(Calendar.MINUTE, -tiempo);

        SimpleDateFormat hoy = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat hor = new SimpleDateFormat("HHmmss");

        String fechaHoy = hoy.format(new Date());
        String hora1 = hor.format(new Date());
        String hora2 = hor.format(calendar.getTime());

        String strQuery = "SELECT case when AENMSGENV='' then '0' else AENMSGENV end "+
                "FROM PCCLIBRAMD.PCCFFLGAEN A WHERE A.AENCANOPE='ATM' AND "+
                "AENNROIDE = %s and AENFECHA= %s" +
                " AND AENHORA BETWEEN %s AND %s ORDER BY RRN(A) DESC FETCH FIRST ROW ONLY  ";

        String sentenciaSQLCompleta = String.format(strQuery, nroIde, fechaHoy, hora2, hora1);

        rs = bda.ejecutarConsulta(sentenciaSQLCompleta, conexion);
        logger.log(Level.INFO,() ->sentenciaSQLCompleta);

        while (rs.next()) {
            array.add((rs.getString(1)));
        }

        ConexionIseries.util().desconectar(conexion);
        logger.log(Level.INFO,() -> String.valueOf(array));

        return array;
    }
}
