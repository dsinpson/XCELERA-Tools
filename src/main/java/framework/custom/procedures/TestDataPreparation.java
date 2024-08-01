package framework.custom.procedures;

import framework.custom.procedures.fm_pattern_procedures.IPerformableProcedure;
import framework.custom.tasks.iseries.*;
import framework.custom.tasks.iseries.myextra.RestartGaen;
import framework.data.dynamicValues.DynamicValuesCustom;
import framework.data.entities.Procedure;
import framework.enums.TypeError;
import framework.helpers.ExecutionStatusHelper;
import framework.helpers.GeneralHelper;
import framework.helpers.ScreenshotHelper;

import java.util.logging.Level;
import java.util.logging.Logger;

import static framework.custom.procedures.ProcedureConstants.*;

public class TestDataPreparation implements IPerformableProcedure {
    DynamicValuesCustom dmc = new DynamicValuesCustom();
    Logger logger=Logger.getLogger(TestDataPreparation.class.getName());

    private final ScreenshotHelper screenshotHelper;
    private final Procedure procedure;

    String tipoTransaccion;
    String casoAplica;

    public TestDataPreparation(Procedure procedure, ScreenshotHelper screenshotHelper) {
        this.screenshotHelper=screenshotHelper;
        this.procedure=procedure;
    }

    @Override
    public void executeCustomProcedure() throws Exception {

        ExecutionStatusHelper helper = new ExecutionStatusHelper();
        casoAplica = dmc.GET_CASO_APLICA();
        tipoTransaccion = dmc.GET_TIPO_TRANSACCION();
        logger.log(Level.INFO,() ->"* * * CASO PARA EL QUE APLICA ESTA PRUEBA: "+ casoAplica);
        logger.log(Level.INFO,() ->"En validarAmbientes TipoTransaccion "+ tipoTransaccion);
        String indicadorDevolucion = dmc.GET_IND_DEVOLUCION_MULTI();
        tipoTransaccion = dmc.GET_TIPO_TRANSACCION();
        String tipoTarjeta = dmc.GET_TIPO_TARJETA();
        logger.log(Level.INFO,() ->"Tipo_Transaccion en PreparaDatosPrueba "+ tipoTransaccion);
        logger.log(Level.INFO,() ->"Tipo transacción: "+ tipoTransaccion);
        logger.log(Level.INFO,() ->"Tipo tarjeta: "+ tipoTarjeta);

        try {

            //*************************PREPARACION DE DATA PARA CREDITO
            if (tipoTarjeta.equals(CARD_TYPE_CREDIT) || tipoTarjeta.equals(CARD_TYPE_PREPAGO)) {
                logger.log(Level.INFO,() ->"entro preparacion de data credito");
                logger.log(Level.INFO, () -> dmc.GET_NUMERO_TARJETA());
                //TODO pending finish FirstData
            /*
            saldoInicial = fd.consultaARIQ(dmc.GET_NUMERO_TARJETA());
            System.out.println(saldoInicial);
            fd.consultaARMB(dmc.GET_NUMERO_CUENTA(), dmc.GET_ESTADO_CUENTA());
             */
                if (tipoTarjeta.equals(CARD_TYPE_CREDIT)) {
                    PrepareForcedPinCardStatus.
                            with(dmc.GET_ESTADO_INICIAL_PINFORZADO(),dmc.GET_NUMERO_TARJETA()).perform();
                }
            }
            if (tipoTarjeta.equals(CARD_TYPE_CREDIT_OUTSERVICE)) {
                logger.log(Level.INFO,() ->"No Realiza Preparación de Data");
                logger.log(Level.INFO,() -> dmc.GET_NUMERO_TARJETA());
                PrepareForcedPinCardStatus.
                        with(dmc.GET_ESTADO_INICIAL_PINFORZADO(),dmc.GET_NUMERO_TARJETA()).perform();
            }
            //************************ FIN PREPARACION DATA CREDITO

            if (tipoTransaccion.equals(TRANS_TYPE_AHORRO_A_LA_MANO)) {
                PrepareBanOnHand.
                        with(dmc.GET_NUMERO_CUENTA(), dmc.GET_NUMERO_PIN(), dmc.GET_MINUTOS_TRX(), dmc.GET_ACUM_TOPE_MES()).perform();

                PrepareBalance.
                        with(dmc.GET_MONTO_INICIAL(), dmc.GET_NUMERO_CUENTA(), dmc.GET_ESTADO_CUENTA()).perform();
                PrepareAccountStatus.
                        with(dmc.GET_ESTADO_CUENTA(), dmc.GET_NUMERO_CUENTA()).perform();
            }
            else if ((tipoTransaccion.equals(TRANS_TYPE_CONSIGNACION_MULTI)
                    || tipoTransaccion.equals(TRANS_TYPE_PAGO_CONVENIO_MULTI)
                    ||  tipoTransaccion.equals(TRANS_TYPE_PAGO_TARJETA_EMPRESARIAL_MULTI)
                    ||  tipoTransaccion.equals(TRANS_TYPE_PAGO_CODIGO_DE_BARRAS_MULTI)
                    ||  tipoTransaccion.equals(TRANS_TYPE_PAGO_CUENTA_RECAUDADORA_MULTI))
            ) {
                PrepareBalance.
                        with(dmc.GET_MONTO_INICIAL(), dmc.GET_NUMERO_CUENTA(), dmc.GET_ESTADO_CUENTA()).perform();
                PrepareAccountStatus.
                        with(dmc.GET_ESTADO_CUENTA(), dmc.GET_NUMERO_CUENTA()).perform();
                if (indicadorDevolucion.equals(STATUS_A)){
                    PrepareBalance.
                            with(dmc.GET_MONTO_INICIAL(), dmc.GET_CUENTA_DEVOLUCION_MULTI(), dmc.GET_ESTADO_CUENTA_DEVOLUCION()).perform();
                    PrepareAccountStatus.
                            with(dmc.GET_ESTADO_CUENTA_DEVOLUCION(), dmc.GET_CUENTA_DEVOLUCION_MULTI()).perform();
                }
            }
            else if (tipoTransaccion.equals(TRANS_TYPE_RETIRO_CUENTA)
                    || tipoTransaccion.equals(TRANS_TYPE_ULTIMOS_MOVIMIENTOS)
                    || tipoTransaccion.equals(TRANS_TYPE_TRANSFERENCIAS)
                    || tipoTransaccion.equals(TRANS_TYPE_CONSULTA_DE_SALDO)
                    || tipoTransaccion.equals(TRANS_TYPE_CAMBIO_DE_CLAVE)
                    || tipoTransaccion.equals(TRANS_TYPE_PAGOS_TD_CTA_RECAUDADORA)
                    || tipoTransaccion.equals(TRANS_TYPE_PAGO_TD_AHORROS_CORRIENTE)
                    || tipoTransaccion.equals(TRANS_TYPE_PAGO_TARJETA_EMPRESARIAL)
                    || tipoTransaccion.equals(TRANS_TYPE_PAGO_CODIGO_DE_BARRAS)
                    || tipoTransaccion.equals(TRANS_TYPE_TOPES_PERSONALIZADOS)
                    || tipoTransaccion.equals(TRANS_TYPE_TOPES_CANAL)) {
                logger.log(Level.INFO,() -> "****Entra a retiro cuenta");
                PrepareBalance.
                        with(dmc.GET_MONTO_INICIAL(), dmc.GET_NUMERO_CUENTA(), dmc.GET_ESTADO_CUENTA()).perform();
                PrepareAccountStatus.
                        with(dmc.GET_ESTADO_CUENTA(), dmc.GET_NUMERO_CUENTA()).perform();
                PrepareCardStatus.
                        with(dmc.GET_ESTADO_TARJETA(), dmc.GET_NUMERO_TARJETA()).perform();
                PreparePasswordStatus.
                        with(dmc.GET_NUMERO_INTENTOS(), dmc.GET_ESTADO_CLAVE(), dmc.GET_NUMERO_TARJETA()).perform();
                PrepareForcedPinCardStatus.
                        with(dmc.GET_ESTADO_INICIAL_PINFORZADO(),dmc.GET_NUMERO_TARJETA()).perform();

                if (dmc.GET_CLIENTE_PERSONALIZADO().equals(STATUS_S)) {
                    logger.log(Level.INFO,() -> "Entra clientes personalizados");
                    PrepareMonetaryCap.
                            with(dmc.GET_TOPE_MONTO(), dmc.GET_NUMERO_TARJETA(), dmc.GET_CODIGO_TRX_TOPES()).perform();
                    PrepareMonetaryAccumulated.
                            with(dmc.GET_ACUMULADO_MONTO(), dmc.GET_NUMERO_TARJETA(), dmc.GET_CODIGO_TRX_TOPES()).perform();
                    PrepareTransactionCap.
                            with(dmc.GET_TOPE_NRO_TRX(), dmc.GET_NUMERO_TARJETA(), dmc.GET_CODIGO_TRX_TOPES()).perform();
                    PrepareTransactionAccumulated.
                            with(dmc.GET_ACUMULADO_NRO_TRX(), dmc.GET_NUMERO_TARJETA(), dmc.GET_CODIGO_TRX_TOPES()).perform();
                }

                if (tipoTransaccion.equals(TRANS_TYPE_TRANSFERENCIAS)
                        || tipoTransaccion.equals(TRANS_TYPE_PAGOS_TD_CTA_RECAUDADORA)
                        || tipoTransaccion.equals(TRANS_TYPE_PAGO_TD_AHORROS_CORRIENTE)
                        || tipoTransaccion.equals(TRANS_TYPE_PAGO_TARJETA_EMPRESARIAL)
                        || tipoTransaccion.equals(TRANS_TYPE_PAGO_CODIGO_DE_BARRAS)) {
                    PrepareBalance.
                            with(dmc.GET_MONTO_INICIAL(), dmc.GET_CUENTA_DESTINO(), dmc.GET_ESTADO_CUENTA_DESTINO()).perform();
                    PrepareAccountStatus.
                            with(dmc.GET_ESTADO_CUENTA_DESTINO(), dmc.GET_CUENTA_DESTINO()).perform();
                    if (tipoTransaccion.equals(TRANS_TYPE_PAGO_TD_AHORROS_CORRIENTE)
                            || tipoTransaccion.equals(TRANS_TYPE_PAGOS_TD_CTA_RECAUDADORA)
                            || tipoTransaccion.equals(TRANS_TYPE_PAGO_TARJETA_EMPRESARIAL)
                            || tipoTransaccion.equals(TRANS_TYPE_PAGO_CODIGO_DE_BARRAS)) {
                        PrepareAgreementStatus.
                                with(dmc.GET_ESTADO_CUENTA_DESTINO(), dmc.GET_CODIGO_CONVENIO()).perform();
                    }
                }

                if (dmc.GET_ALERTA().equals(STATUS_S))
                {
                    // this is pending to finish
                    RestartGaen.task().perform();
                    logger.log(Level.INFO,() -> "Por favor espere unos segundos...");
                    Thread.sleep(2000);
                }
                logger.log(Level.INFO,() -> "Termina Ambientes retiro cuenta");
            }
        }catch (Exception e){
            String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
            String mensaje = "Fallo la preparacion de data ->" + e.getMessage();
            String comment = GeneralHelper.getCommentError(methodName, mensaje);

            screenshotHelper.takeScreenshot();
            helper.setStatusErrorOnProcedure(procedure, TypeError.OperationalError, comment);
        }
    }
}
