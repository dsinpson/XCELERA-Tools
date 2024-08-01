package framework.utils;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

import framework.dataProviders.ConfigFileReader;

public class Utils {
	public static String CaptureProp(String value, String prop) {
		String ret;
		int iniProp;
		int endProp;

		if (value == "" || value.indexOf(prop + "=") < 0) //$NON-NLS-1$ //$NON-NLS-2$
			ret = ""; //$NON-NLS-1$
		else
		{
			iniProp = value.indexOf(prop + "="); //$NON-NLS-1$
			endProp = value.indexOf("|", iniProp ); //$NON-NLS-1$

			if (endProp < iniProp)
				endProp = value.length();
			else
				endProp = value.indexOf("|", iniProp); //$NON-NLS-1$

			iniProp = iniProp + (prop.length() +1);
			ret = value.substring(iniProp, endProp);
		}

		return ret;
	}
	
	public static String FormatValue(String value)  throws Exception
	{
		if (CaptureProp(value, "Formato") == "") //$NON-NLS-1$ //$NON-NLS-2$
			return CaptureProp(value, "Valor"); //$NON-NLS-1$
		else
			return String.format(CaptureProp(value, "Formato"), CaptureProp(value, "Valor")); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public static String Now(){
		return Now("yyyy_MM_dd_HH_mm_ss");
	}
	
	public static String Now(String formatStr)
	{
		DateFormat dateFormat = new SimpleDateFormat(formatStr); //$NON-NLS-1$
		java.util.Date date = new java.util.Date();

		return dateFormat.format(date);
	}    
	
	public static String RandomFile(String idCenario)
	{
		return idCenario + "_" + Now();
	}

	public static boolean ConvertToBool(String value){
		value=value.toUpperCase();

		if(value.equalsIgnoreCase("SIM")) //$NON-NLS-1$
			return true;
		else if(value.equalsIgnoreCase("NAO")) //$NON-NLS-1$
			return false;
		else if(value.equalsIgnoreCase("NÃO"))
			return false;
		else if(value.equalsIgnoreCase("YES")) //$NON-NLS-1$
			return true;
		else if(value.equalsIgnoreCase("NO")) //$NON-NLS-1$
			return false;
		else if(value.equalsIgnoreCase("TRUE")) //$NON-NLS-1$
			return true;
		else if(value.equalsIgnoreCase("FALSE")) //$NON-NLS-1$
			return false;
		else if(value.equalsIgnoreCase("SÍ")) //$NON-NLS-1$
			return true;
		else if(value.equalsIgnoreCase("1")) //$NON-NLS-1$
			return true;        
		else if(value.equalsIgnoreCase("0")) //$NON-NLS-1$
			return false;   

		return false;
	}
	
	public static int ReturnVk (String s){
		try {
			return KeyEvent.class.getField(s).getInt(KeyEvent.class.getField(s));
		} catch (Exception e) {
			return 0;
		}
	}    
	
	public static String GetSendKeys(char c){
		switch (c) {
		case 'a': return "VK_A";
		case 'b': return "VK_B";
		case 'c': return "VK_C";
		case 'd': return "VK_D";
		case 'e': return "VK_E";
		case 'f': return "VK_F";
		case 'g': return "VK_G";
		case 'h': return "VK_H";
		case 'i': return "VK_I";
		case 'j': return "VK_J";
		case 'k': return "VK_K";
		case 'l': return "VK_L";
		case 'm': return "VK_M";
		case 'n': return "VK_N";
		case 'o': return "VK_O";
		case 'p': return "VK_P";
		case 'q': return "VK_Q";
		case 'r': return "VK_R";
		case 's': return "VK_S";
		case 't': return "VK_T";
		case 'u': return "VK_U";
		case 'v': return "VK_V";
		case 'w': return "VK_W";
		case 'x': return "VK_X";
		case 'y': return "VK_Y";
		case 'z': return "VK_Z";
		case 'A': return "VK_SHIFT;VK_A";
		case 'B': return "VK_SHIFT;VK_B";
		case 'C': return "VK_SHIFT;VK_C";
		case 'D': return "VK_SHIFT;VK_D";
		case 'E': return "VK_SHIFT;VK_E";
		case 'F': return "VK_SHIFT;VK_F";
		case 'G': return "VK_SHIFT;VK_G";
		case 'H': return "VK_SHIFT;VK_H";
		case 'I': return "VK_SHIFT;VK_I";
		case 'J': return "VK_SHIFT;VK_J";
		case 'K': return "VK_SHIFT;VK_K";
		case 'L': return "VK_SHIFT;VK_L";
		case 'M': return "VK_SHIFT;VK_M";
		case 'N': return "VK_SHIFT;VK_N";
		case 'O': return "VK_SHIFT;VK_O";
		case 'P': return "VK_SHIFT;VK_P";
		case 'Q': return "VK_SHIFT;VK_Q";
		case 'R': return "VK_SHIFT;VK_R";
		case 'S': return "VK_SHIFT;VK_S";
		case 'T': return "VK_SHIFT;VK_T";
		case 'U': return "VK_SHIFT;VK_U";
		case 'V': return "VK_SHIFT;VK_V";
		case 'W': return "VK_SHIFT;VK_W";
		case 'X': return "VK_SHIFT;VK_X";
		case 'Y': return "VK_SHIFT;VK_Y";
		case 'Z': return "VK_SHIFT;VK_Z";
		case '0': return "VK_NUMPAD0";
		case '1': return "VK_NUMPAD1";
		case '2': return "VK_NUMPAD2";
		case '3': return "VK_NUMPAD3";
		case '4': return "VK_NUMPAD4";
		case '5': return "VK_NUMPAD5";
		case '6': return "VK_NUMPAD6";
		case '7': return "VK_NUMPAD7";
		case '8': return "VK_NUMPAD8";
		case '9': return "VK_NUMPAD9";
		case '!':  return "33";
		case '\"': return "34";
		case '#': return "35";
		case '$': return "36";
		case '%': return "37";
		case '&': return "38";
		case '\'': return "39";
		case '(': return "40";
		case ')': return "41";
		case '*': return "42";
		case '+': return "43";
		case ',': return "44";
		case '-': return "45";
		case '.': return "46";
		case '/': return "47";
		case ':': return "58";
		case ';': return "59";
		case '<': return "60";  
		case '=': return "61";
		case '>': return "62";
		case '?': return "63";
		case '@': return "64";
		case '[': return "91";
		case '\\': return "92";
		case ']': return "93";
		case '^': return "94";
		case '_': return "95";
		case '`': return "96";
		case '{': return "123";
		case '|': return "124";
		case '}': return "125";
		case '~': return "126";		
		case ' ': return "VK_SPACE";
		default:
			System.out.println("Erro na funcao SENDKEYS da classe StarcObjects. \nNï¿½o foi possivel digitar o caracter: "+c);
			return null;
		}
	}
	
	public static String CapturePrint(String printPath) throws Exception {
		BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	    ImageIO.write(image, "png", new File(printPath));
	    
	    return printPath;
	}
}
