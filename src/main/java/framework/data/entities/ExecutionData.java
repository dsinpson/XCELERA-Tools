package framework.data.entities;

import java.util.List;

public class ExecutionData {

	public String Id;
	
	public String UserName;
	
	public long UserId;
    
	public long CustomerId;
	
	public long ProjectId;
	
	public String ProjectName;
    
	public int Cycle;
	
	public int ExecutionNumberPlan;
	
	public int ExecutionPlanItemsId;
	
	public int ExecutionNumberCase;
	
	public List<ExecutionConfig> ExecutionConfig;
	
	public ExecutionPlan ExecutionPlan;
    
	public TestPlan TestPlan;
    
	public TestSuite TestSuite;
    
	public long TestCaseId;
    
	public String TestCaseName;

	public String Status;
    
	public String TypeError;
	
	public String Capabilities;
	
	public String Comment;
    
	public int WorkerId;
    
	public String Worker;
    
	public int DataSet;
    
	public int Order;
    
	public boolean Debug;
    
	public boolean Downloaded;
	
	public boolean Stopped;
    
	public String ScheduledDate;
	
	public String StartDate;
	
	public String EndDate;
	
	public String Duration;

	public String TestCaseDescription;
	
	public String TestCasePreCondition;
	
	public String TestCasePostCondition;
	
	public List<ActionData> Actions;
	
	public FullErrorInfo FullErrorInfo;
	
	public long TestTypeId;
	
	public String TestTypeName;
	
	public WebServiceFullErrorFieldDefinition WebServiceFullErrorFieldDefinition;
}
