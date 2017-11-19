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
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Date;

public class RandomGenerator {

    private static final String TMP_DIR = "basic/src/main/resources/tmp";

    public void generate() throws IOException {
        prepareTmpDir();

        String path = "basic/src/main/resources/knowledge_base.xsd";
        File file = new File(path);
        InputStream is = new FileInputStream(file);

        XmlSchemaCollection coll = new XmlSchemaCollection();
        coll.setBaseUri(file.toString());

        StreamSource source = new StreamSource(is);
        XmlSchema schema = coll.read(source);

        XmlGenOptions options = new XmlGenOptions();
        options.setMaxRecursiveDepth(3);
        options.setMaxRepeatingElements(1);
        options.setGenOptionalElements(true);
        options.setDefVals(DefaultValues.DEFAULT);
        SchemaTypeXmlGenerator generator = new SchemaTypeXmlGenerator(coll, options);
        boolean isPretty = true;
//        System.out.println(generator.generateXml(new QName("root"), isPretty));

        PrintWriter writer = new PrintWriter(TMP_DIR + "/tmp_" + new Date().getTime() + ".xml", "UTF-8");
        writer.println(generator.generateXml(new QName("root"), isPretty));
        writer.close();
    }

    private void prepareTmpDir() throws IOException {
        Path rootPath = Paths.get(TMP_DIR);
        if (Files.exists(rootPath)) {
            System.out.println("Delete objects: ");
            Files.walk(rootPath, FileVisitOption.FOLLOW_LINKS)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .peek(System.out::println)
                    .forEach(File::delete);
        }
        Files.createDirectory(rootPath);
        System.out.println("Created tmp directory: " + rootPath);
    }
}
