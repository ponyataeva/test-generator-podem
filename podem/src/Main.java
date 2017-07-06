import entities.Gate;
import entities.Scheme;
import entities.impl.FaultValueImpl;
import entities.utils.StateUtils;
import parser.XmlHelper;

import java.io.IOException;
import java.util.Set;


public class Main {

    public static void main(String[] args) throws IOException {
//        Set<Gate> gates = TextHelper.parse(FileReader.readFile());
        Set<Gate> gates = XmlHelper.parse();
        Scheme scheme = new Scheme(gates);
//        System.out.println(scheme.toString());

        StateUtils.getState("K").setFaultType(FaultValueImpl.sa0);
//        StateUtils.getState("G").setValue(Value.NOT_D);
        PodemExecutor executer = new PodemExecutor(scheme, StateUtils.getState("K"));
//        System.out.println(StateUtils.getAllStates());
        executer.execute();
        System.out.println(scheme.getTest());
    }
}