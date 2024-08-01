package framework.custom.tasks.iseries;

import framework.custom.tasks.ITask;
import framework.custom.utils.BaseDatosAplicacion;
import framework.custom.utils.ConexionIseries;

import java.sql.Connection;

public class PrepareMonetaryAccumulated implements ITask {
    private final String monto;
    private final String tarjeta;
    private final String codigoTrx;
    Connection conexion;

    public PrepareMonetaryAccumulated(String monto, String tarjeta, String codigoTrx) {
        this.monto = monto;
        this.tarjeta = tarjeta;
        this.codigoTrx = codigoTrx;
    }

    public static PrepareMonetaryAccumulated with(String monto, String tarjeta, String codigoTrx){
        return new PrepareMonetaryAccumulated(monto, tarjeta, codigoTrx);
    }
    @Override
    public void perform() {
        conexion = ConexionIseries.util().conectar();
        String strQuery = "update pcclibramd.PCCFFATPCL Set MONTO= %s WHERE NROID= (SELECT TJNRODOC FROM cabLIBRAMD.cabfftarj WHERE TJNROTRJ = SUBSTR('%s', 7, 10) fetch first row only) and CANALHABIL='ATM' and CODOPERA= %s";
        String sentenciaSQLCompleta = String.format(strQuery, monto, tarjeta, codigoTrx);
        BaseDatosAplicacion bda = new BaseDatosAplicacion();
        bda.ejecutarConsultaUpdate(sentenciaSQLCompleta, conexion);
        ConexionIseries.util().desconectar(conexion);
        System.out.println(sentenciaSQLCompleta);
    }
}
