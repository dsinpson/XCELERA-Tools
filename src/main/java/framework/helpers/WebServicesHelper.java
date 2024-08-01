package framework.helpers;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class WebServicesHelper {
	
	public static String xmlGetTagValueByText(String body ,String tag)
   	{
   		try {
   	   		String bodyAux;
   	   		String bodyUpper=body.toUpperCase();
   	   		String tagUpper=tag.toUpperCase();
   	   		String tagEnd=tagUpper.replaceAll("<", "</");
   	   		
   	   		bodyAux=body.substring( bodyUpper.indexOf(tagUpper) + tagUpper.length(),bodyUpper.indexOf(tagEnd) ).trim();
   	   		return bodyAux;
		} 
   		catch (Exception e) {
			return "Response error, " + body;
		}
	}
	
	public static String xmlSetTagValue(String message, String tag,String value) throws Exception
   	{
   		String bodyAux;
   		String bodyUpper=message.toUpperCase();
   		String tagUpper=tag.toUpperCase();
   		String tagEnd=tagUpper.replaceAll("<", "</");
   		try {
   			bodyAux=message.substring(0, bodyUpper.indexOf(tagUpper) + tagUpper.length() );
   			message=bodyAux + value + message.substring(bodyUpper.indexOf(tagEnd) );
   			return message;
		} catch (Exception e) {
			throw new Exception("Invalid tag: " + tag);
		}   		
   	}
   	
  	public static String xmlGetTagValue(String message ,String tag,int itemNumber,String attribute) throws Exception{
		String text = null;
		try {
    		tag=tag.replaceAll( "<","").replaceAll(">", "");
    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();           
            Document document = db.parse(new InputSource(new StringReader(message)));
            NodeList nl = document.getElementsByTagName(tag);
            for(int i = 0 ; i < nl.getLength(); i++)
            {
            	if(i+1 == itemNumber)
            	{
                  org.w3c.dom.Element e = (org.w3c.dom.Element)nl.item(i);
                  if(attribute.isEmpty()) 
                  {
                	  text = e.getTextContent();
                  }else 
                  {
                	  text =nl.item(i).getAttributes().getNamedItem(attribute).getNodeValue();
                  }
            	}
             }            
		} catch (Exception e) {
			throw new Exception("Tag not found: returned response, " + convertXMLComment(message) + ", tag:" + tag);
		}
		return text;
	}
  	
	public static String jsonGetPropValue(String message ,String prop) throws Exception 
	{
		String[] propSpl=prop.split(":");
		int indexof1=-1,indexof2=-1,indexof3=-1,indexofSelected=-1,indexof4=-1, indexold=0;
		String propaux="";
		
		for (int i = 0; i < propSpl.length; i++) 
		{
			propaux=propSpl[i] + ":";
			indexof1=message.indexOf(propaux,indexold);
			if(indexof1 == -1)
			{
				throw new Exception("Property not found: " + prop + ", Response Message: " + message);
			}
			//se for a última propriedade captura o valor
			else if(i==propSpl.length - 1)
			{
				indexof2=message.indexOf(",",indexof1 + propaux.length());
				indexof3=message.indexOf("]",indexof1 + propaux.length());
				indexof4=message.indexOf("}",indexof1 + propaux.length());
				
				if(indexof2 != -1 && indexof2 <= indexof3 && indexof2 <= indexof4)
					indexofSelected=indexof2;
				else if(indexof3 != -1 && indexof3 <= indexof2 && indexof3 <= indexof4)
					indexofSelected=indexof3;
				else
					indexofSelected=indexof4;
				
				return  message.substring(indexof1 + propaux.length(),indexofSelected);

				
			}
			indexold=indexof1 + propaux.length();
		}
		return "";
	}
	
	public static String jsonSetPropValue(String message ,String prop,String value) throws Exception 
	{
		String[] propSpl=prop.split(":");
		int indexof1=-1,indexof2=-1,indexof3=-1,indexofSelected=-1,indexof4=-1, indexold=0;
		String propaux="";
		String caractString="";
		
		for (int i = 0; i < propSpl.length; i++) 
		{
			propaux=propSpl[i] + ":";
			indexof1=message.indexOf(propaux,indexold);
			if(indexof1 == -1)
			{
				throw new Exception("Property not found: " + prop);
			}
			//se for a última propriedade captura o valor
			else if(i==propSpl.length - 1)
			{
				indexof2=message.indexOf(",",indexof1 + propaux.length());
				indexof3=message.indexOf("]",indexof1 + propaux.length());
				indexof4=message.indexOf("}",indexof1 + propaux.length());
				
				if(indexof2==-1)
					indexof2=99999999;
				if(indexof3==-1)
					indexof3=99999999;
				if(indexof4==-1)
					indexof4=99999999;			
				
				if(indexof2 <= indexof3 && indexof2 <= indexof4 && indexof2!=-1)
					indexofSelected=indexof2;
				else if(indexof3 <= indexof2 && indexof3 <= indexof4 && indexof3!=-1)
					indexofSelected=indexof3;
				else
					indexofSelected=indexof4;
				
				if(getFirstCaract(message,indexof1 + propaux.length()).equals("\"") && !getFirstCaract(value,0).equals( "\""))
					caractString="\"";
				
				message= message.substring(0,indexof1 + propaux.length()) + caractString + value + caractString + message.substring(indexofSelected);
				
				return  message;
			}
			indexold=indexof1 + propaux.length();
		}
		return "";
	}
	
	private static String getFirstCaract(String message, int indexStart) 
	{
		for(int x=indexStart;x < message.length();x++)
		{
			if(message.charAt(x) != ' ')
			{
				return message.substring(x,x + 1);
			}
		}
		return "";
	}
	
  	public static String convertXMLComment(String value)
  	{
  		return value.replaceAll("<", "<a><</a>");
  	}
}
