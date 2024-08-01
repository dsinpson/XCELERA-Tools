package framework.custom.tasks.iseries;

import framework.custom.tasks.ITask;
import framework.custom.utils.BaseDatosAplicacion;
import framework.custom.utils.ConexionIseries;

import java.sql.Connection;

public class PreparePasswordStatus implements ITask {
    private final String nroIntentos;
    private final String estado;
    private final String tarjeta;
    Connection conexion;

    public PreparePasswordStatus(String nroIntentos, String estado, String tarjeta) {
        this.nroIntentos = nroIntentos;
        this.estado = estado;
        this.tarjeta = tarjeta;
    }

    public static PreparePasswordStatus with(String nroIntentos, String estado, String tarjeta){
        return new PreparePasswordStatus(nroIntentos, estado, tarjeta);
    }

    @Override
    public void perform() {
        conexion = ConexionIseries.util().conectar();
        String strQuery = "UPDATE MATLIBRAMD.MATFFESTCL SET ECNROINT = %s, ECESTADO = '%s' WHERE "+
                "ECNROID = (SELECT TJNRODOC FROM cablibramd.CABfftarJ WHERE "+
                "TJNROTRJ=substr(%s, 7, 10) fetch first row only )";
        String sentenciaSQLCompleta = String.format(strQuery, nroIntentos, estado, tarjeta);
        BaseDatosAplicacion bda = new BaseDatosAplicacion();
        bda.ejecutarConsultaUpdate(sentenciaSQLCompleta, conexion);
        ConexionIseries.util().desconectar(conexion);
        System.out.println(sentenciaSQLCompleta);
    }
}
