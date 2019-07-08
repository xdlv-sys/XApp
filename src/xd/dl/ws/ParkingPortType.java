
package xd.dl.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * 
 */
@WebService(name = "parkingPortType", targetNamespace = "http://service.geweb.com")
public interface ParkingPortType {

	/**
	 * 
	 * @param json
	 * @return returns java.lang.String
	 */
	@WebMethod(operationName = "CancelParkingFee", action = "urn:CancelParkingFee")
	@WebResult(targetNamespace = "http://service.geweb.com")
	@RequestWrapper(localName = "CancelParkingFee", targetNamespace = "http://service.geweb.com", className = "xd.dl.ws.CancelParkingFee")
	@ResponseWrapper(localName = "CancelParkingFeeResponse", targetNamespace = "http://service.geweb.com", className = "xd.dl.ws.CancelParkingFeeResponse")
	public String cancelParkingFee(@WebParam(name = "json", targetNamespace = "http://service.geweb.com") String json);

	/**
	 * 
	 * @param json
	 * @return returns java.lang.String
	 */
	@WebMethod(operationName = "DllGetCardParktime", action = "urn:DllGetCardParktime")
	@WebResult(targetNamespace = "http://service.geweb.com")
	@RequestWrapper(localName = "DllGetCardParktime", targetNamespace = "http://service.geweb.com", className = "xd.dl.ws.DllGetCardParktime")
	@ResponseWrapper(localName = "DllGetCardParktimeResponse", targetNamespace = "http://service.geweb.com", className = "xd.dl.ws.DllGetCardParktimeResponse")
	public String dllGetCardParktime(
			@WebParam(name = "json", targetNamespace = "http://service.geweb.com") String json);

	/**
	 * 
	 * @param json
	 * @return returns java.lang.String
	 */
	@WebMethod(operationName = "ProcParkingFee", action = "urn:ProcParkingFee")
	@WebResult(targetNamespace = "http://service.geweb.com")
	@RequestWrapper(localName = "ProcParkingFee", targetNamespace = "http://service.geweb.com", className = "xd.dl.ws.ProcParkingFee")
	@ResponseWrapper(localName = "ProcParkingFeeResponse", targetNamespace = "http://service.geweb.com", className = "xd.dl.ws.ProcParkingFeeResponse")
	public String procParkingFee(@WebParam(name = "json", targetNamespace = "http://service.geweb.com") String json);

}