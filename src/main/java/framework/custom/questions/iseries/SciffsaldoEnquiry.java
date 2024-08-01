package framework.custom.questions.iseries;

import framework.custom.questions.IQuestion;
import framework.custom.utils.BaseDatosAplicacion;
import framework.custom.utils.ConexionIseries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class SciffsaldoEnquiry implements IQuestion<ArrayList<String>> {

    Connection conexion;
    ResultSet rs;


    private final String cuenta;

    public SciffsaldoEnquiry(String cuenta) {
        this.cuenta = cuenta;
    }

    public static SciffsaldoEnquiry with(String cuenta){return new SciffsaldoEnquiry(cuenta);}

    @Override
    public ArrayList<String> ask() throws SQLException {

        BaseDatosAplicacion bda = new BaseDatosAplicacion();
        ArrayList<String> array = new ArrayList<>();

        String strQuery = "SELECT SDSDODSP Saldo_Disponible, SDCUENTA Numero_Cuenta FROM scilibramd.sciffsaldo WHERE SDCUENTA= %s";
        String sentenciaSQLCompleta = String.format(strQuery, cuenta);

        conexion = ConexionIseries.util().conectar();

        rs = bda.ejecutarConsulta(sentenciaSQLCompleta, conexion);

        while (rs.next()) {
            array.add(rs.getString(1));
            array.add(rs.getString(2));
        }

        ConexionIseries.util().desconectar(conexion);
        return array;
    }
}
