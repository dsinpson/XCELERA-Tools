package framework.custom.utils;

public class UtilsConstants {

    private UtilsConstants(){}

    // BD Connections
    public static final String STR_CONNECTION_ISERIES = AppProperties.getStrConexionIseries();
    public static final String USER_ISERIES = AppProperties.getUserIseries();
    public static final String PASSWORD_ISERIES = AppProperties.getPasswordIseries();

    public static final String STR_CONNECTION_AUTHENTIC = AppProperties.getStrConexionAuthentic();
    public static final String USER_AUTHENTIC = AppProperties.getUserAuthentic();
    public static final String PASSWORD_AUTHENTIC = AppProperties.getPasswordAuthentic();

    public static final String CONNECTION_FAILURE = "Error: no se estableci� la conexion - mensaje: ";
    public static final String CONNECTION_NOT_CLOSED = "Error: no se cerr� la conexion - mensaje: ";
    public static final String DRIVER_NOT_FOUND = "Error: no encontr� el driver - mensaje: ";

    //Http Connections
    public static final String METHOD_POST = "POST";
    public static final String KEY_CONTENT_TYPE = "Content-Type";
    public static final String VALUE_APPLICATION_JSON = "application/json";

    //Starc Imagi
    public static final String RUTA_STARC_IMAGI_API = AppProperties.getRutaStarImagiApi();









}
