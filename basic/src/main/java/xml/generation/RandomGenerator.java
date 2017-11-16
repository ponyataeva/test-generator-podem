package xml.generation;

import com.stratumsoft.xmlgen.DefaultValues;
import com.stratumsoft.xmlgen.SchemaTypeXmlGenerator;
import com.stratumsoft.xmlgen.XmlGenOptions;
import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaCollection;

import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class RandomGenerator {

    public void generate() throws FileNotFoundException {
        String path = "basic/src/main/resources/knowledge_base.xsd";
        File file = new File(path);
        InputStream is = new FileInputStream(file);

        XmlSchemaCollection coll = new XmlSchemaCollection();
        coll.setBaseUri(file.toString());

        StreamSource source = new StreamSource(is);
        XmlSchema schema = coll.read(source);

        XmlGenOptions options = new XmlGenOptions();
        options.setGenCommentsForParticles(true);
        options.setGenChoiceOptionsAsComments(false);
        options.setMaxRecursiveDepth(1);
        options.setMaxRepeatingElements(2);
        options.setDefVals(DefaultValues.DEFAULT);
        SchemaTypeXmlGenerator generator = new SchemaTypeXmlGenerator(coll, options);
        boolean isPretty = true;
        System.out.println(generator.generateXml(new QName("root"), isPretty));

    }
}
