package framework.utils;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Predicate;
import com.jayway.jsonpath.spi.json.JsonProvider;

public class WebServicesUtils {
	
	public static String XmlGetTagValueByText(String body ,String tag)
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
	
	public static String XmlSetTagValue(String message, String tag,String value) throws Exception
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
   	
  	public static String XmlGetTagValue(String message ,String tag,int itemNumber,String attribute) throws Exception{
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
			throw new Exception("Tag not found: returned response, " + ConvertXMLComment(message) + ", tag:" + tag);
		}
		return text;
	}
  	
	public static String JsonGetPropValue(String json ,String prop) throws Exception 
	{
		return JsonPath.read(json, prop);
	}
	
	public static String JsonSetPropValue(String json ,String prop,String value) throws Exception 
	{
		return JsonPath.parse(json).set(prop, value).jsonString();
	}
	
	public static String JsonReadPropValue(String json ,String prop) throws Exception 
	{
		return JsonPath.parse(json).read(prop).toString();
	}
	
	private static String GetFirstCaract(String message, int indexStart) 
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
	
  	public static String ConvertXMLComment(String value)
  	{
  		return value.replaceAll("<", "<a><</a>");
  	}
}
