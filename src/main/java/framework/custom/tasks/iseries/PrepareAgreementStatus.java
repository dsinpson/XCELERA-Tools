package framework.custom.tasks.iseries;

import framework.custom.tasks.ITask;
import framework.custom.utils.BaseDatosAplicacion;
import framework.custom.utils.ConexionIseries;

import java.sql.Connection;

public class PrepareAgreementStatus implements ITask {
    private final String estado;
    private final String convenio;
    Connection conexion;

    public PrepareAgreementStatus(String estado, String convenio) {
        this.estado = estado;
        this.convenio = convenio;
    }

    public static PrepareAgreementStatus with(String estado, String convenio){
        return new PrepareAgreementStatus(estado, convenio);
    }
    @Override
    public void perform() {
        conexion = ConexionIseries.util().conectar();
        String strQuery ="UPDATE RECLIBRAMD.RECFFMCONE set ESTCONV = '%s' where CODCONV ='%s'";
        String sentenciaSQLCompleta = String.format(strQuery, estado, convenio);
        BaseDatosAplicacion bda = new BaseDatosAplicacion();
        bda.ejecutarConsultaUpdate(sentenciaSQLCompleta, conexion);
        ConexionIseries.util().desconectar(conexion);
        System.out.println(sentenciaSQLCompleta);

    }
}
