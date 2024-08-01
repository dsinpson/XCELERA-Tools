package framework.custom.questions.authentic;

import framework.custom.questions.IQuestion;
import framework.custom.utils.BaseDatosAplicacion;
import framework.custom.utils.ConexionAuthentic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticTransactionWithVentasStatus implements IQuestion<String[]> {
    Connection conexion;
    ResultSet rs;
    Logger logger=Logger.getLogger(AuthenticTransactionWithVentasStatus.class.getName());

    private final String idCajero;
    private final String trxType;
    private String messageType;
    private String authenticInternalCode;
    private final String ventasParam;

    public AuthenticTransactionWithVentasStatus(String idCajero,
                                                String trxType,
                                                String messageType,
                                                String authenticInternalCode,
                                                String ventasParam) {
        this.idCajero = idCajero;
        this.trxType = trxType;
        this.messageType = messageType;
        this.authenticInternalCode = authenticInternalCode;
        this.ventasParam = ventasParam;
    }

    public static AuthenticTransactionWithVentasStatus with(String idCajero,
                                                            String trxType,
                                                            String messageType,
                                                            String authenticInternalCode,
                                                            String ventasParam){
        return new AuthenticTransactionWithVentasStatus(idCajero,
                trxType, messageType, authenticInternalCode, ventasParam);
    }

    @Override
    public String[] ask() throws SQLException {
        BaseDatosAplicacion bda = new BaseDatosAplicacion();
        SimpleDateFormat year = new SimpleDateFormat("yyyy-MM-dd_");
        SimpleDateFormat hor = new SimpleDateFormat("HH");
        SimpleDateFormat min = new SimpleDateFormat("mm");
        SimpleDateFormat seg = new SimpleDateFormat("ss");
        String fechaHoy = year.format(new Date());
        String hora = hor.format(new Date());
        String minuto = min.format(new Date());

        int hori = Integer.parseInt(hora)-1;
        int minut = Integer.parseInt(minuto)-3;
        if (minut < 0){
            minut =57;
            hora = Integer.toString(hori);
        }
        minuto = Integer.toString(minut);
        if (minuto.length()<=1){
            minuto="0"+minuto;
        }
        if (hora.length()<=1){
            hora="0"+hora;
        }
        String segundo = seg.format(new Date());

        String traxType = "";
        String actionCode = "";
        String authorisedBy = "";
        String strQuery = "";

        String fechaHora = fechaHoy+hora+":"+minuto+":"+segundo;

        conexion = ConexionAuthentic.util().conectar();
        strQuery = "SELECT * FROM ("+
                "SELECT "+
                "A.TRL_TTY_ID as Trx_Type, "+
                "A.TRL_MESSAGE_TYPE as Message_Type, "+
                "A.TRL_TSC_CODE as Authentic_internal_code, "+
                "B.BCOL_FROM_ACCOUNT_TYPE as Account_Type_From, "+
                "B.BCOL_TO_ACCOUNT_TYPE as Account_Type_To, "+
                "B.BCOL_CARD_TYPE, "+
                "NVL(A.TRL_DESTINATION_RESULT_CODE,A.TRL_ACTION_RESPONSE_CODE) AS Action_Code, "+
                "C.ARC_NAME as Action_Name, "+
                "A.TRL_AMT_TXN, "+
                "A.TRL_AUTHORISED_BY, "+
                "TO_CHAR(A.TRL_DATETIME_LOCAL_TXN,'YYYY-MM-DD_HH24:MI:SS.'), "+
                "A.TRL_APPROVAL_CODE, "+
                "A.TRL_STAN, "+
                "A.TRL_RRN, "+
                "A.TRL_PIN_RETRY_COUNT, "+
                "A.TRL_CARD_STATUS_CODE, "+
                "A.TRL_CARD_ACPT_TERMINAL_IDENT, "+
                "A.TRL_CARD_ACPT_NAME_LOCATION, "+
                "A.TRL_STANDIN_REASON_INDICATOR  "+
                "FROM BCOLOMBIA_OWNER.TRANSACTION_LOG A  "+
                "LEFT JOIN BCOLOMBIA_OWNER.BCOL_TRANSACTION_LOG B ON A.TRL_ID = B.BCOL_TRL_ID "+
                "INNER JOIN BCOLOMBIA_OWNER.AUTH_RESULT_CODE C ON A.TRL_ACTION_RESPONSE_CODE = C.ARC_CODE "+
                "WHERE TO_CHAR(A.TRL_DATETIME_LOCAL_TXN,'YYYY-MM-DD_HH24:MI:SS') >= '%s' "+
                "AND A.TRL_CARD_ACPT_TERMINAL_IDENT = '%s' AND A.TRL_TTY_ID = %s "+
                "AND A.TRL_MESSAGE_TYPE = %s "+
                "AND A.TRL_TSC_CODE = %s AND A.TRL_TQU_ID = %s "+
                "ORDER BY A.TRL_DATETIME_LOCAL_TXN DESC "+
                ")WHERE ROWNUM = 1";

        String sentenciaSQLCompleta = String.format(strQuery, fechaHora, idCajero, trxType, messageType,  authenticInternalCode, ventasParam);
        rs = bda.ejecutarConsulta(sentenciaSQLCompleta, conexion);
        logger.log(Level.INFO,() ->sentenciaSQLCompleta);

        while (rs.next()) {
            traxType = rs.getString("TRX_TYPE");
            messageType = rs.getString("MESSAGE_TYPE");
            authenticInternalCode = rs.getString("AUTHENTIC_INTERNAL_CODE");
            actionCode = rs.getString("ACTION_CODE");
            authorisedBy = rs.getString("TRL_AUTHORISED_BY");
        }

        if (!authorisedBy.equals("authentic")) {
            if (actionCode.equals("0") || actionCode.equals("000") || actionCode.equals("0000")) {
                actionCode = "00";
            }
        }

        ConexionAuthentic.util().desconectar(conexion);
        String[] arrRespuesta = {traxType, messageType, authenticInternalCode, actionCode, authorisedBy, sentenciaSQLCompleta};
        return arrRespuesta;
    }
}
