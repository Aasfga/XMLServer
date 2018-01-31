
package FileValidator.XML;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the FileValidator.XML package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CarList_QNAME = new QName("", "CarList");
    private final static QName _PersonList_QNAME = new QName("", "PersonList");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: FileValidator.XML
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CarList }
     * 
     */
    public CarList createCarList() {
        return new CarList();
    }

    /**
     * Create an instance of {@link PersonList }
     * 
     */
    public PersonList createPersonList() {
        return new PersonList();
    }

    /**
     * Create an instance of {@link Car }
     * 
     */
    public Car createCar() {
        return new Car();
    }

    /**
     * Create an instance of {@link Person }
     * 
     */
    public Person createPerson() {
        return new Person();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CarList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CarList")
    public JAXBElement<CarList> createCarList(CarList value) {
        return new JAXBElement<CarList>(_CarList_QNAME, CarList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PersonList")
    public JAXBElement<PersonList> createPersonList(PersonList value) {
        return new JAXBElement<PersonList>(_PersonList_QNAME, PersonList.class, null, value);
    }

}
