package xml.generation;

import com.google.common.collect.ImmutableList;
import model.entities.Fact;
import model.entities.Root;
import model.entities.Rule;
import util.RandomString;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class RandomGenerator {

    private static final String TMP_DIR = "basic/src/main/resources/tmp";
    private static final RandomString RANDOM_STRING = new RandomString();

//    public void generate() throws IOException {
//        prepareTmpDir();
//
//        String path = "basic/src/main/resources/knowledge_base.xsd";
//        File file = new File(path);
//        InputStream is = new FileInputStream(file);
//
//        XmlSchemaCollection coll = new XmlSchemaCollection();
//        coll.setBaseUri(file.toString());
//
//        StreamSource source = new StreamSource(is);
//        XmlSchema schema = coll.read(source);
//
//        XmlGenOptions options = new XmlGenOptions();
//        options.setMaxRecursiveDepth(1);
//        options.setMaxRepeatingElements(1);
//        options.setGenOptionalElements(true);
//        options.setDefVals(DefaultValues.DEFAULT);
//        SchemaTypeXmlGenerator generator = new SchemaTypeXmlGenerator(coll, options);
//        boolean isPretty = true;
////        System.out.println(generator.generateXml(new QName("root"), isPretty));
//
//        PrintWriter writer = new PrintWriter(TMP_DIR + "/tmp_" + new Date().getTime() + ".xml", "UTF-8");
//        writer.println(generator.generateXml(new QName("root"), isPretty));
//        writer.close();
//    }

    public static void main(String[] args) throws IOException {
        prepareTmpDir();
        Root root = new Root();
        Root.RulesList rulesList = new Root.RulesList();
        Rule rule = new Rule();
        Fact input = new Fact();
        input.setName("1");
        rule.setInputs(ImmutableList.of(input));
        Fact out = new Fact();
        out.setName("1");
        rule.setOutput(out);
        rulesList.setRules(ImmutableList.of(rule));
        root.setRulesList(rulesList);
        root.setFactsList(getStatesList());
        try {

            File file = new File(TMP_DIR + "/tmp_" + new Date().getTime() + ".xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Root.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(root, file);
            jaxbMarshaller.marshal(root, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    private static Root.FactsList getStatesList() {
        Root.FactsList factsList = new Root.FactsList();
        factsList.setFacts(getInputs());
        return factsList;
    }

    private static List<Fact> getInputs() {
        List<Fact> input = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            Fact fact = new Fact();
            fact.setIndex(i);
            fact.setName(RANDOM_STRING.nextString());
            input.add(fact);
        }
        return input;
    }

    private Fact getOutput() {
        Fact output = new Fact();
        output.setIndex(1);
        output.setName("1");
        return output;
    }

    private static void prepareTmpDir() throws IOException {
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
