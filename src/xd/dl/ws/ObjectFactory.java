
package xd.dl.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the xd.dl.ws package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _ProcParkingFeeResponseReturn_QNAME = new QName("http://service.geweb.com", "return");
	private final static QName _ProcParkingFeeJson_QNAME = new QName("http://service.geweb.com", "json");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: xd.dl.ws
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link ProcParkingFeeResponse }
	 * 
	 */
	public ProcParkingFeeResponse createProcParkingFeeResponse() {
		return new ProcParkingFeeResponse();
	}

	/**
	 * Create an instance of {@link ProcParkingFee }
	 * 
	 */
	public ProcParkingFee createProcParkingFee() {
		return new ProcParkingFee();
	}

	/**
	 * Create an instance of {@link DllGetCardParktime }
	 * 
	 */
	public DllGetCardParktime createDllGetCardParktime() {
		return new DllGetCardParktime();
	}

	/**
	 * Create an instance of {@link CancelParkingFeeResponse }
	 * 
	 */
	public CancelParkingFeeResponse createCancelParkingFeeResponse() {
		return new CancelParkingFeeResponse();
	}

	/**
	 * Create an instance of {@link DllGetCardParktimeResponse }
	 * 
	 */
	public DllGetCardParktimeResponse createDllGetCardParktimeResponse() {
		return new DllGetCardParktimeResponse();
	}

	/**
	 * Create an instance of {@link CancelParkingFee }
	 * 
	 */
	public CancelParkingFee createCancelParkingFee() {
		return new CancelParkingFee();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String
	 * }{@code >}}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.geweb.com", name = "return", scope = ProcParkingFeeResponse.class)
	public JAXBElement<String> createProcParkingFeeResponseReturn(String value) {
		return new JAXBElement<String>(_ProcParkingFeeResponseReturn_QNAME, String.class, ProcParkingFeeResponse.class,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String
	 * }{@code >}}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.geweb.com", name = "json", scope = ProcParkingFee.class)
	public JAXBElement<String> createProcParkingFeeJson(String value) {
		return new JAXBElement<String>(_ProcParkingFeeJson_QNAME, String.class, ProcParkingFee.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String
	 * }{@code >}}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.geweb.com", name = "json", scope = DllGetCardParktime.class)
	public JAXBElement<String> createDllGetCardParktimeJson(String value) {
		return new JAXBElement<String>(_ProcParkingFeeJson_QNAME, String.class, DllGetCardParktime.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String
	 * }{@code >}}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.geweb.com", name = "return", scope = CancelParkingFeeResponse.class)
	public JAXBElement<String> createCancelParkingFeeResponseReturn(String value) {
		return new JAXBElement<String>(_ProcParkingFeeResponseReturn_QNAME, String.class,
				CancelParkingFeeResponse.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String
	 * }{@code >}}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.geweb.com", name = "return", scope = DllGetCardParktimeResponse.class)
	public JAXBElement<String> createDllGetCardParktimeResponseReturn(String value) {
		return new JAXBElement<String>(_ProcParkingFeeResponseReturn_QNAME, String.class,
				DllGetCardParktimeResponse.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String
	 * }{@code >}}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.geweb.com", name = "json", scope = CancelParkingFee.class)
	public JAXBElement<String> createCancelParkingFeeJson(String value) {
		return new JAXBElement<String>(_ProcParkingFeeJson_QNAME, String.class, CancelParkingFee.class, value);
	}

}
