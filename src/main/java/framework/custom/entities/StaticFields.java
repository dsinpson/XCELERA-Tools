package framework.custom.entities;

import java.util.HashMap;

import framework.dataProviders.ConfigFileReader;

public final class StaticFields {

	private static String QUERY_CACHED_VALUE = null;

	private static HashMap<String, String> LIST_DATA_CACHED = null;

	public static String getCachedQueryValue() {
		return QUERY_CACHED_VALUE;
	}

	public static void setCachedQueryValue(String cACHED_VALUE) {
		QUERY_CACHED_VALUE = cACHED_VALUE;
	}

	public static HashMap<String, String> getLIST_DATA_CACHED() {
		return LIST_DATA_CACHED;
	}

	public static String getDataCached(String key) {
		String valor = null;

		// condicao ? verdadeira : falsa
		if (LIST_DATA_CACHED.get(key) != null && getLIST_DATA_CACHED().containsKey(key)) {
			valor = getLIST_DATA_CACHED().get(key);
		} else {
			valor = null;
		}

		return valor;
	}

	public static String getWorkerName() {
		ConfigFileReader reader = new ConfigFileReader("configs/config.properties");
		return reader.getPropertyByKey("workerName");
	}
	

	public static String getFarolEndPoint() {
		ConfigFileReader reader = new ConfigFileReader("configs/config.properties");
		try {
			return reader.getPropertyByKey("farol_endpoint");
		} catch (Exception e) {
			return "http://hdixuftapp14";
		}
	}

	public static void setLIST_DATA_CACHED(String key, String valor) {
		LIST_DATA_CACHED.put(key, valor);
	}

	public static void clearContextFields() {
		LIST_DATA_CACHED = null;
		QUERY_CACHED_VALUE = null;
		LIST_DATA_CACHED = new HashMap<String, String>();
	}
}
