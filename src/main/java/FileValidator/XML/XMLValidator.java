package FileValidator.XML;

import FileValidator.Validator;
import org.w3c.dom.DocumentType;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;

public class XMLValidator implements Validator
{

    @Override
    public void validate(byte[] xml) throws Exception
    {
        validate(new ByteArrayInputStream(xml));
    }

    @Override
    public void validate(String xml) throws Exception
    {
        validate(xml.getBytes());
    }

    @Override
    public void validate(InputStream xml) throws Exception
    {
        JAXBContext jc = JAXBContext.newInstance(XMLValidator.class, ObjectFactory.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(new File("schema.xsd"));
        unmarshaller.setSchema(schema);
        unmarshaller.unmarshal(xml);
    }
}
