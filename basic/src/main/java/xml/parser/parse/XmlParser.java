package xml.parser.parse;

import model.dto.Root;
import model.entities.Fact;
import model.entities.Rule;
import model.entities.utils.RuleUtils;
import xml.parser.utils.Constants;
import xml.validation.Validator;
import xml.validation.XSDValidator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static model.entities.Operation.NAND;
import static model.entities.utils.FactUtils.createFact;
import static model.entities.utils.FactUtils.getFact;

/**
 * Add class description
 */
public class XmlParser {

    private static final File XSD_FILE = new File("basic/src/main/resources/knowledge_base.xsd");
    private static final File RULES_FILE = new File("basic/src/main/resources/sample.xml");
    private static JAXBContext jaxbContext;

    private XmlParser() {
    }

    public static Set<Rule> parseDefaultCfg(String pathToFile) throws IOException {
        return parseDefaultCfg(new File(pathToFile));
    }

    public static Set<Rule> parseDefaultCfg() {
        return parseDefaultCfg(RULES_FILE);
    }

    public static Set<Rule> parseDefaultCfg(File file) {
        boolean isValid = XSDValidator.validateXMLSchema(XSD_FILE, file);
        if (!isValid) {
            System.out.println(Constants.INVALID_XML_CFG);
            return Collections.emptySet();
        }
        return doParse();
    }

    private static Set<Rule> doParse() {
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

    private static Set<Rule> getGates(Root root) {
        Set<Rule> rules = new HashSet<>();
        root.getFacts().forEach(fact -> createFact(fact.getName(), fact.getId()));

        root.getRules().forEach(ruleDto -> {
            SortedSet<Fact> preconditions = new TreeSet<>();
            ruleDto.getInputs().forEach(
                    factDto -> {
                        Fact fact = getFact(Integer.parseInt(factDto.getName()));
                        fact.setNegation(factDto.isNegation());
                        preconditions.add(fact);
                    });
            Fact action = getFact(Integer.parseInt(ruleDto.getOutput().getName()));
            action.setNegation(ruleDto.getOutput().isNegation());

            Rule rule = RuleUtils.getRule(preconditions, action);
            rule.setRuleId(ruleDto.getId());

            // TODO there are WA for stabilization.
            // Need to replace with PODEM code for work with negation rule
            if (isNegation(rule)) {
                rule.setOperation(NAND);
            }
            rules.add(rule);
        });
        return rules;
    }

    private static boolean isNegation(Rule rule) {
        return rule.isNegatedFact(rule.getOutput().getIndex());
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
