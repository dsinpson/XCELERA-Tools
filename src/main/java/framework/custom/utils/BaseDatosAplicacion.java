package framework.custom.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import static framework.custom.utils.UtilsConstants.*;


public class BaseDatosAplicacion {

    Logger logger=Logger.getLogger(BaseDatosAplicacion.class.getName());
    private ResultSet rs = null;
    private Statement smt;
    private boolean result = false;

    public BaseDatosAplicacion(){
        // Write document why this constructor is empty
    }

    public ResultSet ejecutarConsulta(String strQuery, Connection conexion) throws SQLException
    {
        try{
            smt = conexion.createStatement();
            rs = smt.executeQuery(strQuery);
        }catch(SQLException e){
            logger.log(Level.SEVERE,() -> CONNECTION_FAILURE + e.getMessage());
        }catch(Exception ex){
            logger.log(Level.SEVERE,() ->DRIVER_NOT_FOUND + ex.getMessage() );
        }
        return rs;
    }

    public boolean ejecutarConsultaUpdate(String strQuery, Connection conexion)
    {
        try{
            smt = conexion.createStatement();
            smt.executeUpdate(strQuery);
            result = true;
        }catch(SQLException e){
            logger.log(Level.SEVERE,() -> CONNECTION_FAILURE + e.getMessage());
            result = false;
        }catch(Exception ex){
            logger.log(Level.SEVERE,() -> DRIVER_NOT_FOUND + ex.getMessage());
        }
        return result;
    }

}
