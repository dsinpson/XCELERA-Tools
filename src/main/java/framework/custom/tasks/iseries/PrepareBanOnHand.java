package framework.custom.tasks.iseries;

import framework.custom.tasks.ITask;
import framework.custom.utils.BaseDatosAplicacion;
import framework.custom.utils.ConexionIseries;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PrepareBanOnHand implements ITask {

    private final String cuenta;
    private final String pin;
    private final String minutosTrx;
    private final String acumTopeMes;
    Connection conexion;
    public PrepareBanOnHand(String cuenta, String pin, String minutosTrx, String acumTopeMes) {
        this.cuenta = cuenta;
        this.pin = pin;
        this.minutosTrx = minutosTrx;
        this.acumTopeMes = acumTopeMes;

    }

    public static PrepareBanOnHand with(String cuenta, String pin, String minutosTrx, String acumTopeMes){
        return new PrepareBanOnHand(cuenta,pin,minutosTrx,acumTopeMes);
    }

    @Override
    public void perform() {

        conexion= ConexionIseries.util().conectar();
        String strQuery;
        SimpleDateFormat hoy = new SimpleDateFormat("yyyyMMdd");

        String FechaHoy = hoy.format(new Date());
        SimpleDateFormat hora = new SimpleDateFormat("HHmmss");

        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MINUTE,Integer.parseInt(minutosTrx));
        String hora1 = hora.format(calendar.getTime());
        strQuery = "UPDATE PCCLIBRAMD.PCCFFTOACM SET TOAVLRACU = %s WHERE TOACUENTA = %s";
        String sentenciaSQLCompleta = String.format(strQuery, acumTopeMes, cuenta);
        BaseDatosAplicacion bda = new BaseDatosAplicacion();
        bda.ejecutarConsultaUpdate(sentenciaSQLCompleta, conexion);

        strQuery = "DELETE PCCLIBRAMD.PCCFFSLCCL WHERE PNUMCUENT = %s";
        sentenciaSQLCompleta = String.format(strQuery, cuenta);
        bda.ejecutarConsultaUpdate(sentenciaSQLCompleta, conexion);
        System.out.println("**Fecha ** "+ FechaHoy);
        System.out.println("** Hora ** "+ hora1);
        System.out.println("**Cuenta ** "+ cuenta);
        strQuery = "INSERT INTO PCCLIBRAMD.PCCFFSLCCL VALUES ('MOV', '%s' , '7','%s','%s' , %s , %s , '%s' , '0000',' ',0,0,0)";
        sentenciaSQLCompleta = String.format(strQuery, cuenta, FechaHoy, hora1, FechaHoy, hora1, pin);
        bda.ejecutarConsultaUpdate(sentenciaSQLCompleta, conexion);
        ConexionIseries.util().desconectar(conexion);
        System.out.println(sentenciaSQLCompleta);

    }
}
