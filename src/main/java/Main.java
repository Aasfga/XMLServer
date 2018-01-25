import SchemaClasses.Person;
import SchemaClasses.PersonList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;

public class Main
{
    public static void main(String[] args) throws JAXBException, SAXException
    {
        JAXBContext jc = JAXBContext.newInstance( "SchemaClasses" );
        Unmarshaller u = jc.createUnmarshaller();
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(new File(Main.class.getResource("FilleValidator/XML/schema.xsd").getPath()));
        u.setSchema(schema);
        JAXBElement element = (JAXBElement) u.unmarshal( new File( Main.class.getResource("FilleValidator/XML/xml_examples/person_list_example.xml").getPath()) );
        PersonList list = (PersonList) element.getValue();
        for(Person p : list.getPersons())
            System.out.println(p.getName());
    }
}
