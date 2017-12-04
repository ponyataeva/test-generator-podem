package xml.parser.parse;

import model.dto.Root;
import model.dto.Rule;
import model.entities.Fact;
import model.entities.Gate;
import model.entities.utils.FactUtils;
import model.entities.utils.GateUtils;
import xml.parser.utils.Constants;
import xml.validation.Validator;
import xml.validation.XSDValidator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static model.entities.Operation.NAND;

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
        root.getFacts().forEach(fact -> FactUtils.getFact(fact.getName(), fact.getId()));
        for (Rule rule : root.getRules()) {
            SortedSet<Fact> preconditions = new TreeSet<>();
            rule.getInputs().forEach(fact -> preconditions.add(FactUtils.getFact(Integer.parseInt(fact.getName()))));
            Fact action = FactUtils.getFact(Integer.parseInt(rule.getOutput().getName()));
            Gate g = GateUtils.getGate(preconditions, action);
            g.setRuleId(rule.getId());

            if (isNegation(rule)) { // TODO there are WA for stabilization. Need to replace with PODEM code for work with negation rule
                g.setOperation(NAND);
            }
            gates.add(g);
        }
        return gates;
    }

    private static boolean isNegation(Rule rule) {
        for (model.dto.Fact fact : rule.getInputs()) {
            if (fact.isNegation()) {
                return true;
            }
        }

        return rule.getOutput().isNegation();
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
