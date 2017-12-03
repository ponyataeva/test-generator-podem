package xml.generation;

import model.dto.Fact;
import model.dto.Root;
import model.dto.Rule;
import model.dto.XmlBaseObject;
import xml.generation.util.RandomString;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomGenerator {

    private static final String TMP_DIR = "basic/src/main/resources/tmp";
    private static final RandomString RANDOM_STRING = new RandomString();
    private static final Random RANDOM_BOOLEAN = new Random();
    private static AtomicInteger factSequence = new AtomicInteger();
    private static AtomicInteger ruleSequence = new AtomicInteger();
    private static Map<Integer, XmlBaseObject> facts = new HashMap<>();
    private static Supplier<IntStream> factIterator;

    public static void generate(int factCount, int fileCount, int ruleCount) throws IOException {
        prepareTmpDir();
        generateFactList(factCount);
        factIterator = () -> new Random().ints(1, facts.size());

        for (int i = 0; i < fileCount; i++) {
            generateFile(generateRules(ruleCount));
        }
    }

    private static Root generateFile(List<Rule> rules) throws IOException {
        Root root = new Root();
        Root.RulesList rulesList = new Root.RulesList();

        rulesList.setRules(rules);
        root.setFactsList(getFactsList());
        root.setRulesList(rulesList);
        try {
            File file = new File(TMP_DIR + "/tmp_" + new Date().getTime() + ".xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Root.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(root, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return root;
    }

    private static Root.FactsList getFactsList() {
        Root.FactsList factsList = new Root.FactsList();
        factsList.setFacts(facts.values().stream().collect(Collectors.toList()));
        return factsList;
    }

    private static void generateFactList(int count) {
        for (int i = 0; i < count; i++) {
            XmlBaseObject fact = new XmlBaseObject();
            fact.setId(factSequence.incrementAndGet());
            fact.setName(RANDOM_STRING.nextString());
            facts.put(fact.getId(), fact);
        }
    }

    private static List<Rule> generateRules(int count) {
        List<Rule> rules = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Rule rule = new Rule();
            rule.setId(ruleSequence.incrementAndGet());
            rule.setInputs(getInputs());
            rule.setOutput(getOutput());
            rules.add(rule);
        }
        return rules;
    }

    private static List<Fact> getInputs() {
        List<Fact> input = new ArrayList<>();
        int count = new Random().ints(1, facts.size() / 2).findAny().getAsInt();
        for (int i = 0; i < count; i++) {
            XmlBaseObject tmpFact = facts.get(factIterator.get().findAny().getAsInt());
            Fact fact = new Fact();
            fact.setName(String.valueOf(tmpFact.getId()));
            fact.setNegation(RANDOM_BOOLEAN.nextBoolean());
            input.add(fact);
        }
        return input;
    }

    private static Fact getOutput() {
        XmlBaseObject output = facts.get(factIterator.get().findFirst().getAsInt());
        Fact fact = new Fact();
        fact.setName(String.valueOf(output.getId()));
        fact.setNegation(false);
        return fact;
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
