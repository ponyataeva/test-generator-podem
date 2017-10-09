public class Main {
// TODO add static creation of class
// TODO and objects can be compared by ==
// TODO переопределить hashCode везде, где переопределен equals
//
//    public static void main(String[] args) throws IOException {
////        Set<Gate> gates = TextHelper.rules.parser.parse(FileReader.readFile());
//        Set<Gate> gates = XmlHelper.rules.parser.parse();
//        Scheme scheme = new Scheme(gates);
////        System.out.println(scheme.toString());
//
//        StateUtils.getState("K").setFaultType(FaultValueImpl.sa0);
////        StateUtils.getState("G").setValue(Value.NOT_D);
//        PodemExecutor executer = new PodemExecutor(scheme, StateUtils.getState("K"));
////        System.out.println(StateUtils.getAllStates());
//        executer.execute();
//        System.out.println(scheme.getTest());
//    }


    public static void main(String[] args) {


        long sum2 = 0L;

        long startTime2 = System.currentTimeMillis();
        for (long i = 0; i < Integer.MAX_VALUE; i++) {
            sum2 += i;
        }

        long stopTime2 = System.currentTimeMillis();
        long elapsedTime2 = stopTime2 - startTime2;
        System.out.println(elapsedTime2);
    }
}