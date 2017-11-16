package xml.generation;

import com.stratumsoft.xmlgen.DefaultValues;
import com.stratumsoft.xmlgen.XmlGenOptions;
import org.apache.ws.commons.schema.XmlSchemaCollection;

import javax.xml.bind.annotation.XmlSchema;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.net.URL;

public class RandomGenerator {

    public void generate() {
        String path = "basic/src/test/resources/test_1.xml";
        InputStream is = this.getClass().getResourceAsStream(path);
        URL xsdUrl = this.getClass().getResource(path);

        XmlSchemaCollection coll = new XmlSchemaCollection();
        coll.setBaseUri(xsdUrl.toString());

        StreamSource source = new StreamSource(is);
        XmlSchema schema = (XmlSchema) coll.read(source);

        XmlGenOptions options = new XmlGenOptions();
        options.setGenCommentsForParticles(true);
        options.setGenChoiceOptionsAsComments(false);
        options.setMaxRecursiveDepth(1);
        options.setMaxRepeatingElements(2);
        options.setDefVals(DefaultValues.DEFAULT);


    }
}
