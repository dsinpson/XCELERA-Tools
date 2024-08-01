package framework.helpers;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

import framework.custom.procedures.OCRCapturer;
import org.apache.xmlbeans.impl.util.Base64;
import framework.data.entities.Attribute;
import framework.data.entities.ExecutionConfig;
import framework.data.entities.ExecutionData;
import framework.data.entities.Procedure;
import framework.dataProviders.ConfigFileReader;

public class GeneralHelper {

	private static final boolean isWindows = System.getProperty("os.name").toLowerCase().contains("windows");

	public static String getDate() {
		return LocalDateTime.now().toString();
	}

	public static String getFormatValue(Procedure procedure) throws Exception {
		String defaultFormat = procedure.Object.Format;
		String value = procedure.Value;

		if (defaultFormat == null)
			return value;
		else
			return String.format(defaultFormat, value);
	}

	public static String now() throws Exception {
		return now("yyyy_MM_dd_HH_mm_ss");
	}

	public static String now(String formatStr) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat(formatStr); // $NON-NLS-1$
		java.util.Date date = new java.util.Date();

		return dateFormat.format(date);
	}

	public static String getPrintPath(ExecutionData testToExecute) throws Exception {
		String dir="dirDefaultPrints";
		return buildPath(testToExecute, dir) + now() + ".png"; //$NON-NLS-1$
	}
	public static String getLogPath(ExecutionData testToExecute) throws Exception {
		String dir="dirDefaultLogs";
		return buildPath(testToExecute, dir) + "Log_" + now() + ".txt"; //$NON-NLS-1$
	}
	public static String getPdfPath(ExecutionData testToExecute) throws Exception {
		String dir="dirDefaultLogs";
		return buildPath(testToExecute, dir) + "Evidencia.pdf"; //$NON-NLS-1$
	}

	public static String buildPath(ExecutionData testToExecute, String directory) throws Exception {
		ConfigFileReader reader = new ConfigFileReader("configs/config.properties");
		String separator = System.getProperty("file.separator");
		String dirDefaultFolderPrints = reader.getPropertyByKey(directory);
		if (!isWindows) {
			// Ajuste para trabalhar com LINUX ou DARWIN
			dirDefaultFolderPrints = System.getProperty("user.home") + separator
					+ reader.getPropertyByKey(directory).replace("c:\\", "").replace("\\", "/");
		}
		return dirDefaultFolderPrints + testToExecute.ExecutionPlan.ExecutionPlanId + " - "
				+ testToExecute.ExecutionPlan.Name + separator + testToExecute.TestPlan.TestPlanId
				+ separator + testToExecute.TestSuite.TestSuiteId + " - "
				+ testToExecute.TestSuite.Name + separator + testToExecute.TestCaseId + separator;
	}

	public static boolean convertToBool(String value) throws Exception {
		value = value.toUpperCase();

		if (value.equalsIgnoreCase("SIM")) //$NON-NLS-1$
			return true;
		else if (value.equalsIgnoreCase("NAO")) //$NON-NLS-1$
			return false;
		else if (value.equalsIgnoreCase("NÃO"))
			return false;
		else if (value.equalsIgnoreCase("YES")) //$NON-NLS-1$
			return true;
		else if (value.equalsIgnoreCase("NO")) //$NON-NLS-1$
			return false;
		else if (value.equalsIgnoreCase("TRUE")) //$NON-NLS-1$
			return true;
		else if (value.equalsIgnoreCase("FALSE")) //$NON-NLS-1$
			return false;
		else if (value.equalsIgnoreCase("SÍ")) //$NON-NLS-1$
			return true;
		else if (value.equalsIgnoreCase("1")) //$NON-NLS-1$
			return true;
		else if (value.equalsIgnoreCase("0")) //$NON-NLS-1$
			return false;

		return false;
	}

	public static int returnVk(String s) {
		try {
			return KeyEvent.class.getField(s).getInt(KeyEvent.class.getField(s));
		} catch (Exception e) {
			return 0;
		}
	}

	public static String getSendKeys(char c) throws Exception {
		switch (c) {
		case 'a':
			return "VK_A";
		case 'b':
			return "VK_B";
		case 'c':
			return "VK_C";
		case 'd':
			return "VK_D";
		case 'e':
			return "VK_E";
		case 'f':
			return "VK_F";
		case 'g':
			return "VK_G";
		case 'h':
			return "VK_H";
		case 'i':
			return "VK_I";
		case 'j':
			return "VK_J";
		case 'k':
			return "VK_K";
		case 'l':
			return "VK_L";
		case 'm':
			return "VK_M";
		case 'n':
			return "VK_N";
		case 'o':
			return "VK_O";
		case 'p':
			return "VK_P";
		case 'q':
			return "VK_Q";
		case 'r':
			return "VK_R";
		case 's':
			return "VK_S";
		case 't':
			return "VK_T";
		case 'u':
			return "VK_U";
		case 'v':
			return "VK_V";
		case 'w':
			return "VK_W";
		case 'x':
			return "VK_X";
		case 'y':
			return "VK_Y";
		case 'z':
			return "VK_Z";
		case 'A':
			return "VK_SHIFT;VK_A";
		case 'B':
			return "VK_SHIFT;VK_B";
		case 'C':
			return "VK_SHIFT;VK_C";
		case 'D':
			return "VK_SHIFT;VK_D";
		case 'E':
			return "VK_SHIFT;VK_E";
		case 'F':
			return "VK_SHIFT;VK_F";
		case 'G':
			return "VK_SHIFT;VK_G";
		case 'H':
			return "VK_SHIFT;VK_H";
		case 'I':
			return "VK_SHIFT;VK_I";
		case 'J':
			return "VK_SHIFT;VK_J";
		case 'K':
			return "VK_SHIFT;VK_K";
		case 'L':
			return "VK_SHIFT;VK_L";
		case 'M':
			return "VK_SHIFT;VK_M";
		case 'N':
			return "VK_SHIFT;VK_N";
		case 'O':
			return "VK_SHIFT;VK_O";
		case 'P':
			return "VK_SHIFT;VK_P";
		case 'Q':
			return "VK_SHIFT;VK_Q";
		case 'R':
			return "VK_SHIFT;VK_R";
		case 'S':
			return "VK_SHIFT;VK_S";
		case 'T':
			return "VK_SHIFT;VK_T";
		case 'U':
			return "VK_SHIFT;VK_U";
		case 'V':
			return "VK_SHIFT;VK_V";
		case 'W':
			return "VK_SHIFT;VK_W";
		case 'X':
			return "VK_SHIFT;VK_X";
		case 'Y':
			return "VK_SHIFT;VK_Y";
		case 'Z':
			return "VK_SHIFT;VK_Z";
		case '0':
			return "VK_NUMPAD0";
		case '1':
			return "VK_NUMPAD1";
		case '2':
			return "VK_NUMPAD2";
		case '3':
			return "VK_NUMPAD3";
		case '4':
			return "VK_NUMPAD4";
		case '5':
			return "VK_NUMPAD5";
		case '6':
			return "VK_NUMPAD6";
		case '7':
			return "VK_NUMPAD7";
		case '8':
			return "VK_NUMPAD8";
		case '9':
			return "VK_NUMPAD9";
		case '!':
			return "33";
		case '\"':
			return "34";
		case '#':
			return "35";
		case '$':
			return "36";
		case '%':
			return "37";
		case '&':
			return "38";
		case '\'':
			return "39";
		case '(':
			return "40";
		case ')':
			return "41";
		case '*':
			return "42";
		case '+':
			return "43";
		case ',':
			return "44";
		case '-':
			return "45";
		case '.':
			return "46";
		case '/':
			return "47";
		case ':':
			return "58";
		case ';':
			return "59";
		case '<':
			return "60";
		case '=':
			return "61";
		case '>':
			return "62";
		case '?':
			return "63";
		case '@':
			return "64";
		case '[':
			return "91";
		case '\\':
			return "92";
		case ']':
			return "93";
		case '^':
			return "94";
		case '_':
			return "95";
		case '`':
			return "96";
		case '{':
			return "123";
		case '|':
			return "124";
		case '}':
			return "125";
		case '~':
			return "126";
		case ' ':
			return "VK_SPACE";
		default:
			System.out.println(
					"Erro na funcao SENDKEYS da classe StarcObjects. \nNï¿½o foi possivel digitar o caracter: " + c);
			return null;
		}
	}

	public static Map<String, String> getMapAttributes(Procedure procedure) throws Exception {

		List<Attribute> listAttributes = procedure.Attributes;
		Map<String, String> map = new HashMap<String, String>();

		for (Attribute obj : listAttributes) {
			map.put(obj.Name, obj.Value);
		}

		return map;
	}

	public static Map<String, String> getMapConfigs(ExecutionData executionData) throws Exception {

		List<ExecutionConfig> listConfigs = executionData.ExecutionConfig;
		Map<String, String> map = new HashMap<String, String>();

		for (ExecutionConfig obj : listConfigs) {
			map.put(obj.ConfigName, obj.ConfigValue);
		}

		return map;
	}

	public static String getCommentError(String methodName, String msg) {
		if (msg.indexOf("(Session info") > 0)
			msg = msg.substring(0, msg.indexOf("(Session info") - 1);

		return "Error at [" + methodName + "] method: " + msg;
	}

	public static String getLocatorProcedure(int locatorNro, Procedure procedure) throws Exception {
		if (locatorNro == 0)
			locatorNro = 1;

		if (locatorNro == 1)
			return procedure.Object.Locator1;
		else
			return procedure.Object.Locator2;

	}

	public static String getValueFromProcedure1(Procedure procedure) {
		return procedure.Value;
	}

	public static String convertImageInByte(BufferedImage image) throws Exception {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "PNG", outputStream);

		String encodedImage = new String(Base64.encode(outputStream.toByteArray()), StandardCharsets.UTF_8);

		return encodedImage;
	}

	public static void waitingNextExecution() throws Exception {
		String msg = "Waiting Execution";
		System.out.print(msg);
		for (int i = 0; i <= 3; i++) {
			System.out.print('.');
			Thread.sleep(1000);
		}
		System.out.println();
	}

	public static void CreateFolder(ExecutionData testToExecute, String dir) throws Exception {

		String path = buildPath(testToExecute,dir);
		File folderToPrint = new File(path);

		if (folderToPrint.exists()) {
			DeleteFiles(folderToPrint);
		}

		folderToPrint.mkdirs();
		folderToPrint.setWritable(true);
	}

	public static void DeleteFiles(File file) throws Exception {
		if (file.isDirectory()) {
			if (file.list().length != 0) {
				String[] prints = file.list();
				for (String print : prints) {
					File fileToDelete = new File(file, print);
					fileToDelete.delete();
				}
			}
		}
	}

	public static String cNull(String value) {
		if (value == null)
			return "";
		else
			return value;

	}

	public static String cNull(String value, String valueNew) {
		if (value == null)
			return valueNew;
		else
			return value;

	}
}
