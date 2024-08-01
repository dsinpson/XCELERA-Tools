package framework.custom.utils;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import static framework.custom.utils.UtilsConstants.*;

public class ConexionIseries {

    Logger logger=Logger.getLogger(ConexionIseries.class.getName());

    public static ConexionIseries util(){return new ConexionIseries();}

    public Connection conectar(){

        Connection connection=null;
        try {
            GestorConexiones gc = new GestorConexiones();
            gc.crearConexionDb2(STR_CONNECTION_ISERIES,USER_ISERIES,PASSWORD_ISERIES);
            connection = gc.getConnection();
        } catch(Exception e) {
            logger.log(Level.SEVERE,() ->CONNECTION_FAILURE + e.getMessage());
        }
        return connection;
    }
    public void desconectar( Connection connection){
        GestorConexiones.util().closeConnection(connection);
    }
}
