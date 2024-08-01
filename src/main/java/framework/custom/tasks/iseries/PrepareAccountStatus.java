package framework.custom.tasks.iseries;

import framework.custom.tasks.ITask;
import framework.custom.utils.BaseDatosAplicacion;
import framework.custom.utils.ConexionIseries;

import java.sql.Connection;

public class PrepareAccountStatus implements ITask {

    private final String estado;
    private final String cuenta;
    Connection conexion;

    public PrepareAccountStatus(String estado, String cuenta) {
        this.estado = estado;
        this.cuenta = cuenta;
    }

    public static PrepareAccountStatus with(String estado, String cuenta){
        return new PrepareAccountStatus(estado, cuenta);
    }

    @Override
    public void perform() {
        conexion = ConexionIseries.util().conectar();
        String strQuery = "UPDATE CABLIBRAMD.CABFFCTAS SET CAESTADO = '%s' WHERE CANROOFC||CANROCTA = '%s'";
        String sentenciaSQLCompleta = String.format(strQuery, estado, cuenta);
        BaseDatosAplicacion bda = new BaseDatosAplicacion();
        bda.ejecutarConsultaUpdate(sentenciaSQLCompleta, conexion);
        ConexionIseries.util().desconectar(conexion);
        System.out.println(sentenciaSQLCompleta);
    }
}
