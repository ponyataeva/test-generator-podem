import model.entities.Fact;
import model.entities.Gate;
import model.entities.Scheme;
import model.entities.impl.FaultValueImpl;
import model.entities.utils.FactUtils;
import xml.parser.parse.XmlParser;

import java.io.IOException;
import java.util.Set;

import static model.entities.impl.FaultValueImpl.NONE;

public class Main {
    // TODO add static creation of class
    // TODO and objects can be compared by ==
    // TODO переопределить hashCode везде, где переопределен equals
    public static void main(String[] args) throws IOException {
        Set<Gate> gates = XmlParser.parseDefaultCfg();
        Scheme scheme = new Scheme(gates);

        FactUtils.getFact("K").setFaultType(FaultValueImpl.sa0);

        // TODO need to change model.
        // Now podem work with Gate's operation and negate it.
        // In real life will the fact with negation, and need to inverse it.
        PodemExecutor executor = new PodemExecutor(scheme, getFault(gates));
        executor.execute();
        System.out.println(scheme.getTest());
    }

    private static Fact getFault(Set<Gate> gates) {
        for (Gate gate : gates) {
            for (Fact fact : gate.getInputs()) {
                if (!NONE.equals(fact.getFaultType())) {
                    return fact;
                }
            }
            if (!NONE.equals(gate.getOutput().getFaultType())) {
                return gate.getOutput();
            }
        }
        return null;
    }
}