package framework.custom.tasks.iseries;


import framework.custom.tasks.ITask;
import framework.custom.utils.BaseDatosAplicacion;
import framework.custom.utils.ConexionIseries;

import java.sql.Connection;

public class PrepareForcedPinCardStatus implements ITask {

    private final String estado;
    private final String tarjeta;
    PrepareForcedPinCardStatus(String estado, String tarjeta){
        this.estado=estado;
        this.tarjeta=tarjeta;
    }
    Connection conexion;

    public static PrepareForcedPinCardStatus with(String estado, String tarjeta){
        return new PrepareForcedPinCardStatus(estado, tarjeta);}

    @Override
    public void perform(){

        conexion = ConexionIseries.util().conectar();
        String strQuery ="UPDATE MATLIBRAMD.MATFFCNIP set NIPESTADO = '%s' where NIPNROTRJ = %s";
        String sentenciaSQLCompleta = String.format(strQuery, estado, tarjeta);
        BaseDatosAplicacion bda = new BaseDatosAplicacion();
        bda.ejecutarConsultaUpdate(sentenciaSQLCompleta, conexion);
        ConexionIseries.util().desconectar(conexion);
        System.out.println(sentenciaSQLCompleta);
    }


}
