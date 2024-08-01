package framework.data.dynamicValues;


import framework.custom.utils.BaseDatosAplicacion;
import framework.custom.utils.GestorConexiones;
import framework.dataProviders.ConfigFileReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 *
 */

public class DynamicValuesCustom {

    Logger logger=Logger.getLogger(DynamicValuesCustom.class.getName());
    private static final String SENTENCIA_SQL = "SELECT * FROM %s WHERE ID = %s";
    GestorConexiones gc;
    BaseDatosAplicacion bda;
    public static Map <String, String> datosRepositorioPruebas;
    public static Map <String, String> datosConfiguracionInicial;
    HashMap <String, String> datosMensajesPantalla;
    HashMap <String, String> datosMovimientosRobot;
    ConfigFileReader f = new ConfigFileReader("configs/config.properties");
    String typeNew2 = f.getPropertyByKey("IsCoBot");
    String idRobot = f.getPropertyByKey("IDRobot");
    String marca = f.getPropertyByKey("Marca");

    public DynamicValuesCustom() {
        // Write document why this constructor is empty
    }

    public void getDatosPruebas(String id) throws SQLException, IOException {
        datosRepositorioPruebas = new HashMap<>();
        String sentenciaSQL = "EXEC Get_Datos_Prueba_"+marca+" %s"; // si se cambia de cajero cambiar a la marca correspondiente
        String sentenciaSQLCompleta = String.format(sentenciaSQL, id);

        datosRepositorioPruebas=getDatosRecordTabla(sentenciaSQLCompleta);
    }

    public void getConfiguracionRobot(String id, String tabla) throws SQLException, IOException {
        datosConfiguracionInicial = new HashMap<>();

        String sentenciaSQLCompleta = String.format(SENTENCIA_SQL, tabla, id);

        datosConfiguracionInicial = getDatosRecordTabla(sentenciaSQLCompleta);
    }

    public void getMensajesPantalla(String id, String tabla) throws SQLException, IOException {
        datosMensajesPantalla = new HashMap<>();

        String sentenciaSQLCompleta = String.format(SENTENCIA_SQL, tabla, id);

        datosMensajesPantalla = getDatosRecordTabla(sentenciaSQLCompleta);
    }

    public void getMovimientosRobot(String id, String tabla) throws SQLException, IOException {
        datosMovimientosRobot = new HashMap<>();

        String sentenciaSQLCompleta = String.format(SENTENCIA_SQL, tabla, id);

        datosMovimientosRobot =getDatosRecordTabla(sentenciaSQLCompleta);
    }
    public HashMap<String, String> getDatosRecordTabla(String query) throws SQLException {
        ResultSet resultSet;
        Connection connection;

        try
        {
            gc = new GestorConexiones();
            ConfigFileReader reader = new ConfigFileReader("configs/config.properties");
            String base = reader.getPropertyByKey("cadenaConexionSQL");
            gc.crearConexionSQL(base);
            connection = gc.getConnection();

            bda = new BaseDatosAplicacion();

            resultSet = bda.ejecutarConsulta(query, connection);
        }
        catch(Exception ex)
        {
            logger.log(Level.SEVERE,ex.getMessage());
            resultSet = null;
        }
        return llenarHashConResultSet(resultSet);
    }

    private HashMap<String,String> llenarHashConResultSet(ResultSet resultSet) throws SQLException {

        HashMap<String,String> hashMap = new HashMap<>();

        if(resultSet != null) {
            while(resultSet.next()) {
                for(int i=1;i<=resultSet.getMetaData().getColumnCount();i++){
                    String nombreCampo = resultSet.getMetaData().getColumnName(i);
                    String valorCampo = resultSet.getObject(i)== null?"":resultSet.getObject(i).toString();
                    hashMap.put(nombreCampo, valorCampo);
                }
            }
        }
        return hashMap;
    }

    //'****************** PARA CONFIGURACION INICIAL ********************

    public void SET_ID_PRUEBA(String id) throws SQLException, IOException {
        this.getDatosPruebas(id);
    }
    public void SET_PARAMETROS_ROBOT(String id) throws SQLException, IOException {
        this.getConfiguracionRobot(idRobot, "TBL_CONFIGURACION_INICIAL");
    }
    public String GET_CAJERO() {return datosConfiguracionInicial.get("IdCajero");}
    public String GET_IP_ROBOT() {return datosConfiguracionInicial.get("IpRobot");}
    public String GET_IP_MOD_BOTONES_1() {return datosConfiguracionInicial.get("IpModBotones1");}
    public String GET_IP_MOD_BOTONES_2() {return datosConfiguracionInicial.get("IpModBotones2");}
    public String GET_IP_MOD_TARJETA() {return datosConfiguracionInicial.get("IpModTarjeta");}
    public String GET_IP_MOD_TECLADO() {return datosConfiguracionInicial.get("IpModTeclado");}
    public String GET_IP_MOD_RECIBO() {return datosConfiguracionInicial.get("IpModRecibo");}
    public String GET_PUERTO_ROBOT() {return datosConfiguracionInicial.get("PuertoRobot");}
    public String GET_RUTA_ARCHIVO_STARC() {return datosConfiguracionInicial.get("RutaArchivoStarc");}
    public String GET_RUTA_ARCHIVO_STARCG() {return datosConfiguracionInicial.get("RutaArchivoStarcg");    }
    public String GET_MARCA_CAJERO() {return datosConfiguracionInicial.get("MarcaCajero");}
    public String GET_COORDENADA_ROBOT() {return datosConfiguracionInicial.get("CoordenadaRobot");}
    public String GET_SET_PRUEBA() {return datosConfiguracionInicial.get("SetPrueba");}

    //****************** DATOS PARA TRANSACCION ********************
    public String GET_TIPO_TRANSACCION() {
        return datosRepositorioPruebas.get("NombreTransaccionPrueba");
    }

    public String GET_NUMERO_CUENTA() {
        return datosRepositorioPruebas.get("NumeroCuenta");
    }

    public String GET_NUMERO_PIN() {return datosRepositorioPruebas.get("NumeroPin");}

    public String GET_MONTO_INICIAL() {
        return datosRepositorioPruebas.get("MontoInicial");
    }

    public String GET_NUMERO_TARJETA() {
        return datosRepositorioPruebas.get("NumeroTarjeta");
    }

    public String GET_CODIGO_ERROR() {
        return datosRepositorioPruebas.get("CodigoError");
    }

    public String GET_MINUTOS_TRX() {
        return datosRepositorioPruebas.get("MinutosTRX");
    }

    public String GET_ESTADO_CUENTA() {
        return datosRepositorioPruebas.get("EstadoCuenta");
    }

    public String GET_AUTORIZADOR() {
        return datosRepositorioPruebas.get("Autorizador");
    }

    public String GET_TRANSACCION() {
        return datosRepositorioPruebas.get("Transaccion");
    }

    public String GET_SALDOFINAL() {
        return datosRepositorioPruebas.get("SaldoFinal");
    }

    public String GET_ACUM_TOPE_MES() {
        return datosRepositorioPruebas.get("AcumTopeMes");
    }

    public String GET_NRO_IDENTIFICACION() {
        return datosRepositorioPruebas.get("NroIdentificacion");
    }

    public String GET_ESTADO_TARJETA() {
        return datosRepositorioPruebas.get("EstadoTarjeta");
    }

    public String GET_NUMERO_INTENTOS() {
        return datosRepositorioPruebas.get("NroIntentos");
    }

    public String GET_ESTADO_CLAVE() {
        return datosRepositorioPruebas.get("EstadoClave");
    }

    public String GET_CLIENTE_PERSONALIZADO() {
        return datosRepositorioPruebas.get("ClientePersonalizado");
    }

    public String GET_TOPE_MONTO() {
        return datosRepositorioPruebas.get("TopeMonto");
    }

    public String GET_ACUMULADO_MONTO() {
        return datosRepositorioPruebas.get("AcumuladoMonto");
    }

    public String GET_TOPE_NRO_TRX() {
        return datosRepositorioPruebas.get("TopeTRX");
    }

    public String GET_ACUMULADO_NRO_TRX() {
        return datosRepositorioPruebas.get("AcumuladoTRX");
    }

    public String GET_ALERTA() {
        return datosRepositorioPruebas.get("Alertas");
    }

    public String GET_CONSULTA_DE_COSTO() {
        return datosRepositorioPruebas.get("ConsultaCosto");
    }

    public String GET_TIPO_CUENTA() {
        return datosRepositorioPruebas.get("TipoCuenta");
    }

    public String GET_CUENTA_DESTINO() {
        return datosRepositorioPruebas.get("CuentaDestino");
    }

    public String GET_SALDO_CUENTA_DESTINO() {
        return datosRepositorioPruebas.get("SaldoCuentaDestino");
    }

    public String GET_ESTADO_CUENTA_DESTINO() {
        return datosRepositorioPruebas.get("EstadoCuentaDestino");
    }

    public String GET_CODIGO_CONVENIO() {
        return datosRepositorioPruebas.get("CodigoConvenio");
    }

    public String GET_CODIGO_ERROR_DESTINO() {
        return datosRepositorioPruebas.get("CodErrorDestino");
    }

    public String GET_TRANSACCION_DESTINO() {
        return datosRepositorioPruebas.get("TransaccionDestino");
    }

    public String GET_CANTIDAD_VALIDACIONES() {
        return datosRepositorioPruebas.get("CantidadValidaciones");
    }

    public String GET_CASO_APLICA() {
        return datosRepositorioPruebas.get("CasoAplica");
    }

    public String GET_TIPO_TARJETA() {
        return datosRepositorioPruebas.get("TipoTarjeta");
    }

    public String GET_ESTADO_INICIAL_PINFORZADO() {
        return datosRepositorioPruebas.get("EstadoInicialCliente_PinForzado");
    }
    public String GET_ESTADO_ESPERADO_PINFORZADO() {
        return datosRepositorioPruebas.get("EstadoEsperadoCliente_PinForzado");
    }
    public String GET_CODIGO_TRX_TOPES() {
        return datosRepositorioPruebas.get("CodigoTrxTopes");
    }

    //****************** PARA FIRST DATA ********************

    public String GET_RESPUESTA_FIRST_DATA() {
        return datosRepositorioPruebas.get("RespuestaFirstData");
    }

    public String GET_TRANSACCION_FIRST_DATA() {
        return datosRepositorioPruebas.get("TransaccionFirstData");
    }

    public String GET_TIPO_SOLICITUD_FIRST_DATA() {
        return datosRepositorioPruebas.get("TipoSolicitudFirstData");
    }

    public String GET_TIPO_SOLICITUD_DESTINO_FIRST_DATA() {
        return datosRepositorioPruebas.get("TipoSolicitudDestinoFirstData");
    }

    public String GET_DISPOSITIVO_FIRST_DATA() {
        return datosRepositorioPruebas.get("DispositivoFirstData");
    }

    public String GET_DISPOSITIVO_DESTINO_FIRST_DATA() {
        return datosRepositorioPruebas.get("DispositivoDestinoFirstData");
    }

    //******************* PARA MULTIFUNCIONAL *******************

    public String GET_IND_DEVOLUCION_MULTI () {
        return datosRepositorioPruebas.get("DevolucionMulti");
    }

    public String GET_CUENTA_DEVOLUCION_MULTI() {
        return datosRepositorioPruebas.get("CuentaDevolucionMulti");
    }

    public String GET_TIPO_CUENTA_MULTI() {
        return datosRepositorioPruebas.get("TipoCuentaMulti");
    }

    public String GET_VALOR_DEVOLUCION_MULTI() {
        return datosRepositorioPruebas.get("ValorDevolucionMulti");
    }

    public String GET_CONSIGNACION_EFECTIVO_MULTI() {
        return datosRepositorioPruebas.get("ConsignacionEfectivoMulti");
    }

    public String GET_ESTADO_CUENTA_DEVOLUCION() {
        return datosRepositorioPruebas.get("EstadoCuentaDevolucion");
    }

    //'****************** PARA AUTHENTIC ********************

    public String GET_TRX_TYPE() {
        return datosRepositorioPruebas.get("TrxType");
    }

    public String GET_ACTION_CODE() {
        return datosRepositorioPruebas.get("Action_Code");
    }

    public String GET_AUTORISED_BY() {
        return datosRepositorioPruebas.get("Authorised_by");
    }

    public String GET_MESSAGE_TYPE() {
        return datosRepositorioPruebas.get("Message_Type");
    }

    public String GET_AUTHENTIC_INTERNAL_CODE() {
        return datosRepositorioPruebas.get("Authentic_internal_code");
    }

    public String GET_TRX_TYPE_COSTO() {
        return datosRepositorioPruebas.get("TrxTypeCosto");
    }

    public String GET_MESSAGE_TYPE_COSTO() {
        return datosRepositorioPruebas.get("Message_TypeCosto");
    }

    public String GET_AUTHENTIC_INTERNAL_CODE_COSTO() {
        return datosRepositorioPruebas.get("Authentic_internal_code_Costo");
    }

    public String GET_ACTION_CODE_COSTO() {
        return datosRepositorioPruebas.get("Action_Code_Costo");
    }

    public String GET_AUTORISED_BY_COSTO() {
        return datosRepositorioPruebas.get("Authorised_by_Costo");
    }

    public String GET_ACTION_CODE_PIN_FORZADO() {
        return datosRepositorioPruebas.get("Action_Code_Forzada");
    }

    public String GET_ASEGURAR_RETIRO() {
        return datosRepositorioPruebas.get("Asegurar_Retiro");
    }
    public String GET_TRX_TYPE_AR() {
        return datosRepositorioPruebas.get("Trx_Type_AR");
    }
    public String GET_ACTION_CODE_AR() {
        return datosRepositorioPruebas.get("Action_Code_AR");
    }
    public String GET_AUTORISED_BY_AR() {
        return datosRepositorioPruebas.get("Authorised_by_AR");
    }
    public String GET_MESSAGE_TYPE_AR() {
        return datosRepositorioPruebas.get("Message_Type_AR");
    }
    public String GET_AUTHENTIC_INTERNAL_CODE_AR() {
        return datosRepositorioPruebas.get("Authentic_Internal_Code_AR");
    }
    public String GET_TRX_CAMBIO_CLAVE() {
        return datosRepositorioPruebas.get("Transaccion_Cambio_Clave");
    }

    public String GET_VENTAS_ATM() { return datosRepositorioPruebas.get("Ventas");    }
    public String GET_TRX_TYPE_VENTAS() { return datosRepositorioPruebas.get("Trx_Type_Vta");  }
    public String GET_ACTION_CODE_VENTAS() { return datosRepositorioPruebas.get("Action_Code_Vta"); }
    public String GET_AUTORISED_BY_VENTAS() { return datosRepositorioPruebas.get("Authorised_by_Vta"); }
    public String GET_MESSAGE_TYPE_VENTAS() { return datosRepositorioPruebas.get("Message_Type_Vta");  }
    public String GET_AUTHENTIC_INTERNAL_CODE_VENTAS() { return datosRepositorioPruebas.get("Authentic_Internal_Code_Vta"); }


    //'****************** PARA MENSAJES EN PANTALLA OCR ********************
    public String GET_MENSAJE_PANTALLA(String id) throws SQLException, IOException {
        this.getMensajesPantalla(id, "TBL_MENSAJES_PANTALLA_FENIX");
       // this.getMensajesPantalla(id, "TBL_MENSAJES_PANTALLA_FENIX_SIN_Duplicados");
        return this.datosMensajesPantalla.get("MENSAJE_PANTALLA");
    }

    //'****************** PARA MOVIMIENTOS ROBOT ********************

    public String GET_MOVIMIENTO_ROBOT(String id) throws SQLException, IOException {
        boolean isCoBot = Boolean.parseBoolean(typeNew2);

        if (isCoBot)
        {
            this.getMovimientosRobot(id, "MOVIMIENTO_ROBOT2");
        }
        else
        {
            this.getMovimientosRobot(id, "MOVIMIENTO_ROBOT");
        }
        return this.datosMovimientosRobot.get("Nombre_Movimiento");
    }
}