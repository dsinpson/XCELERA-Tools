package framework.custom.utils;



import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import static framework.custom.utils.UtilsConstants.*;

public class ConexionAuthentic {

    Logger logger=Logger.getLogger(ConexionAuthentic.class.getName());

    public static ConexionAuthentic util(){return new ConexionAuthentic();}

    public Connection conectar(){

        Connection connection=null;
        try {
            GestorConexiones gc = new GestorConexiones();
            gc.crearConexionOracle(STR_CONNECTION_AUTHENTIC,USER_AUTHENTIC,PASSWORD_AUTHENTIC);
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
