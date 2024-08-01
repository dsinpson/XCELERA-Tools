package framework.utils;

public class ClientUtils {
	
	public String formatAttachPagamentoPatrimonial(String valor){

	    String metodoPagamento = "";
	    String parcela = "";

	    if(valor.contains(";")){
	        metodoPagamento = valor.substring(0, valor.indexOf(";")).toLowerCase();
	        parcela = valor.substring(valor.indexOf(";") + 1);
	    }

	    return "//*[normalize-space(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVXWYZ', 'abcdefghijklmnopqrstuvwxyz'))='"
	    + metodoPagamento + "']/../../../..//*[contains(normalize-space(), '" + parcela + "')]/input";

	}

}
