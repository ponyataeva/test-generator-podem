import entities.Root;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Add class description
 */
public class Main {

    public static void main(String[] args) {
        try {
            File file = new File("sample.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Root.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Root root = (Root) jaxbUnmarshaller.unmarshal(file);
            System.out.println(root);

            Validator.validateDuplicates(root.getStatesList().getStates());

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
