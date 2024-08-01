package framework.data.dynamicValues;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import framework.RestWebService;
import framework.custom.Custom;
import framework.custom.entities.StaticFields;
import framework.data.entities.LayoutFileData;
import framework.data.entities.Procedure;
import framework.enums.TypeError;
import framework.helpers.ExecutionStatusHelper;
import framework.helpers.GeneralHelper;

public class DynamicValuesHelper {

	public static Integer installmentDay = 0;
	public static String dateFirstPayment;
	public static String dateFirstInstallment;

	Logger logger=Logger.getLogger(DynamicValuesHelper.class.getName());

	public void changeDynamicValue(int locatorNro, Procedure procedure, Map<String, String> executionConfigs)
			throws Exception {
		try {
			procedure.Value = changeDynamicValueData(procedure.Value, executionConfigs);
			procedure.Object.Locator1 = GeneralHelper.getLocatorProcedure(locatorNro, procedure);
		} catch (Exception e) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			String comment = GeneralHelper.getCommentError(methodName, e.getMessage());
			ExecutionStatusHelper helper = new ExecutionStatusHelper();
			helper.setStatusErrorOnProcedure(procedure, TypeError.OperationalError, comment);
		}
	}

	public String changeDynamicValueData(String value, Map<String, String> executionConfigs) throws Exception {
		boolean toCache = false;
		String chave = null;

		if (value == null || value.isEmpty())
			return "";

		if (value.toLowerCase().contains("#cached(")) {
			chave = value.replace("#cached(", "").replace(")", "");
			chave += ")";
			return StaticFields.getDataCached(chave);
		} else if (value.toLowerCase().contains("#cached")) {
			return StaticFields.getCachedQueryValue();
		} else if (value.toLowerCase().contains("#cache")) {
			value = value.replace("#cache(", "").replace(")", "") + ")";
			chave = value;
			toCache = true;
		}

		if (value.contains("#DynamicAlias=")) {
			if (value.contains("|"))
				value = value.substring(value.indexOf("#DynamicAlias=") + 14, value.indexOf("|"));
			else
				value = value.substring(value.indexOf("#DynamicAlias=") + 14);
		}

		if (value.toLowerCase().contains("#dynamicname")) {
			UUID uuid = UUID.randomUUID();
			String myRandom = uuid.toString();
			String letras = myRandom.substring(0, 10).replace("-", "");
			value = "TESTE GRUPO HDI " + letras;
		}

		if (value.startsWith("@")) {
			DynamicValuesExcel valuesExcel = new DynamicValuesExcel();
			value = valuesExcel.getValue(1, value);
		}
		if (value.startsWith("#")) {
			executeFunction(value);
		}

		if (!value.toLowerCase().contains("<ignore>")) {
			String finalValue = value;
			logger.log(Level.INFO,() ->"Valor Recibido de Xcelera: " + finalValue);
		}

		if (toCache)
			StaticFields.setLIST_DATA_CACHED(chave, value);

		return value;
	}


	public JSONObject changeDynamicWebServiceBody(String webServiceBody, Map<String, String> executionConfigs)
			throws Exception {

		JSONObject json = new JSONObject(webServiceBody);

		Set<String> keys = json.keySet();
		for (String key : keys) {

			Object jsonValue = json.get(key);
			if (jsonValue instanceof JSONObject) {
				JSONObject replacedJson = changeDynamicWebServiceBody(jsonValue.toString(), executionConfigs);
				json.put(key, replacedJson);
			} else if (jsonValue instanceof JSONArray) {
				JSONArray replacedJsonArray = new JSONArray();
				for (int i = 0; i <= ((JSONArray) jsonValue).length() - 1; i++) {

					if (((JSONArray) jsonValue).get(i) instanceof JSONObject) {
						JSONObject jsonObj = ((JSONArray) jsonValue).getJSONObject(i);
						replacedJsonArray.put(changeDynamicWebServiceBody(jsonObj.toString(), executionConfigs));
					} else if (((JSONArray) jsonValue).get(i) instanceof Integer) {
						Object jsonObj = ((JSONArray) jsonValue).get(i);
						replacedJsonArray
								.put(Integer.parseInt(changeDynamicValueData(jsonObj.toString(), executionConfigs)));
					} else if (((JSONArray) jsonValue).get(i) instanceof Boolean) {
						Object jsonObj = ((JSONArray) jsonValue).get(i);
						replacedJsonArray.put(
								Boolean.parseBoolean(changeDynamicValueData(jsonObj.toString(), executionConfigs)));
					} else if (((JSONArray) jsonValue).get(i) instanceof Double) {
						Object jsonObj = ((JSONArray) jsonValue).get(i);
						replacedJsonArray
								.put(Double.parseDouble(changeDynamicValueData(jsonObj.toString(), executionConfigs)));
					} else {
						Object jsonObj = ((JSONArray) jsonValue).get(i);
						replacedJsonArray.put(changeDynamicValueData(jsonObj.toString(), executionConfigs));
					}

				}

				json.put(key, new JSONArray(replacedJsonArray.toString()));
			} else if (jsonValue instanceof Integer) {
				json.put(key, Integer.parseInt(changeDynamicValueData(jsonValue.toString(), executionConfigs)));
			} else if (jsonValue instanceof Boolean) {
				json.put(key, Boolean.parseBoolean(changeDynamicValueData(jsonValue.toString(), executionConfigs)));
			} else if (jsonValue instanceof Double) {
				json.put(key, Double.parseDouble(changeDynamicValueData(jsonValue.toString(), executionConfigs)));
			} else {
				json.put(key, changeDynamicValueData(jsonValue.toString(), executionConfigs));
			}
		}

		return json;
	}

	/*
	 * This method need to be customized to get path and file name correctly
	 */
	public void changeDynamicPathAndFilename(LayoutFileData layoutFileData) throws Exception {
		if (layoutFileData.PathAndFilename.contains("#FILEPATH")) {
			if (layoutFileData.TypeFileName.contains("CSV"))
				layoutFileData.PathAndFilename = "C:\\HDI\\Temp\\caixa_2020-12-16-11-26-46.csv";
			else
				layoutFileData.PathAndFilename = "C:\\HDI\\Temp\\arquivo_2020-12-29_17_59.txt";
		}
	}

	public static String getdateFirstInstallment(int days) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd");
		c.getTime().toString();
		c.setTime(c.getTime());
		c.add(Calendar.DAY_OF_MONTH, days);
		dateFirstInstallment = formatador.format(c.getTime());
		return dateFirstInstallment;
	}

	public static String getdateFirstPayment(int days) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd");
		c.getTime().toString();
		c.setTime(c.getTime());
		c.add(Calendar.DAY_OF_MONTH, days);
		dateFirstPayment = formatador.format(c.getTime());
		return dateFirstPayment;
	}

	public String executeFunction(String valor)
	{
		try {
			Method methodObj; //Object to invoke the method

			String methodParameters = valor.substring(valor.indexOf("(")+1, valor.indexOf(")"));
			String methodName = valor.substring(valor.indexOf("#") + 1,valor.indexOf("("));
			String[] parametersArray = methodParameters.split(",");


			if (!methodParameters.equals(""))
			{
				//Parameter type list
				Class<?>[] parTypeList = new Class[parametersArray.length];

				//Parameter value list
				Object[] parValueList = new Object[ parametersArray.length];

				int currentArrayPosition;
				for (currentArrayPosition = 0; currentArrayPosition < parametersArray.length; currentArrayPosition++ )
				{
					parTypeList[currentArrayPosition] =  String.class;
					parValueList[currentArrayPosition] = parametersArray[currentArrayPosition];
				}

				//Find class where method is created
				Class<?> findMethodClassReturn = findMethodClass(methodName , parTypeList);

				if (findMethodClassReturn != null)
				{
					//Get method object
					methodObj = findMethodClassReturn.getMethod(methodName, parTypeList);

					//Execute Method
					if (findMethodClassReturn == framework.data.dynamicValues.DynamicValuesCustom.class)
					{
						DynamicValuesCustom classObj = new DynamicValuesCustom();
						return String.valueOf( methodObj.invoke(classObj, parValueList));
					}else
					{
						return String.valueOf( methodObj.invoke(this, parValueList));
					}
				}
			}
			else
			{
				//Find class where method is created
				Class<?> findMethodClassReturn = findMethodClass(methodName,null);

				if (findMethodClassReturn != null)
				{
					//Get method object
					methodObj = findMethodClassReturn.getMethod(methodName,null);

					//Execute Method
					if (findMethodClassReturn == framework.data.dynamicValues.DynamicValuesCustom.class)
					{
						DynamicValuesCustom classObj = new DynamicValuesCustom();
						return String.valueOf( methodObj.invoke(classObj, null));
					}else
					{
						return String.valueOf( methodObj.invoke(this, null));
					}
				}
				return "";
			}
		} catch (InvocationTargetException exception) {
			throw new RuntimeException(exception);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Class<?> findMethodClass(String methodName, Class<?>[] parTypeList)
	{
		try
		{
			Class<?> methodClass = Class.forName("framework.data.dynamicValues.DynamicValuesCustom");
			methodClass.getMethod(methodName, parTypeList);
			return methodClass;
		}
		catch (NoSuchMethodException | ClassNotFoundException e)
		{
			logger.log(Level.SEVERE,e.getMessage());
		}
		return null;
	}



	public String captureProp(String valueStr,String prop,Boolean changeFV)
	{
		String ret;
		int iniProp;
		int endProp;
		//int lenProp;

		if (valueStr.equals("") || valueStr.indexOf(prop + "=") < 0) //$NON-NLS-1$ //$NON-NLS-2$
			ret = ""; //$NON-NLS-1$
		else
		{
			iniProp = valueStr.indexOf(prop + "="); //$NON-NLS-1$
			endProp = valueStr.indexOf("|", iniProp ); //$NON-NLS-1$
			if (endProp < iniProp)
				endProp = valueStr.length();
			else
				endProp = valueStr.indexOf("|", iniProp); //$NON-NLS-1$

			iniProp = iniProp + (prop.length() +1);
			//lenProp = (endProp - iniProp) ;
			ret = valueStr.substring(iniProp, endProp);
		}

		if(changeFV)
		{
			if(ret.indexOf("#")<ret.indexOf("(")){ //$NON-NLS-1$ //$NON-NLS-2$
				String newRet=changeFunctionValue(ret);
				valueStr=valueStr.replace(ret, newRet);
				ret=newRet;
			}

		}
		return ret;
	}

	private String changeFunctionValue(String strValue){
		String strResult=strValue;
		String strFunction=""; //$NON-NLS-1$
		String strParam=""; //$NON-NLS-1$

		if(strResult.indexOf("(")>strResult.indexOf("#")){ //$NON-NLS-1$ //$NON-NLS-2$
			strFunction=strResult.substring(strResult.indexOf("#")+1, strResult.indexOf("(")); //$NON-NLS-1$ //$NON-NLS-2$
			strParam=strResult.substring(strResult.indexOf("(")+1,strResult.length()-1); //$NON-NLS-1$

			strResult=executeFunction(strResult);
		}
		//return result
		strResult=strValue.replace("#"+strFunction+"("+strParam+")", strResult); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return strResult;
	}
}
