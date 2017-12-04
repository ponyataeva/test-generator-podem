package xml.parser.parse;

import model.dto.Root;
import model.dto.Rule;
import model.entities.Fact;
import model.entities.Gate;
import model.entities.utils.GateUtils;
import xml.parser.utils.Constants;
import xml.validation.Validator;
import xml.validation.XSDValidator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static model.converter.Dto2EntityConverter.convertFact;

/**
 * Add class description
 */
public class XmlParser {

    private static final File XSD_FILE = new File("basic/src/main/resources/knowledge_base.xsd");
    private static final File RULES_FILE = new File("basic/src/main/resources/sample.xml");
    private static JAXBContext jaxbContext;

    private XmlParser() {
    }

    public static Set<Gate> parseDefaultCfg(String pathToFile) throws IOException {
        return parseDefaultCfg(new File(pathToFile));
    }

    public static Set<Gate> parseDefaultCfg() {
        return parseDefaultCfg(RULES_FILE);
    }

    public static Set<Gate> parseDefaultCfg(File file) {
        boolean isValid = XSDValidator.validateXMLSchema(XSD_FILE, file);
        if (!isValid) {
            System.out.println(Constants.INVALID_XML_CFG);
            return Collections.emptySet();
        }
        return doParse();
    }

    private static Set<Gate> doParse() {
        Root root = parseToDto();
//        System.out.println(root);
        new Validator(root).doValidation();
        return getGates(root);
    }

    private static Root parseToDto() {
        try {
            Unmarshaller jaxbUnmarshaller = getJaxbContext().createUnmarshaller();
            return (Root) jaxbUnmarshaller.unmarshal(RULES_FILE);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private static Set<Gate> getGates(Root root) {
        Set<Gate> gates = new HashSet<>();
        for (Rule rule : root.getRules()) {
            List<Fact> preconditions = new ArrayList<>();
            rule.getInputs().forEach(fact -> preconditions.add(convertFact(root.getFacts(), fact)));
            Fact action = convertFact(root.getFacts(), rule.getOutput());
            Gate g = GateUtils.getGate(preconditions, action);
            g.setRuleId(rule.getId());
            gates.add(g);
        }
        return gates;
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
}
