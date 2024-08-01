package framework.helpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.jayway.jsonpath.JsonPath;

import framework.data.entities.WebServiceData;


public class RestWebServiceHelper {
	
	public static String getParamsString(Map<String, String> params) 
      throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
 
        for (Map.Entry<String, String> entry : params.entrySet()) {
          result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
          result.append("=");
          result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
          result.append("&");
        }
 
        String resultString = result.toString();
        return resultString.length() > 0
          ? resultString.substring(0, resultString.length() - 1)
          : resultString;
    }
	
	/*
	 * This method need to be customized do each generate token method
	 * Above that is a sample of how to generate a Bearer token calling a /auth resource
	 */
	public static String generateToken(WebServiceData webServiceData) throws Exception {
		try{
			URL url = new URL(webServiceData.Endpoint + "/auth");
			
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);
			
			String jsonAuth = "{\"Username\":\"admin\", \"Password\":\"admin\"}";
			
			try(OutputStream os = con.getOutputStream()){
				byte[] input = jsonAuth.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);			
			}
			
			String responseBody = "";
			try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))){
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				responseBody = response.toString();
			}
			
			String accessToken = new JSONObject(responseBody).getJSONObject("Data").getString("accessToken");
			
			return accessToken;
		}
		catch(Exception e) {
			throw new Exception("Error generating token. Error Message: " + e.getMessage());
		}
	}
	
	public static String GetPropValue(String document ,String prop, Boolean isJson) throws Exception 
	{		
		if(isJson)
			return JsonPath.read(document, prop);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
        builder = factory.newDocumentBuilder();
         
        Document doc = builder.parse(new InputSource(new StringReader(document)));
		
        return doc.getElementsByTagName(prop).item(0).getTextContent();            		
	}
}
