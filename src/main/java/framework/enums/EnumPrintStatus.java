package framework.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EnumPrintStatus {
	HasPrint(1),
	NoPrint(2),
	ExpiredPrint(4);
	
	private final int codigo;
	
	EnumPrintStatus(int codigo) { 
		this.codigo = codigo; 
	}
	
    public int getValue() { 
    	return this.codigo; 
	}
    
    @JsonCreator
    public static EnumPrintStatus forValue(int codigo) {
    	for(EnumPrintStatus p : EnumPrintStatus.values()) {
    		if( p.codigo == codigo)
    			return p;
    	}
    	return null;
    }
}
