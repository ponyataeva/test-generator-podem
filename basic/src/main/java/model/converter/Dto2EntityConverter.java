package model.converter;

import model.dto.XmlBaseObject;
import model.entities.Fact;
import model.entities.impl.FaultValueImpl;

import java.util.List;

/**
 * Created by Екатерина on 03.12.2017.
 */
public class Dto2EntityConverter {

    public static Fact convertFact(List<XmlBaseObject> allFacts, model.dto.Fact fact) {
        XmlBaseObject factInfo = findFactById(allFacts, Integer.parseInt(fact.getName()));
        Fact result = new Fact();
        result.setName(factInfo.getName());
        result.setIndex(factInfo.getId());
        result.setNegation(fact.isNegation());

        if (result.getName().equals("K")) {
            result.setFaultType(FaultValueImpl.sa0);
        }
        return result;
    }
    private static XmlBaseObject findFactById(List<XmlBaseObject> allFacts, Integer id) {
        return allFacts.stream()
                .filter(fact -> Integer.compare(fact.getId(), id) == 0)
                .findFirst()
                .get();
    }
}
