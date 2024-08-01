package framework.custom.procedures;


import framework.custom.utils.AppProperties;

public class ProcedureConstants {

    private ProcedureConstants(){

    }
    //status
    public static final String STATUS_S= "S";
    public static final String STATUS_I= "I";
    public static final String STATUS_A= "A";
    public static final String EMPTY= "";

    //cards type
    public static final String CARD_TYPE_CREDIT = "CREDITO";
    public static final String CARD_TYPE_CREDIT_OUTSERVICE = "CREDITO_OUTSERVICE";
    public static final String CARD_TYPE_PREPAGO = "PREPAGO";
    public static final String CARD_TYPE_DEBIT = "DEBITO";
    public static final String CARD_TYPE_DCC = "DCC";

    //transactions type
    public static final String TRANS_TYPE_RETIRO_CUENTA ="RETIRO_CUENTA";
    public static final String TRANS_TYPE_TRANSFERENCIAS="TRANSFERENCIAS";
    public static final String TRANS_TYPE_CAMBIO_DE_CLAVE="CAMBIO_DE_CLAVE";
    public static final String TRANS_TYPE_ULTIMOS_MOVIMIENTOS="ULTIMOS_MOVIMIENTOS";
    public static final String TRANS_TYPE_AHORRO_A_LA_MANO="AHORRO_A_LA_MANO";
    public static final String TRANS_TYPE_FRONT="FRONT";
    public static final String TRANS_TYPE_OTRASREDES="OTRASREDES";
    public static final String TRANS_TYPE_TOPES_PERSONALIZADOS="TOPES_PERSONALIZADOS";
    public static final String TRANS_TYPE_TOPES_CANAL="TOPES_CANAL";
    public static final String TRANS_TYPE_TOPES_NO_DATA="TOPES_NO_DATA";
    public static final String TRANS_TYPE_AHORRO_NO_DATA="AHORRO_NO_DATA";
    public static final String TRANS_TYPE_CONSULTA_DE_SALDO="CONSULTA_DE_SALDO";
    public static final String TRANS_TYPE_PAGOS_TD_CTA_RECAUDADORA="PAGOS_TD_CTA_RECAUDADORA";
    public static final String TRANS_TYPE_PAGO_TD_AHORROS_CORRIENTE="PAGO_TD_AHORROS_CORRIENTE";
    public static final String TRANS_TYPE_PAGO_TARJETA_EMPRESARIAL="PAGO_TARJETA_EMPRESARIAL";
    public static final String TRANS_TYPE_PAGO_CODIGO_DE_BARRAS="PAGO_CODIGO_DE_BARRAS";
    public static final String TRANS_TYPE_PAGO_CONVENIO_MULTI="PAGO_CONVENIO_MULTI";
    public static final String TRANS_TYPE_PAGO_TARJETA_EMPRESARIAL_MULTI="PAGO_TARJETA_EMPRESARIAL_MULTI";
    public static final String TRANS_TYPE_PAGO_CODIGO_DE_BARRAS_MULTI="PAGO_CODIGO_DE_BARRAS_MULTI";
    public static final String TRANS_TYPE_PAGO_CUENTA_RECAUDADORA_MULTI="PAGO_CUENTA_RECAUDADORA_MULTI";
    public static final String TRANS_TYPE_CONSIGNACION_MULTI="CONSIGNACION_MULTI";
    public static final String TRANS_TYPE_CUENTAS_INSCRITAS="CUENTAS_INSCRITAS";

    //belong to AuthenticValidation.class
    public static final String ISERIES = "iSeries";
    public static final String SUFIJO = "sufijo: ";
    public static final String VALOR_TRX_TYPE = " El valor de TrxType es: ";
    public static final String QUERY_SQL = "Query SQL: ";
    public static final String VALOR_MESSAGE_TYPE = " El valor de Message_Type es: ";
    public static final String VALOR_AUTHENTIC_INTERNAL_CODE = " El valor de Authentic_internal_code es: ";
    public static final String VALOR_ACTION_CODE =  "El valor de Action_Code es: ";
    public static final String INFORMATION_ACTION_CODE = "La informacion de transaccion no corresponde. El valor de Action_Code esperado es ";
    public static final String GOT_AUTHENTIC_FROM =" y el obtenido desde Authentic es: ";
    public static final String VALOR_AUTHORIZED_BY = "El valor de Authorised_by es: ";
    public static final String UNEXPECTED_VALUE = "La informacion de transaccion no corresponde. El valor de Authorised_by esperado es ";
    public static final String TRX_QUALIFIER_VENTAS = "N";


    //belong to AuthenticEnvironmentValidation.class
    public static final String VALIDATION_ENVIRONMENT_RESULT_IS ="El resultado de validar ambientes es ";
    public static final String VALIDATION_NOTIFICATION_RESULT_IS ="El resultado de validar servidor notificaciones es: ";
    public static final String OPERATIONAL_OK = " se encuentran operativas. ";
    public static final String INACTIVE_SERVICES = "SERVIDORES ** INACTIVOS **";
    public static final String OFFLINE = " se encuentran fuera de linea. Detalle: ";

    //belong to RobotExecutor.class
    public static final String MOVEMENT_ACERCARATECLADO = "ACERCARATECLADO";
    public static final String MOVEMENT_TECLA = "TECLA";
    public static final String MOVEMENT_SEGURIDAD = "SEGURIDAD";
    public static final String MOVEMENT_ANOTACION = "ANOTACION";
    public static final String MOVEMENT_SACARTARJETA = "SACARTARJETA";

    //belong to CardConfiguration.class
    public static final String DIRECTORY_SWITCH_CARD = AppProperties.getRutaSwitchCards();

}
