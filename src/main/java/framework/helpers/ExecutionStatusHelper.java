package framework.helpers;

import framework.data.entities.ActionData;
import framework.data.entities.ExecutionData;
import framework.data.entities.Procedure;
import framework.dataProviders.ConfigFileReader;
import framework.enums.TypeError;

public class ExecutionStatusHelper {

	public void setStatusErrorOnProcedure(Procedure procedure, TypeError typeError, String comment) {

		if (procedure != null) {
			procedure.TypeError = typeError.getMensagemTypeError();
			procedure.Status = "Failed";
			procedure.Comment = comment;
		}
	}

	public void setStatusLogOkProcedure(Procedure procedure, String comment) {

		if (procedure != null) {
			procedure.Status = "Passed";
			procedure.Comment = comment;
		}
	}

	public void setStatusLogOkAction(ActionData action, Procedure procedure) {

		action.Status = "Passed";
		action.Comment += procedure.Comment;
	}

	public void setStatusErrorOnAction(ActionData actionData, Procedure procedure) {

		String msg = logHostname() + "Execution failed on action: [" + actionData.Name
				+ "]. The following message was shown: ";
		actionData.Status = procedure.Status;
		actionData.TypeError = procedure.TypeError;
		actionData.Comment = msg + procedure.Comment;
	}

	public void setStatusErrorOnAction(ActionData actionData, String message) {
		String msg = logHostname() + "Execution failed on action: [" + actionData.Name
				+ "]. The following message was shown: ";
		actionData.Status = "Failed";
		actionData.TypeError = "Operational Error";
		actionData.Comment = msg + message;
	}

	public void setStatusErrorOnExecution(ExecutionData executionData, ActionData actionData) {

		String msg = logHostname() + "Execution failed on action: [" + actionData.Name + "].";
		executionData.Status = actionData.Status;
		executionData.TypeError = actionData.TypeError;
		executionData.Comment = msg;
	}

	public void setStatusErrorOnExecution(ExecutionData executionData) {

		String msg = logHostname() + "A execução apresentou falha.";
		executionData.Status = "Failed";
		executionData.TypeError = "Unexpected error";
		executionData.Comment = msg;
	}

	private String logHostname() {
		String workerName = "";
		try {
			ConfigFileReader reader = new ConfigFileReader("configs/config.properties");
			workerName = reader.getPropertyByKey("workerName") + "  -> ";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return workerName;
	}
}
