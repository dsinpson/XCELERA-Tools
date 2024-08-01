package framework.data.entities;

import java.util.List;

public class WebServiceData {
	public String Endpoint;
	public String Resource;
	public List<Attribute> Headers;
	public List<Attribute> Parameters;
	public String Method;
	public String Body;
	public String ContentType;
	public ExpectedResponse ExpectedResponse;
	public ObtainedResponse ObtainedResponse;
}
