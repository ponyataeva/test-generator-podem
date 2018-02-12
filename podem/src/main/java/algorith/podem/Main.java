package algorith.podem;

import model.entities.FaultType;
import model.entities.Rule;
import model.entities.Scheme;
import model.entities.utils.FactUtils;
import xml.parser.parse.XmlParser;

import java.io.IOException;
import java.util.Set;

public class Main {
    // TODO add static creation of class
    // TODO and objects can be compared by ==
    // TODO переопределить hashCode везде, где переопределен equals
    public static void main(String[] args) throws IOException {
        Set<Rule> rules = XmlParser.parseDefaultCfg();
        Scheme scheme = new Scheme(rules);

        FactUtils.getAllFacts().forEach(fact -> {
            fact.setFaultType(FaultType.sa0);
            try {
                new PodemApplication(scheme, fact).execute();
                System.out.println(scheme.getTest());
                scheme.print();
            } catch (Exception e) {
                System.out.println(e);
            }
            scheme.clear();
            fact.setFaultType(FaultType.NONE);
            System.out.println("====================================================================");
        });
    }
}