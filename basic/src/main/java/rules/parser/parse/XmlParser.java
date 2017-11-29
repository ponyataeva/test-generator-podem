package rules.parser.parse;

import model.entities.Fact;
import model.entities.Gate;
import model.entities.Root;
import model.entities.Rule;
import model.entities.utils.GateUtils;
import rules.parser.utils.Constants;
import rules.validation.Validator;
import rules.validation.XSDValidator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Add class description
 */
public class XmlParser {

    private static final File XSD_FILE = new File("basic/src/main/resources/knowledge_base.xsd");
    private static final File RULES_FILE = new File("basic/src/main/resources/sample.xml");
    private static JAXBContext jaxbContext;

    public static Set<Gate> parseDefaultCfg() {
        boolean isValid = XSDValidator.validateXMLSchema(XSD_FILE, RULES_FILE);
        if (!isValid) {
            System.out.println(Constants.INVALID_XML_CFG);
            return Collections.emptySet();
        }
        return doParse();
    }

    private static Set<Gate> doParse() {
        Root root = parseToRoot();
        System.out.println(root);
        new Validator(root).doValidation();
        return getGates(root);
    }

    private static Root parseToRoot() {
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
            List<Fact> preconditions = rule.getInputs();
            Fact action = rule.getOutput();
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
