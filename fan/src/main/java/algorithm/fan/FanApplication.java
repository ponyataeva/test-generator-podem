package algorithm.fan;

import algorithm.podem.PodemApplication;
import model.entities.Fact;
import model.entities.FaultType;
import model.entities.Rule;
import model.entities.Scheme;
import model.entities.utils.FactUtils;
import xml.parser.parse.XmlParser;

import java.io.IOException;
import java.util.Set;

public class FanApplication extends PodemApplication {

    private FanApplication(Scheme scheme, Fact fault) {
        super(scheme, fault);
    }

    // TODO add static creation of class
    // TODO and objects can be compared by ==
    // TODO переопределить hashCode везде, где переопределен equals
    public static void main(String[] args) throws IOException {
        Set<Rule> rules = XmlParser.parseDefaultCfg();
        Scheme scheme = new Scheme(rules);
        FanApplication app = new FanApplication(scheme, FactUtils.getFact("K"));

        FactUtils.getFact("K").setFaultType(FaultType.sa0);
            try {
                app.execute();
                System.out.println(scheme.getTest());
                scheme.print();
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("====================================================================");
    }

    public boolean execute() {

        return false;
    }
}
