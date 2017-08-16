package parse;

import entities.Gate;
import entities.Root;
import entities.Rule;
import entities.State;
import entities.utils.GateUtils;
import utils.Constants;
import validation.Validator;
import validation.XSDValidator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Add class description
 */
public class XmlParser {

    private static final File XSD_FILE = new File("rules-parser/files/knowledge_base.xsd");
    private static final File RULES_FILE = new File("rules-parser/files/sample.xml");
    private static JAXBContext jaxbContext;

    public static void parseDefaultCfg() {
        boolean isValid = XSDValidator.validateXMLSchema(XSD_FILE, RULES_FILE);
        if (!isValid) {
            System.out.println(Constants.INVALID_XML_CFG);
            return;
        }
        doParse();
    }

    private static Set<Gate> doParse() {
        Root root = parseToRoot();
        System.out.println(root);
        Validator.checkDuplicates(root.getStates());
        Validator.validateInversionsExists(root.getRules());
        return getGates(root);
    }

    private static Root parseToRoot() {
        Unmarshaller jaxbUnmarshaller = null;
        try {
            jaxbUnmarshaller = getJaxbContext().createUnmarshaller();
            return (Root) jaxbUnmarshaller.unmarshal(RULES_FILE);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private static Set<Gate> getGates(Root root) {
        Set<Gate> gates = new HashSet<>();
        for (Rule rule : root.getRules()) {
            List<State> preconditions = rule.getInputs();
            State action = rule.getOutput();
            Gate g = GateUtils.getGate(preconditions, action);
            g.setRuleId(rule.getIndex());
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
