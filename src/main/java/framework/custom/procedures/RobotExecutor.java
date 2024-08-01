package framework.custom.procedures;

import framework.custom.models.robot.RobotConfiguration;
import framework.custom.procedures.fm_pattern_procedures.IPerformableProcedure;
import framework.custom.tasks.robot.ExecuteNumpad;
import framework.custom.tasks.robot.SendCommandToArm;
import framework.custom.tasks.robot.SendCommandToArmWithAxes;
import framework.custom.tasks.robot.SendCommandToModules;
import framework.custom.utils.robot.SendArduinoCommand;
import framework.data.dynamicValues.DynamicValuesCustom;
import framework.dataProviders.ConfigFileReader;
import framework.custom.utils.cobot.ClientCobotExecutor;

import java.util.logging.Level;
import java.util.logging.Logger;

import static framework.custom.procedures.ProcedureConstants.*;

public class RobotExecutor implements IPerformableProcedure {

    Logger logger = Logger.getLogger(RobotExecutor.class.getName());

    private final String nombreMovimiento;
    private final String tipo;
    public static String tip;
    public static boolean sacarTarjeta = false;


    public RobotExecutor(String nombreMovimiento, String tipo) {
        this.nombreMovimiento = nombreMovimiento;
        this.tipo = tipo;
    }

    public static RobotExecutor getInstance(String nombreMovimiento, String tipo) {
        return new RobotExecutor(nombreMovimiento, tipo);
    }

    @Override
    public void executeCustomProcedure() throws Exception {

        DynamicValuesCustom dmc = new DynamicValuesCustom();
        RobotConfiguration rc;
        ClientCobotExecutor ap = new ClientCobotExecutor();
        ConfigFileReader f = new ConfigFileReader("configs/config.properties");
        String typeNew = f.getPropertyByKey("IsModularRobot");
        String typeNew2 = f.getPropertyByKey("IsCoBot");
        String typeNew3 = f.getPropertyByKey("IpRobot");

        logger.log(Level.INFO, () -> "Nombre Movimiento a ejecutar: " + nombreMovimiento);
        logger.log(Level.INFO, () -> "Tipo Movimiento a ejecutar: " + tipo);
        //Thread.sleep(3000);

        try {
            if (nombreMovimiento.equalsIgnoreCase("INSERTARTARJETA")) {
                setStatusSacarTarjeta(true);
                setTipoTarjeta(tipo);

            } else if (nombreMovimiento.equalsIgnoreCase("SACARTARJETA")) {
                setStatusSacarTarjeta(false);
            }

            boolean isModularRobot = Boolean.parseBoolean(typeNew);
            boolean isCoBot = Boolean.parseBoolean(typeNew2);
            if (isCoBot) {


                ap.cobotExecutor(nombreMovimiento,tipo);
                /*
                if (tipo.equals("TecladoNumerico") && nombreMovimiento.length() >= 3) {
                    //Ir a punto intermedio
                    String nomMovimiento = nombreMovimiento;
                    ap.consumeApi(nomMovimiento);
                    System.out.println("Espera movimiento");
                    Thread.sleep(5000);

                    //Acercar al teclado
                    jsonInputString = String.format("{\"command\": \"play\", \"type\": \"movement\", \"name\": \"%s\"}",
                            "acercartec");
                    ap.consumeApi(typeNew3, jsonInputString);
                    System.out.println("Espera movimiento");
                    Thread.sleep(5000);


                    for (int i = 0; i < (nombreMovimiento.length()); i++) {
                        char numTecla = nombreMovimiento.charAt(i);
                        jsonInputString = String.format("{\"command\": \"play\", \"type\": \"movement\", \"name\": \"%s\"}",
                                "tecla" + numTecla);
                        ap.consumeApi(typeNew3, jsonInputString);
                        System.out.println("Espera movimiento");
                        Thread.sleep(5000);
                    }

                    //Presionar anotacion
                    jsonInputString = String.format("{\"command\": \"play\", \"type\": \"movement\", \"name\": \"%s\"}",
                            "anotacion");
                    ap.consumeApi(typeNew3, jsonInputString);
                    System.out.println("Espera movimiento");
                    Thread.sleep(5000);

                    //Acercar al teclado
                    jsonInputString = String.format("{\"command\": \"play\", \"type\": \"movement\", \"name\": \"%s\"}",
                            "acercartec");
                    ap.consumeApi(typeNew3, jsonInputString);
                    System.out.println("Espera movimiento");
                    Thread.sleep(5000);
                    jsonInputString = String.format("{\"command\": \"play\", \"type\": \"movement\", \"name\": \"%s\"}",
                            "inter");
                    ap.consumeApi(typeNew3, jsonInputString);
                    System.out.println("Espera movimiento");
                    Thread.sleep(5000);
                }
                else if (nombreMovimiento.length() <= 3) {
                    System.out.println("Error nombre movimiento a ejecutar, verificar consulta DB");
                }
                else {
                    String jsonInputString = String.format("{\"command\": \"play\", \"type\": \"sequence\", \"name\": \"%s\"}",
                            nombreMovimiento);
                    ap.consumeApi(typeNew3, jsonInputString);
                    System.out.println("Espera movimiento");
                    if (nombreMovimiento.equals("CLAVE7890") || nombreMovimiento.equals("CLAVE0987") || nombreMovimiento.equals("BOTON8MONTO") || nombreMovimiento.equals("MONTO40MIL") || nombreMovimiento.equals("PINCLIENTEPERSONALIZADO(397137")) {
                        System.out.println("Espera clave");
                        Thread.sleep(20000);
                    }
                    else {
                        Thread.sleep(5000);
                    }
                }
                */
            }
            else {

                if (isModularRobot) {

                    rc = new RobotConfiguration.Builder()
                            .setServerIP(dmc.GET_IP_MOD_TECLADO())
                            .setPathMovesArm(dmc.GET_RUTA_ARCHIVO_STARC())
                            .setPathMovesAxes(dmc.GET_RUTA_ARCHIVO_STARCG())
                            .setIpModBotones1(dmc.GET_IP_MOD_BOTONES_1())
                            .setIpModBotones2(dmc.GET_IP_MOD_BOTONES_2())
                            .setIpModTeclado(dmc.GET_IP_MOD_TECLADO())
                            .setIpModTarjeta(dmc.GET_IP_MOD_TARJETA())
                            .setIpModRecibo(dmc.GET_IP_MOD_RECIBO())
                            .build();

                    if (tipo.equalsIgnoreCase("otro")) {
                        SendCommandToModules.
                                with(nombreMovimiento, rc).perform();
                    }
                    else if (tipo.equalsIgnoreCase("sinanotacion")) {
                        ExecuteNumpad.with(nombreMovimiento, rc, false).perform();
                    }
                    else if (tipo.equalsIgnoreCase("tecladonumerico")) {
                        ExecuteNumpad.with(nombreMovimiento, rc, true).perform();
                    }
                }
                else {

                    rc = new RobotConfiguration.Builder()
                            .setServerIP(dmc.GET_IP_ROBOT())
                            .setPathMovesArm(dmc.GET_RUTA_ARCHIVO_STARC())
                            .setPathMovesAxes(dmc.GET_RUTA_ARCHIVO_STARCG())
                            .build();

                    String posicionTeclado = dmc.GET_COORDENADA_ROBOT();
                    String numeros = "";
                    String[] arrayNumeros;

                    if (tipo.equalsIgnoreCase("otro")) {
                        SendCommandToArmWithAxes.
                                with(nombreMovimiento, rc).perform();
                    }
                    else if (tipo.equalsIgnoreCase("sinanotacion")) {
                        String tecla = "";
                        numeros = nombreMovimiento;
                        numeros = numeros.replace("", "#").substring(1);
                        arrayNumeros = numeros.split("#");

                        for (int i = 0; i < (nombreMovimiento.length()); i++) {

                            if (i == nombreMovimiento.length() - 1) {

                                tecla = arrayNumeros[i];

                                //SendArduinoCommand.util().
                                //        send(posicionTeclado, 1, "", rc);
                                SendCommandToArm.
                                        with(MOVEMENT_TECLA + tecla, rc).perform();
                                SendCommandToArm.
                                        with(MOVEMENT_SEGURIDAD, rc).perform();
                            }
                            else {
                                tecla = arrayNumeros[i];
                                if (i == 0) {
                                    // SendArduinoCommand.util().
                                    //        send(posicionTeclado, 1, "", rc);
                                    SendCommandToArm.
                                            with(MOVEMENT_ACERCARATECLADO, rc).perform();
                                }
                                //SendArduinoCommand.util().
                                //      send(posicionTeclado, 1, "", rc);
                                SendCommandToArm.
                                        with(MOVEMENT_TECLA + tecla, rc).perform();
                            }
                        }
                    }
                    else if (tipo.equalsIgnoreCase("tecladonumerico")) {
                        String tecla;
                        numeros = nombreMovimiento;
                        numeros = numeros.replace("", "#").substring(1);

                        arrayNumeros = numeros.split("#");

                        for (int i = 0; i < (nombreMovimiento.length()); i++) {

                            if (i == nombreMovimiento.length() - 1) {

                                tecla = arrayNumeros[i];

                                //SendArduinoCommand.util().
                                //      send(posicionTeclado, 1, "", rc);
                                SendCommandToArm.
                                        with(MOVEMENT_TECLA + tecla, rc).perform();
                                SendCommandToArm.
                                        with(MOVEMENT_ANOTACION, rc).perform();
                            }
                            else {
                                tecla = arrayNumeros[i];
                                if (i == 0) {
                                    //  SendArduinoCommand.util().
                                    //        send(posicionTeclado, 1, "", rc);
                                    SendCommandToArm.
                                            with(MOVEMENT_ACERCARATECLADO, rc).perform();
                                }
                                //SendArduinoCommand.util().
                                //       send(posicionTeclado, 1, "", rc);
                                SendCommandToArm.
                                        with(MOVEMENT_TECLA + tecla, rc).perform();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, () -> "Problema en la ejecucion del robot - mensaje: " + e.getMessage() + e.getLocalizedMessage());
            throw e;
        }
    }

    public static boolean getStatusSacarTarjeta() {
        return RobotExecutor.sacarTarjeta;
    }
    public static String getTipoTarjeta() {
        return RobotExecutor.tip;
    }

    public static void setStatusSacarTarjeta(boolean sacarTarjeta) {
        RobotExecutor.sacarTarjeta = sacarTarjeta;
    }
    public static void setTipoTarjeta(String tip) {
        RobotExecutor.tip = tip;
    }

}
