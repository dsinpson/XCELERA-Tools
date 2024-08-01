package framework.enums;

public enum TypeError {
	
	Defect(1, "Defect"), 
	OperationalError(2,"Operational Error"),
	Timeout(3, "Timeout"),
	Sucess(4,"Sucess");
	
	private final int Codigo;
	private final String MensagemTypeError;
	
	TypeError(int codigo, String msg){
		Codigo = codigo;
		MensagemTypeError = msg;
		
	}

	public int getCodigo() {
		return Codigo;
	}
	
	public String getMensagemTypeError() {
		return MensagemTypeError;
	}

}
