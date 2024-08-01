package framework.custom.utils.ocr;

import com.github.sarxos.webcam.Webcam;
import info.debatty.java.stringsimilarity.JaroWinkler;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static framework.custom.procedures.OCRCapturer.ACCURACY_PERCENT;
import static java.awt.SystemColor.info;

public class OCRTesseractUtils {

    Logger logger = Logger.getLogger(OCRTesseractUtils.class.getName());

    public static final String ACCEPT ="\\Ã¡-\\Ã©-\\Ã­-\\Ã³-\\Ãº";

    public static OCRTesseractUtils util(){return new OCRTesseractUtils();}


    public String getTextFromWebcam(Webcam webcam, String ruta) throws InterruptedException
    {


        BufferedImage im = webcam.getImage();
        File outfile = new File(ruta);
        try {
            ImageIO.write(im, "jpg", outfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread.sleep(100);

        ITesseract instance = new Tesseract();
        instance.setDatapath("C:\\XCELERA-javaframework-Bancolombia CO-BOT v1.0\\tessdata");
        try{
            instance.setLanguage("spa");
            return instance.doOCR(im);
        }catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }


    public double getSimilarity(String deseado, String obtenido)
    {
        String[] objetCleanText = obtenido.split("\n");
        String[] textWished = getWordFromDB(deseado);
        ArrayList<Double> resultSim = new ArrayList<>();
        int ñ=0;
        for (String s : textWished) {
            for (String line : objetCleanText) {
                String cleanText = line.replaceAll("[^A-Za-z0-9" + ACCEPT + "]", "");
                double result = JaroWinkler.Similarity(cleanText, s);
                if (result > ACCURACY_PERCENT) {
                    logger.log(Level.INFO,() ->"\n"
                            + "obtenido en linea: " + cleanText + "\n"
                            + "deseado en linea: " + s + "\n"
                            + "simulitud en linea: " + result
                    );
                    resultSim.add(result);
                    break;
                }else{
                    if(ñ<3) {
                        logger.log(Level.INFO, () -> "\n"
                                + "******************LINE NOT FOUND*********************" + "\n"
                                + "obtenido en linea: " + cleanText + "\n"
                                + "deseado en linea: " + s + "\n"
                                + "*****************************************************"
                        );
                        ñ++;
                    }
                }
            }
        }
        double sum = resultSim.stream().mapToDouble(a -> a).sum();
        double avg = sum/textWished.length;
        logger.log(Level.INFO,() ->"El promedio obtenido de similutud es: " + avg);
        return avg;
    }

    private String[] getWordFromDB(String text){
        String clean = text.replace(" ","");
        return clean.split(";");
    }
}
