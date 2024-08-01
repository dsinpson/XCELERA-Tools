
package framework.localAgent;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import framework.custom.utils.Evidencia;
import framework.custom.utils.HttpsTrustManager;
import framework.custom.utils.InvalidCertificates;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import framework.data.entities.ExecutionData;
import framework.data.entities.Prints;
import framework.dataProviders.ConfigFileReader;
import framework.helpers.GeneralHelper;

public class ScheduledExecution {

	private final String ApiURL;
	private String _bearer;
	boolean _requestToSTARCAPI;
	boolean isWindows = System.getProperty("os.name").toLowerCase().contains("windows");

	public ScheduledExecution() throws Exception {

		ConfigFileReader reader = new ConfigFileReader("configs/config.properties");

		_requestToSTARCAPI = GeneralHelper.convertToBool(reader.getPropertyByKey("requestToSTARCApi"));

		if (_requestToSTARCAPI)
			this.ApiURL = reader.getPropertyByKey("STARCApiURL");
		else
			this.ApiURL = reader.getPropertyByKey("localAgentURL");
//
//		String workerName = reader.getPropertyByKey("workerName");
//		if (workerName.contains("05") || workerName.contains("06") || workerName.contains("07")
//				|| workerName.contains("08") || workerName.contains("04"))
//			this.ApiURL = "https://app.x-celera.com/ExecutionManagerAPI/api/";
	}

	private static String readAll(Reader rd) throws Exception {
		StringBuilder sb = new StringBuilder();
		int cp;

		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}

		return sb.toString();
	}

	public ExecutionData getNextTestToExecute() {
		try {
			InetAddress ia = InetAddress.getLocalHost();
			String hostname = ia.getHostName();
			//String hostname = "HDIWUFTAPP13";

			// Ajuste para ler o nome do host pelo arquivo de config na chave workerName
			ConfigFileReader reader = new ConfigFileReader("configs/config.properties");
			String workerName = reader.getPropertyByKey("workerName");

			if (workerName != null && !workerName.isEmpty())
				hostname = workerName;

			String url = this.ApiURL + "v1/executions/workers?workerName=" + hostname;
			JSONObject json = readJsonFromUrl(url);
			Evidencia.crearUrl(json);

//			hostname = Character.isDigit(workerName.charAt(workerName.length() - 1)) ? "GMUDEXEC" : "GMUDEXEC_HEADLESS";
//
//			/**
//			 * Ajuste para hostname priorizado e pool primeiro verifica que o hostname
//			 * default tem execuções, caso não usa o nome pool
//			 * 
//			 * Excluindo os debugs locais
//			 */
//			if (json == null) {
//				if (!workerName.toLowerCase().contains("QADEVEL")) {
//					json = readJsonFromUrl(this.ApiURL + "v1/executions/workers?workerName=" + hostname);
//					if (json == null)
//						return null;
//				} else {
//					return null;
//				}
//			}

			return getObjectExecutionData(json);
		} catch (Exception e) {
			return null;
		}
	}

	private String SendGet(String url) throws Exception {
		BufferedReader is = null;
		HttpURLConnection con = null;

		try {
			URL obj = new URL(url);
			con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json, charset=utf-8");
			con.setRequestProperty("Authorization", "Bearer " + _bearer);
			is = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
			String jsonText = readAll(is);
			return jsonText;
		} catch (Exception e) {
			throw e;
		} finally {
			is.close();
			con.disconnect();
		}
	}

	private JSONObject readJsonFromUrl(String url) throws Exception {
		String jsonText = "";
		try {
			try {
				if (!isWindows)
					Authentication();
				jsonText = SendGet(url);

			} catch (NullPointerException e) {
				Authentication();
				jsonText = SendGet(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (jsonText.equals(""))
				return null;

			JSONObject json = new JSONObject(jsonText);

			if (_requestToSTARCAPI) {

				try {
					JSONObject data = json.getJSONObject("Data");

					json = data;
				} catch (JSONException e) {
					json = null;
				}
			}
			return json;

		} catch (Exception e) {
			return null;
		}
	}

	private ExecutionData getObjectExecutionData(JSONObject json) {
		try {
			String jsonString = json.toString();
			ObjectMapper objMapper = new ObjectMapper();

			return objMapper.readValue(jsonString, ExecutionData.class);
		} catch (Exception e) {
			return null;
		}
	}

	public void SendPost(ExecutionData executionData) throws Exception {
		OutputStream os = null;
		HttpURLConnection con = null;

		try {

			URL url = new URL(this.ApiURL + "v1/executions/update");
			con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("Authorization", "Bearer " + _bearer);
			ObjectMapper objectMapper = new ObjectMapper();
			String stringExecutionData = objectMapper.writeValueAsString(executionData);

			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			con.setDoOutput(true);

			os = con.getOutputStream();
			byte[] input = stringExecutionData.getBytes();
			String in = new String(input, StandardCharsets.UTF_8);
			os.write(in.getBytes(), 0, in.getBytes().length);

			try (BufferedReader br = new BufferedReader(
					new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;

				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
			} catch (Exception e) {
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			os.close();
			con.disconnect();
		}
	}

	public void SendPrintScreen(Prints prints) throws Exception {
		OutputStream os = null;
		HttpURLConnection con = null;
		try {

			URL url = new URL(this.ApiURL + "v1/executions/prints");
			con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("Authorization", "Bearer " + _bearer);
			ObjectMapper objectMapper = new ObjectMapper();
			String stringPrints = objectMapper.writeValueAsString(prints);

			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; UTF-8");
			con.setDoOutput(true);

			os = con.getOutputStream();
			byte[] input = stringPrints.getBytes();
			os.write(input, 0, input.length);

			try (BufferedReader br = new BufferedReader(
					new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;

				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
			} catch (Exception e) {
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			os.close();
			con.disconnect();
		}
	}

	public void sendExecutionData(ExecutionData executionData) throws Exception {
		try {
			System.out.println("Test finished, sending data to xcelera.");
			SendPost(executionData);
			System.out.println();
			System.out.println("***************************************************************************************************************************************************************************");
			System.out.println();
		} catch (IOException e) {
			Authentication();
			SendPost(executionData);
		} catch (Exception e) {
			throw e;
		}
	}


	public void Authentication() throws Exception {
		try {
			//Descomentar siguiente linea para Correccion error: Remote host closed connection during handshake
			InvalidCertificates.invalidAll();
			//HttpsTrustManager.allowAllSSL();
			ConfigFileReader reader = new ConfigFileReader("configs/config.properties");
			String userName = reader.getPropertyByKey("userName");
			String password = reader.getPropertyByKey("password");
			String accessKey = reader.getPropertyByKey("accessKey");

			URL url = new URL(this.ApiURL + "auth");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("POST");
			con.setRequestProperty("Content-type", "application/json; utf-8");
			con.setDoOutput(true);

			String body = "{'Username':'" + userName + "', 'Password':'" + password + "', 'AccessKey':'" + accessKey + "'}";
			try (OutputStream os = con.getOutputStream()) {
				byte[] input = body.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				JSONObject json = new JSONObject(response.toString());
				JSONObject data = json.getJSONObject("Data");
				_bearer = data.getString("accessToken");
			} catch (Exception e) {
				throw e;
			}
		} catch (Exception e) {
			throw e;
		}
	}


	public InputStream  getPdf(String url) throws Exception{
		Authentication();
		BufferedReader is = null;
		HttpURLConnection con = null;

		try {
			URL obj = new URL(url);
			con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept","application/json, text/plain, */*");
			con.setRequestProperty("Accept-Encoding","gzip, deflate, br");
			con.setRequestProperty("Authorization", "Bearer " + _bearer);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("sec-ch-ua-platform", "Windows");
			con.setDoOutput(true);
			InputStream inputStream = con.getInputStream();
			return inputStream;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}





}
