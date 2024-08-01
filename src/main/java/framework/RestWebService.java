package framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import framework.data.dynamicValues.DynamicValuesHelper;
import framework.data.entities.Attribute;
import framework.data.entities.ObtainedResponse;
import framework.data.entities.WebServiceData;
import framework.helpers.RestWebServiceHelper;
import framework.utils.WebServicesUtils;

public class RestWebService {

	private final Map<String, String> _executionConfigs;

	public static String authCode = "eyJraWQiOiI0Zjk2MGEyMTEyNWZmYzgwNmU2YzA5YzdlNWI4MzQ1OWFmMDY4NzdkIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJhdXRoIiwiYXVkIjoiYXBpLmhkaS5jb20uYnIiLCJyb2xlcyI6WyJSb2xlMSIsIlJvbGUyIiwiUm9sZU4iXSwiaXNzIjoiYXV0aCIsImV4cCI6MTU5MjkyMzk1MiwiaWF0IjoxNTkyOTIzODkyLCJ1c2VyIjoiYXBpLWlucy1hc3Npc3RhbmNlLXNhbGVzZm9yY2UtZG1sLWNyZWRlbnRpYWxzIiwid2ViQ2xpZW50IjoiYXBpLmhkaS5jb20uYnIifQ.K80-RLr2FiZmgEBH_9dfYSSx2lAm4wfFyk5-VygapJGbtFVgCapWvssYYR8YR6hdYsxqgTB0Qu3Q7lau-DALKjHXnReA5Osp5Q23hlS3gLuGX0PHCyKrniA9AnlScyJgDXe3fhkuriPnlGv4M48ijqWtzOXbwNjrOzq9EQctaAo74dk4TnfItgUqtpi12csKMdpeAHC38XC_jJrYLpmw1HBcB9RbLVEn2Xjki9wXfdU47hG35vufpC1RaeNNeXvQckGClrT-9WSvrGDtlrPz2z9BR--nPYehfYMIv3JbSjifgaT2jH-e-XPsPWEv9jkWxyRglwGAGttDSvww0SV-Bw";
	public static String tokenOneClick = "eyJraWQiOiI0Zjk2MGEyMTEyNWZmYzgwNmU2YzA5YzdlNWI4MzQ1OWFmMDY4NzdkIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJoZGktY29ycG9yYXRlLWF1dGhAaW5zLWNyb3NzLXNlY3VyaXR5LWF1dGgtZGV2LmlhbS5nc2VydmljZWFjY291bnQuY29tIiwiYXVkIjoiYXBpLmhkaS5jb20uYnIiLCJhcHBsaWNhdGlvbiI6IjExMTExIiwicm9sZXMiOlsiUm9sZTEiLCJSb2xlMiIsIlJvbGVOIl0sImlzcyI6ImhkaS1jb3Jwb3JhdGUtYXV0aEBpbnMtY3Jvc3Mtc2VjdXJpdHktYXV0aC1kZXYuaWFtLmdzZXJ2aWNlYWNjb3VudC5jb20iLCJleHAiOjE1OTM1NDMyNTEsImlhdCI6MTU5MzUzOTY1MSwidXNlciI6ImFwaS1pbnMtYXNzaXN0YW5jZS1zYWxlc2ZvcmNlLWRtbC1jcmVkZW50aWFscyIsIndlYkNsaWVudCI6ImFwaS5oZGkuY29tLmJyIn0.pSM62HuetNJx9EJ6y1uWZVqBs4sQZ5zURO8NwiSI1yhtov253KcHF0tZ-4p2NArWoQEVrStclDlvzihWZ9JWNS9KDd3CNY2AsCeJkT06CSDWQDCacL8eSBUYWxeYU21NmqYbpFqWp2SGGnMuESy6R22MwAf3fcbREpKXHCeTo0Wqxdt6AzKXo-kvm7X1Y8ydKKp0BkOEyHRG6WI1GqwFAtNZJ2Oyk1t0EEVFQWgPlbjhnviN69VbyaG5SsxXZ9iUWKCUS659-52svVFMfVEq-hKHE8jc3JaA1f46xkQz_u2ytIbzEX8g5IL0lR-LXC9-TvSF4pYVs0fTu2grFBoUqg";
	public static String obtainedResponseHeader = "";
	public static String obtainedResponseHeaderPDF = "";
	public static String obtainedResponseBody = "";
	public static String obtainedStatusCode = "";
	private String _dominio;
	Calendar c = Calendar.getInstance();
	SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd");

	public static String quotationDate;
	public static String effectiveDate;
	public static String expirationDate;
	public static String idOffer;

	public RestWebService(Map<String, String> executionConfigs) {
		_executionConfigs = executionConfigs;
	}

	public void executeAction(long testTypeId, WebServiceData webServiceData) throws Exception {

//		String strUrl = webServiceData.Endpoint + "/" + webServiceData.Resource;

		_dominio = _executionConfigs.get("Custom.dominio");

		String strUrl = _dominio + webServiceData.Endpoint + "/" + webServiceData.Resource;
		// {{dominio}}
		if (strUrl.contains("{{dominio}}"))
			strUrl = strUrl.replace("{{dominio}}", "");

		if (strUrl.contains("br//"))
			strUrl = strUrl.replace("br//", "br/");

//		if(strUrl.contains("#idOffer"))
//			strUrl = strUrl.replace("#idOffer", idOffer);

		if (webServiceData.Endpoint.equals(webServiceData.Resource)) {
			strUrl = webServiceData.Endpoint;
		}

		if (strUrl.contains("?"))
			strUrl = strUrl.substring(0, strUrl.indexOf("?"));

		URL url = new URL(strUrl);

		// ADD Parameters to URL
		if (webServiceData.Parameters != null && !webServiceData.Parameters.isEmpty()) {
			Map<String, String> parameters = new HashMap<>();
			for (Attribute param : webServiceData.Parameters) {
				if (param.Value.equals("<IGNORE>"))
					continue;

				if (param.Value.equals("#key") || param.Value.equals("{{API_KEY}}") || param.Value.equals("#query_key"))
					param.Value = _executionConfigs.get("Custom.API_KEY");

				// {{dataCorrente}}
				if (param.Value.equals("{{dataCorrente}}") || param.Value.equals("#query_calculusDate")) {
					param.Value = formatador.format(c.getTime());
					System.out.println(param.Value);
				}

				if (param.Value.equals("#query_quotationDate")) {
					param.Value = _executionConfigs.get("Custom.#query_quotationDate");
					System.out.println(param.Value);
				}

				if (param.Value.equals("#query_startDateApol")) {
					param.Value = _executionConfigs.get("Custom.#query_startDateApol");
					System.out.println(param.Value);
				}

				if (param.Value.equals("#query_sucursalCode")) {
					param.Value = _executionConfigs.get("Custom.SUCURSAL");
					System.out.println(param.Value);
				}

				if (param.Value.equals("#idOffer")) {
					param.Value = idOffer;
					System.out.println(param.Value);
				}

				parameters.put(param.Name, param.Value);
			}
			String queryParams = RestWebServiceHelper.getParamsString(parameters);

			if (strUrl.contains("#idOffer"))
				strUrl = strUrl.replace("#idOffer", idOffer);

			url = new URL(strUrl + "?" + queryParams);

		}

		// SET HttpUrlConnection object
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod(webServiceData.Method);
		con.setDoOutput(true);

		// ADD Headers
		if (webServiceData.Headers != null && !webServiceData.Headers.isEmpty()) {
			for (Attribute header : webServiceData.Headers) {
				if (header.Value.equals("<IGNORE>"))
					continue;

				if (header.Value.contains("#tokenOneClick")) {
					// String token = RestWebServiceHelper.generateToken(webServiceData);
					header.Value = header.Value.replace("#tokenOneClick", tokenOneClick);
				}

				if (header.Value.contains("#trackidPDF")) {
					// String token = RestWebServiceHelper.generateToken(webServiceData);
					header.Value = header.Value.replace("#trackidPDF", obtainedResponseHeaderPDF);
					Thread.sleep(9000);
				}

				if (header.Value.contains("#trackid")) {
					// String token = RestWebServiceHelper.generateToken(webServiceData);
					header.Value = header.Value.replace("#trackid", obtainedResponseHeader);
				}

				if (header.Value.contains("#X-Track-Id")) {
					// String token = RestWebServiceHelper.generateToken(webServiceData);
					header.Value = header.Value.replace("#X-Track-Id", obtainedResponseHeader);
				}

				if (header.Value.contains("#x-Track-Id")) {
					// String token = RestWebServiceHelper.generateToken(webServiceData);
					header.Value = header.Value.replace("#x-Track-Id", obtainedResponseHeader);
				}

				// _executionConfigs.get("Custom.dominio");
				if (header.Value.contains("#X-User-Id")) {
					// String token = RestWebServiceHelper.generateToken(webServiceData);
					header.Value = header.Value.replace("#X-User-Id", _executionConfigs.get("Custom.X-User-Id"));
				}
				if (header.Value.contains("#x-trace-id")) {
					header.Value = header.Value.replace("#x-trace-id", _executionConfigs.get("Custom.x-trace-id"));
				}

				if (header.Value.contains("#x-company-id")) {
					header.Value = header.Value.replace("#x-company-id", _executionConfigs.get("Custom.x-company-id"));
				}

				if (header.Value.contains("#X-Company-Id")) {
					header.Value = header.Value.replace("#X-Company-Id", _executionConfigs.get("Custom.x-company-id"));
				}

				if (header.Value.contains("#x-application-id")) {
					// String token = RestWebServiceHelper.generateToken(webServiceData);
					header.Value = header.Value.replace("#x-application-id",
							_executionConfigs.get("Custom.x-application-id"));
				}

				if (header.Value.contains("#X-Application-Id")) {
					header.Value = header.Value.replace("#X-Application-Id",
							_executionConfigs.get("Custom.X-Application-Id"));
				}

				con.setRequestProperty(header.Name, header.Value);
			}
		}

		// ADD Body
		String body = "";
		if (webServiceData.Body != null && !webServiceData.Body.isEmpty()) {

			if (!("JSON".equals(webServiceData.ContentType)))
				body = webServiceData.Body;

			// else {
			DynamicValuesHelper dynamicValueHelper = new DynamicValuesHelper();
			JSONObject jsonBody = dynamicValueHelper.changeDynamicWebServiceBody(webServiceData.Body,
					_executionConfigs);

			body = jsonBody.toString();
			body = body.replace("\"installmentDay\":\"{{melhorData}}\"",
					"\"installmentDay\":" + DynamicValuesHelper.installmentDay);

			if (quotationDate != null)
				body = body.replace("{{quotationDate}}", quotationDate);

			if (effectiveDate != null)
				body = body.replace("{{effectiveDate}}", effectiveDate);

			if (expirationDate != null)
				body = body.replace("{{expirationDate}}", expirationDate);

			if (body.contains("insuranceRelationship"))
				body = body.replace("\"null\"", "null");

			// }
		}

		if (!webServiceData.Method.equals("GET")) {
			try (OutputStream os = con.getOutputStream()) {
				byte[] input = body.getBytes();
				String in = new String(input, StandardCharsets.UTF_8);
				os.write(in.getBytes(), 0, in.getBytes().length);
			}
		}

		// GET Responses
		obtainedStatusCode = Integer.toString(con.getResponseCode());
		obtainedResponseBody = "";
		obtainedResponseHeader = con.getHeaderField("x-track-id");
		System.out.println(obtainedResponseHeader);

		if (obtainedResponseHeader != null) {
			obtainedResponseHeaderPDF = obtainedResponseHeader;
		}

		String ExpectedResponseBody = webServiceData.ExpectedResponse.ExpectedResponseBody;
		String obtainedContentType = con.getHeaderField("content-type");

		if (ExpectedResponseBody != null) {

			if (ExpectedResponseBody.contains("{{dateFirstInstallment60}}"))
				ExpectedResponseBody = ExpectedResponseBody.replace("{{dateFirstInstallment60}}",
						DynamicValuesHelper.getdateFirstInstallment(60));

			if (ExpectedResponseBody.contains("{{dateFirstInstallment}}"))
				ExpectedResponseBody = ExpectedResponseBody.replace("{{dateFirstInstallment}}",
						DynamicValuesHelper.getdateFirstInstallment(37));

			if (ExpectedResponseBody.contains("{{dateFirstPayment30}}"))
				ExpectedResponseBody = ExpectedResponseBody.replace("{{dateFirstPayment30}}",
						DynamicValuesHelper.getdateFirstPayment(30));

			if (ExpectedResponseBody.contains("{{dateFirstPayment-7}}"))
				ExpectedResponseBody = ExpectedResponseBody.replace("{{dateFirstPayment-7}}",
						DynamicValuesHelper.getdateFirstPayment(-7));

			if (ExpectedResponseBody.contains("{{dateFirstPayment}}"))
				ExpectedResponseBody = ExpectedResponseBody.replace("{{dateFirstPayment}}",
						DynamicValuesHelper.getdateFirstPayment(7));

		}

		if (obtainedStatusCode.equals("409") && webServiceData.ExpectedResponse.ExpectedStatusCode.equals("401")) {
			// if (webServiceData.ExpectedResponse.ExpectedStatusCode.equals("497")){
			System.out.println("teste de 452 = 497 Unknown");
			// }
		} else if (obtainedStatusCode.equals("401")
				&& !webServiceData.ExpectedResponse.ExpectedStatusCode.equals("401")) {

		} else {

			if (obtainedStatusCode.startsWith("20")) {

				InputStream streamBody = con.getInputStream();

				if ("application/pdf".equals(obtainedContentType)) {
					byte[] bytes = IOUtils.toByteArray(streamBody);
					obtainedResponseBody = Base64.getEncoder().encodeToString(bytes);

				} else {
					try (BufferedReader br = new BufferedReader(new InputStreamReader(streamBody, StandardCharsets.UTF_8))) {
						StringBuilder response = new StringBuilder();
						String responseLine = null;
						while ((responseLine = br.readLine()) != null) {
							response.append(responseLine.trim());
						}
						obtainedResponseBody = response.toString();
					} catch (Exception e) {
						obtainedResponseBody = "";
					}
				}

			} else {
				try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8))) {
					StringBuilder response = new StringBuilder();
					String responseLine = null;
					while ((responseLine = br.readLine()) != null) {
						response.append(responseLine.trim());
					}
					obtainedResponseBody = response.toString();
				} catch (Exception e) {
					obtainedResponseBody = "";
				}
			}

			// GET VALUES FROM RESPONSE BODY
			if (obtainedResponseBody.contains("quotationId") && (!obtainedResponseBody.contains("trackId"))) {
				if (obtainedResponseBody.contains("quotationDate") && quotationDate == null)
					quotationDate = WebServicesUtils
							.JsonGetPropValue(obtainedResponseBody, "offers[0].items[0].insurance.quotationDate");

				if (obtainedResponseBody.contains("effectiveDate") && effectiveDate == null)
					effectiveDate = WebServicesUtils
							.JsonGetPropValue(obtainedResponseBody, "offers[0].items[0].insurance.period.effectiveDate");

				if (obtainedResponseBody.contains("expirationDate") && expirationDate == null)
					expirationDate = WebServicesUtils.JsonGetPropValue(obtainedResponseBody,
							"offers[0].items[0].insurance.period.expirationDate");

				if (obtainedResponseBody.contains("offers") && (!obtainedResponseBody.contains("OneClick")))
					idOffer = WebServicesUtils.JsonGetPropValue(obtainedResponseBody, "offers[0].id");

			}

			// UPDATE ActionData with obtained responses
			webServiceData.ObtainedResponse = new ObtainedResponse();
			webServiceData.ObtainedResponse.ObtainedStatusCode = obtainedStatusCode + "\n" + "  x-track-id  "
					+ obtainedResponseHeader;
			webServiceData.ObtainedResponse.ObtainedResponseBody = obtainedResponseBody;
			setobtainedResponseBody(obtainedResponseBody);
			webServiceData.ObtainedResponse.ObtainedContentType = obtainedContentType;

			// ASSERT Responses
			if ("application/pdf".equals(webServiceData.ExpectedResponse.ExpectedResponseContentType)
					&& !"application/pdf".equals(obtainedContentType))
				throw new Exception("Invalid Content Type." + " Expected Content Type: "
						+ webServiceData.ExpectedResponse.ExpectedResponseContentType + " Obtained Content Type: "
						+ obtainedContentType);

			if (obtainedStatusCode.equals("452") && webServiceData.ExpectedResponse.ExpectedStatusCode.equals("497")) {
				System.out.println("teste de 452 = 497 Unknown");
			} else if (obtainedStatusCode.equals("452")
					&& webServiceData.ExpectedResponse.ExpectedStatusCode.equals("453")) {
				System.out.println("teste de 452 = 453 Unknown");
			} else if (obtainedStatusCode.equals("453")
					&& webServiceData.ExpectedResponse.ExpectedStatusCode.equals("497")) {
				System.out.println("teste de 453 = 497 Unknown");
			} else if (obtainedStatusCode.equals("404")
					&& webServiceData.ExpectedResponse.ExpectedStatusCode.equals("404")) {
				System.out.println("teste de 404 = 404 Not Found");
			} else {
				// ASSERT Full Error Value
				if (testTypeId == 7 && obtainedStatusCode.startsWith("2"))
					throw new Exception(
							"Invalid Status Code. Expected an error status code. Obtained a success status code");

				if (!obtainedStatusCode.equals(webServiceData.ExpectedResponse.ExpectedStatusCode))
					throw new Exception("Invalid Status Code." + " Expected Status Code: "
							+ webServiceData.ExpectedResponse.ExpectedStatusCode + " Obtained Status Code: "
							+ obtainedStatusCode);
			}

			if (webServiceData.ExpectedResponse.ExpectedResponseBody.equals("<IGNORE>")
					|| webServiceData.ExpectedResponse.ExpectedResponseBody.equals(""))
				return;

			if (webServiceData.ExpectedResponse.ExpectedResponseBody.isEmpty() && obtainedResponseBody.isEmpty())
				return;

			String expectedResponseBody = webServiceData.ExpectedResponse.ExpectedResponseBody
					.replaceAll("\n", "");

//		switch(webServiceData.ExpectedResponse.ExpectedResponseContentType)
			String Content = "JSON";
			switch (Content) {
			case "XML": {
				validateXML(ExpectedResponseBody);
				XMLUnit.setIgnoreWhitespace(true);
				Diff diff = XMLUnit.compareXML(ExpectedResponseBody, obtainedResponseBody);

				if (!diff.similar())
					throwMessageExpectedObtained(ExpectedResponseBody, obtainedResponseBody);
				break;
			}
			case "JSON": {
				ObjectMapper mapper = new ObjectMapper();

				if (isJsonValid(ExpectedResponseBody)) {

					JsonNode expected = mapper.readTree(ExpectedResponseBody);
					JsonNode obtained = mapper.readTree(obtainedResponseBody);

					if (!expected.equals(obtained))
						throwMessageExpectedObtained(expected.toString(), obtained.toString());
				} else {
					if (!ExpectedResponseBody.equals(obtainedResponseBody))
						throwMessageExpectedObtained(ExpectedResponseBody, obtainedResponseBody);
				}
				break;
			}
			default: {
				if (!ExpectedResponseBody.equals(obtainedResponseBody))
					throwMessageExpectedObtained(ExpectedResponseBody, obtainedResponseBody);
			}
			}
		}
	}

	private void throwMessageExpectedObtained(String expected, String obtained) throws Exception {
		System.out.println(expected);
		System.out.println(obtained);

		throw new Exception("Invalid Response Body." + " Expected Response Body: " + expected
				+ " Obtained Response Body: " + obtained);
	}

	private void validateXML(String xml) throws Exception {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = null;
			builder = factory.newDocumentBuilder();

			builder.parse(new InputSource(new StringReader(xml)));
		} catch (Exception ex) {
			throw new Exception("Error parsing expected xml. " + ex.getMessage());
		}
	}

	private boolean isJsonValid(String jsonString) {
		try {
			new JSONObject(jsonString);
		} catch (Exception e) {
			try {
				new JSONArray(jsonString);
			} catch (Exception ex1) {
				return false;
			}
		}
		return true;
	}

	public void setobtainedResponseBody(String obtainedResponseBody) throws IOException, InterruptedException {
		RestWebService.obtainedResponseBody = obtainedResponseBody;

		if (obtainedResponseBody.contains("PDF")) {
			// Custom _custom = new Custom(null, _executionConfigs, null, null);
			// custom.SalvarJson();
			// Thread.sleep(1000);
		}
	}
}
