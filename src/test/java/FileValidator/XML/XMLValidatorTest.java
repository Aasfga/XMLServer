package FileValidator.XML;

import FileValidator.Validator;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class XMLValidatorTest
{

    @Test
    public void validate_good_files() throws Exception
    {
        Validator validator = new XMLValidator();
        String files[] = {"good_1"};
        for(String file : files)
        {
            validator.validate(getClass().getResourceAsStream(file + ".xml"));
        }
    }

    @Test()
    public void validate_bad_files() throws Exception
    {
        Validator validator = new XMLValidator();
        String files[] = {"wrong_1"};
        for(String file : files)
        {
            try
            {
                validator.validate(getClass().getResourceAsStream(file + ".xml"));
                fail("Should fail");
            } catch(Exception ignored)
            { }
        }
    }
}