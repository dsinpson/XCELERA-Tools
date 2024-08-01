package framework.data.entities;

import java.util.List;
import framework.enums.EnumPrintStatus;

public class ActionData {
	
	public long ActionId;
    
	public String Name;
    
	public int Order;
    
	public List<Procedure> Procedures;
	
	public WebServiceData WebService;
	
	public LayoutFileData LayoutFile;
    
	public String Status;
    
	public String TypeError;
    
	public String Comment;
	
	public String Description;
	
	public String ExpectedResult;
	
	public EnumPrintStatus PrintStatus;
	
	public String StartDate;
	
	public String EndDate;
	
	public String Duration;
	
	public String RepositoryType;
	
	public List<TestValues> TestValues;
	public boolean ReadyToPlay;
	public boolean OnlyAutomated;
}
