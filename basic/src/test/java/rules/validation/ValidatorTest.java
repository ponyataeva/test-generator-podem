package rules.validation;

import model.dto.Root;
import org.junit.Before;
import org.junit.Test;
import rules.parser.utils.XMLValidationException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class ValidatorTest {

    private static final File TEST_1_FILE = new File("src/test/resources/test_1.xml");
    private static final File TEST_2_FILE = new File("src/test/resources/test_2.xml");
    private static final File TEST_3_FILE = new File("src/test/resources/test_3.xml");
    private static final File TEST_4_FILE = new File("src/test/resources/test_4.xml");
    private static JAXBContext jaxbContext;

    private Root test1;
    private Root test2;
    private Root test3;
    private Root test4;

    @Before
    public void loadTestFiles() throws JAXBException {
        Unmarshaller jaxbUnmarshaller = getJaxbContext().createUnmarshaller();
        this.test1 = (Root) jaxbUnmarshaller.unmarshal(TEST_1_FILE);
        this.test2 = (Root) jaxbUnmarshaller.unmarshal(TEST_2_FILE);
        this.test3 = (Root) jaxbUnmarshaller.unmarshal(TEST_3_FILE);
        this.test4 = (Root) jaxbUnmarshaller.unmarshal(TEST_4_FILE);
    }

    private static JAXBContext getJaxbContext() {
        if (jaxbContext == null) {
            try {
                jaxbContext = JAXBContext.newInstance(Root.class);
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
        }
        return jaxbContext;
    }

    @Test(expected = XMLValidationException.class)
    public void doValidation_success_exceptionDoesntThrows() {
        new Validator(test1).doValidation();
    }

}