package framework.custom.tasks.iseries;

import framework.custom.tasks.ITask;
import framework.custom.utils.BaseDatosAplicacion;
import framework.custom.utils.ConexionIseries;

import java.sql.Connection;

public class PrepareBalance implements ITask {

    private final String saldo;
    private final String cuenta;
    private final String estado;

    Connection conexion;

    public PrepareBalance(String saldo, String cuenta, String estado) {
        this.saldo = saldo;
        this.cuenta = cuenta;
        this.estado = estado;
    }

    public static PrepareBalance with(String saldo, String cuenta, String estado){
        return new PrepareBalance(saldo,cuenta,estado);
    }

    @Override
    public void perform() {

        conexion = ConexionIseries.util().conectar();
        String strQuery = "update scilibramd.sciffsaldo set sdsdodsp = %s, sdestado = '%s' where SDCUENTA='%s'";
        String sentenciaSQLCompleta = String.format(strQuery, saldo, estado, cuenta);
        BaseDatosAplicacion bda = new BaseDatosAplicacion();
        bda.ejecutarConsultaUpdate(sentenciaSQLCompleta, conexion);
        ConexionIseries.util().desconectar(conexion);
        System.out.println(sentenciaSQLCompleta);
    }
}
