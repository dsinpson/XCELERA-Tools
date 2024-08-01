package framework.custom.questions.authentic;

import framework.custom.questions.IQuestion;
import framework.custom.utils.BaseDatosAplicacion;
import framework.custom.utils.ConexionAuthentic;
import framework.dataProviders.ConfigFileReader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnvironmentStatus implements IQuestion<Object[]> {

    private final String process;
    private final String interchange;
    private final String iap;

    public static final String COLUMN_SST_STATUS ="SST_STATUS";
    public static final String COLUMN_SST_URI_SSP ="SST_URI_SSP";
    public static final String COLUMN_TIPO ="TIPO";
    public static final String STATUS_STARTED ="started";
    public static final String STATUS_IN_SESSION ="IN-SESSION";
    public static final String TIPO_PROCESS ="process";
    public static final String TIPO_INTERCHANGE ="interchange";
    public static final String TIPO_IAP ="iap";
    public static  String TABLE_SYSTEM_STATUS ="FROM BCOLOMBIA_OWNER.SYSTEM_STATUS ";
    ConfigFileReader f = new ConfigFileReader("configs/config.properties");
    private final String bd = f.getPropertyByKey("IsBD4.2");
    private final boolean bd42=Boolean.parseBoolean(bd);


    Connection conexion;
    ResultSet rs;
    Logger logger=Logger.getLogger(EnvironmentStatus.class.getName());

    public EnvironmentStatus(String process, String interchange, String iap) {
        this.process = process;
        this.interchange = interchange;
        this.iap = iap;
    }

    public static EnvironmentStatus with(String process, String interchange, String iap){
        return new EnvironmentStatus(process, interchange, iap);
    }

    @Override
    public Object[] ask() throws SQLException {
        if (!bd42)
        {
            TABLE_SYSTEM_STATUS ="FROM SCHAUTAQ.SYSTEM_STATUS ";
        }
        BaseDatosAplicacion bda = new BaseDatosAplicacion();
        String strQuery;
        StringBuilder logAmbienteBuilder = new StringBuilder();

        int contador = 0;
        boolean resultado = true;

        conexion = ConexionAuthentic.util().conectar();
        strQuery = "SELECT * FROM( "+
                "SELECT SST_URI_SSP, "+
                "SST_URI_SCHEMA AS Tipo, SST_STATUS "+
                TABLE_SYSTEM_STATUS+
                "WHERE SST_URI_SSP LIKE '%%%s%%' "+
                "AND SST_URI_SCHEMA = 'process' "+
                "UNION "+
                "SELECT SST_URI_SSP, SST_URI_SCHEMA AS Tipo, SST_STATUS "+
                TABLE_SYSTEM_STATUS+
                "WHERE SST_URI_SSP LIKE '%%%s%%' "+
                "AND SST_URI_SCHEMA = 'interchange' "+
                "UNION "+
                "SELECT SST_URI_SSP, SST_URI_SCHEMA AS Tipo, SST_STATUS "+
                TABLE_SYSTEM_STATUS+
                "WHERE SST_URI_SCHEMA = 'iap' "+
                "AND SST_URI_SSP LIKE '%%%s%%' "+
                ")ORDER BY Tipo DESC, SST_URI_SSP ASC ";

        String sentenciaSQLCompleta = String.format(strQuery, process, interchange, iap);
        rs = bda.ejecutarConsulta(sentenciaSQLCompleta, conexion);
        logger.log(Level.INFO,() -> sentenciaSQLCompleta);

        try {
            while(rs.next()) {

                if(rs.getString("Tipo").equals(TIPO_PROCESS)
                        || rs.getString("Tipo").equals(TIPO_INTERCHANGE)
                        ||rs.getString("Tipo").equals(TIPO_IAP)){

                    logAmbienteBuilder
                            .append(rs.getString(COLUMN_SST_URI_SSP))
                            .append(" - ")
                            .append(rs.getString(COLUMN_TIPO))
                            .append(" = ")
                            .append(rs.getString(COLUMN_SST_STATUS))
                            .append("</br> ");

                    if (rs.getString("Tipo").equals(TIPO_PROCESS)
                            && !rs.getString(COLUMN_SST_STATUS).equals(STATUS_STARTED)) {
                        logger.log(Level.WARNING,() -> "service PROCESS not started");
                        contador += 1;
                    }
                    else if (rs.getString("Tipo").equals(TIPO_INTERCHANGE)
                            &&!rs.getString(COLUMN_SST_STATUS).equals(STATUS_STARTED)) {
                        logger.log(Level.WARNING,() -> "service INTERCHANGE not started");
                        contador += 1;
                    }
                    else if (rs.getString("Tipo").equals(TIPO_IAP) &&
                            !rs.getString(COLUMN_SST_STATUS).equals(STATUS_IN_SESSION))	{
                        contador += 1;
                        //TODO Pending the tasks Restart Services
                        /*
                        if(iap.equals("iSeries_IAP_AT6")
                                &&!rs.getString(COLUMN_SST_STATUS).equals(STATUS_IN_SESSION)) {

                            logger.log(Level.WARNING,() ->"AT6 OUT-SESSION Reiniciando Servidor");
                            ReinicioServidores at6 = new ReinicioServidores();
                            at6.reiniciarAT6();
                            System.out.println("Por favor espere unos segundos...");
                            Thread.sleep(2000);
                            contador = 0;
                        }
                        else if(iap.equals("iSeries_IAP_AT7")
                                    &&!rs.getString(COLUMN_SST_STATUS).equals(STATUS_IN_SESSION)) {

                            logger.log(Level.WARNING,() ->"AT7 OUT-SESSION Reiniciando Servidor");
                            ReinicioServidores at7 = new ReinicioServidores();
                            at7.reiniciarAT7();
                            System.out.println("Por favor espere unos segundos...");
                            Thread.sleep(2000);
                            contador = 0;
                        }
                        else if(iap.equals("iSeries_IAP_AT8")
                                    &&!rs.getString(COLUMN_SST_STATUS).equals(STATUS_IN_SESSION)) {

                            logger.log(Level.WARNING,() ->"AT8 OUT-SESSION Reiniciando Servidor");
                            ReinicioServidores at8 = new ReinicioServidores();
                            at8.reiniciarAT8();
                            System.out.println("Por favor espere unos segundos...");
                            Thread.sleep(2000);
                            contador = 0;
                        }
                        else if(iap.equals("iSeries_IAP_AT9")
                                    &&!rs.getString(COLUMN_SST_STATUS).equals(STATUS_IN_SESSION)) {

                            logger.log(Level.WARNING,() ->"AT9 OUT-SESSION Reiniciando Servidor");
                            ReinicioServidores at9 = new ReinicioServidores();
                            at9.reiniciarAT9();
                            System.out.println("Por favor espere unos segundos...");
                            Thread.sleep(2000);
                            contador = 0;
                        }
                         */
                    }
                }
            }
        } catch(Exception e) {
            logger.log(Level.WARNING,() -> "Falló la verificacion del ambiente - mensaje: "+e.getMessage());
        }
        ConexionAuthentic.util().desconectar(conexion);


        if(contador > 0) {
            resultado = false;
        }

        Object[] array = new Object[2];
        array[0] = resultado;
        array[1] = logAmbienteBuilder.toString();
        return array;
    }
}
