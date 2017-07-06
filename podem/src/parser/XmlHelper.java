package parser;

import entities.*;
import entities.utils.GateUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static entities.impl.OperationImpl.NAND;

/**
 * Add class description
 */
public class XmlHelper {

    public static Set<Gate> parse() {
        try {
            File file = new File("sample.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Root.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Root root = (Root) jaxbUnmarshaller.unmarshal(file);
            System.out.println(root);

            return parse(root);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Set<Gate> parse(Root root) {
        Set<Gate> gates = new HashSet<>();
        for (Rule rule: root.getRulesList().getRules()) {
            List<State> preconditions = rule.getInputs();
            State action = rule.getOutput();
            Gate g = GateUtils.getGate(preconditions, rule.getOutput());
            gates.add(g);
            if (action.getName().contains("Не")) {
                g.setOperation(NAND);
                String n = action.getName();
            }
        }
        return gates;
    }

}
