package framework.custom.procedures;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import framework.custom.procedures.fm_pattern_procedures.IPerformableProcedure;
import framework.custom.utils.ocr.OCRTesseractUtils;
import framework.data.dynamicValues.DynamicValuesHelper;
import framework.data.entities.ExecutionData;
import framework.data.entities.Procedure;
import framework.enums.TypeError;
import framework.helpers.ExecutionStatusHelper;
import framework.helpers.GeneralHelper;
import framework.helpers.ScreenshotHelper;
import org.apache.xmlbeans.impl.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static framework.custom.procedures.ProcedureConstants.MOVEMENT_SACARTARJETA;

public class OCRCapturer implements IPerformableProcedure {

    Logger logger=Logger.getLogger(OCRCapturer.class.getName());

    private final ExecutionData testToExecute;
    private final Procedure procedure;
    private final ScreenshotHelper screenshotHelper;
    private final Webcam webcam;

    private final String mensaje = "";
    public static int diff=0;
    public static final double ACCURACY_PERCENT = 0.7;


    public OCRCapturer(ExecutionData testToExecute,
                       Procedure procedure,
                       ScreenshotHelper screenshotHelper) {
        this.testToExecute = testToExecute;
        this.procedure = procedure;
        this.screenshotHelper=screenshotHelper;
        this.webcam = Webcam.getDefault();
        if (!this.webcam.isOpen()) {
            this.webcam.setViewSize(WebcamResolution.VGA.getSize());
            this.webcam.open();
        }

    }

    public static boolean openCamWebMonitor(){
        //inicio de ventana para monitoreo de webcam
        JFrame ventana = new JFrame();
        JPanel p = new JPanel();
        ventana.setBounds(0, 0, 700, 500);
        ventana.setLocationRelativeTo(null);
        //640, 450
        WebcamPanel panel = new WebcamPanel(Webcam.getDefault(),new Dimension(640,450), false);
        panel.setFillArea(true);
        p.setLayout(new FlowLayout());
        p.add(panel);
        panel.start();
        ventana.add(p);
        ventana.setVisible(false);

        //Fin de inicio de ventana para monitoreo de webcam
        return ventana.isVisible();
    }

    @Override
    public void executeCustomProcedure() throws Exception {

        DynamicValuesHelper dvh = new DynamicValuesHelper();
        ExecutionStatusHelper helper = new ExecutionStatusHelper();


        Date fechaInicio = new Date();
        Date fechaFin;
        int timeMaximo = 40 ;
        setDiff(0);

        String pathImage = GeneralHelper.getPrintPath(testToExecute);

        String valorCompara = dvh.captureProp(procedure.Value,"Valor_Compara",true);
        logger.log(Level.INFO,() ->valorCompara);
        boolean result = false;

        while (getDiff() < timeMaximo && !result)
        {
           // do {
                String obtenido = OCRTesseractUtils.util().getTextFromWebcam(webcam, pathImage);

                double simil = OCRTesseractUtils.util().getSimilarity(valorCompara, obtenido);

                if (simil >= ACCURACY_PERCENT) {
                    result = true;
                }
           // }while(result);

            fechaFin = new Date();
            int auxDiff = (int) (fechaFin.getTime()-fechaInicio.getTime())/1000;
            setDiff(auxDiff);
            logger.log(Level.INFO,() ->"Espera Funci�n OCR (segundos): "+getDiff());
        }


        if(result) {
            sendPrintToXcelera(pathImage);
            String comment="El texto: "+valorCompara+" ha sido encontrado. ";
            helper.setStatusLogOkProcedure(procedure,comment);
        }
        else {
            logger.log(Level.WARNING,() ->"\n********************************\n>>> ERROR: ERROR, Mensaje no encontrado\nMensaje encontrado:"+mensaje+"\nMensaje esperado:"+valorCompara+"\n*******************************\n");
            if(RobotExecutor.getStatusSacarTarjeta()){
                String tipo = RobotExecutor.getTipoTarjeta();
                RobotExecutor.getInstance(MOVEMENT_SACARTARJETA, tipo).executeCustomProcedure();
            }
            String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
            String comment="No se encontr� el texto: "+valorCompara+". ";
            String commentFinal = GeneralHelper.getCommentError(methodName, comment);

            sendPrintToXcelera(pathImage);
            helper.setStatusErrorOnProcedure(procedure, TypeError.OperationalError, commentFinal);
            throw new Exception();
        }
    }

    public static void setDiff(int diff){OCRCapturer.diff=diff;}

    public static int getDiff(){return OCRCapturer.diff;}

    private void sendPrintToXcelera(String pathImage) throws Exception {
        //Send print to report - Xcelera
        BufferedImage captureOfCam = ImageIO.read(new File(pathImage));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(captureOfCam, "PNG", baos);

        screenshotHelper.setPrintSaved(true);
        screenshotHelper.setValueToPrintAction(new String(Base64.encode(baos
                .toByteArray()), StandardCharsets.UTF_8));
        //Finished send print to report - Xcelera
    }
}