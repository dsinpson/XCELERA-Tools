package framework.custom.tasks.iseries;

import framework.custom.tasks.ITask;
import framework.custom.utils.BaseDatosAplicacion;
import framework.custom.utils.ConexionIseries;

import java.sql.Connection;

public class PrepareTransactionAccumulated implements ITask {
    private final String nroOper;
    private final String tarjeta;
    private final String codigoTrx;
    Connection conexion;

    public PrepareTransactionAccumulated(String nroOper, String tarjeta, String codigoTrx) {
        this.nroOper = nroOper;
        this.tarjeta = tarjeta;
        this.codigoTrx = codigoTrx;
    }

    public static PrepareTransactionAccumulated with(String nroOper, String tarjeta, String codigoTrx){
        return new PrepareTransactionAccumulated(nroOper, tarjeta, codigoTrx);
    }

    @Override
    public void perform() {
        conexion = ConexionIseries.util().conectar();
        String strQuery = "update pcclibramd.PCCFFATPCL Set NROOPERA= %s WHERE NROID= (SELECT TJNRODOC FROM cabLIBRAMD.cabfftarj WHERE TJNROTRJ = SUBSTR('%s', 7, 10) fetch first row only) and CANALHABIL='ATM' and CODOPERA= %s";
        String sentenciaSQLCompleta = String.format(strQuery, nroOper, tarjeta, codigoTrx);
        BaseDatosAplicacion bda = new BaseDatosAplicacion();
        bda.ejecutarConsultaUpdate(sentenciaSQLCompleta, conexion);
        ConexionIseries.util().desconectar(conexion);
        System.out.println(sentenciaSQLCompleta);
    }
}
