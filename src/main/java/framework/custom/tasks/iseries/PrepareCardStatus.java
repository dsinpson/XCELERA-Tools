package framework.custom.tasks.iseries;

import framework.custom.tasks.ITask;
import framework.custom.utils.BaseDatosAplicacion;
import framework.custom.utils.ConexionIseries;

import java.sql.Connection;

public class PrepareCardStatus implements ITask {
    private final String estado;
    private final String tarjeta;
    Connection conexion;

    public PrepareCardStatus(String estado, String tarjeta) {
        this.estado = estado;
        this.tarjeta = tarjeta;
    }

    public static PrepareCardStatus with(String estado, String tarjeta){
        return new PrepareCardStatus(estado, tarjeta);
    }

    @Override
    public void perform() {
        conexion = ConexionIseries.util().conectar();
        String strQuery = "Update cablibramd.cabfftarj set TJESTTARDB = '%s' WHERE TJNROTRJ= SUBSTR('%s', 7, 10)";
        String sentenciaSQLCompleta = String.format(strQuery, estado, tarjeta);
        BaseDatosAplicacion bda = new BaseDatosAplicacion();
        bda.ejecutarConsultaUpdate(sentenciaSQLCompleta, conexion);
        ConexionIseries.util().desconectar(conexion);
        System.out.println(sentenciaSQLCompleta);
    }
}
