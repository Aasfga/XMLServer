package FileValidator;

import java.io.InputStream;

public interface Validator
{
    void validate(byte[] xml) throws Exception;

    void validate(String xml) throws Exception;

    void validate(InputStream xml) throws Exception;
}
