package java8.streamsapi;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.*;

public class StreamsApi {

    private static final Logger LOGGER = Logger.getLogger(StreamsApi.class.getName());

    private static int count = 0;
    public static void main(String[] args) throws IOException {

        // stream of collection
        // getStreamOfCollection().forEach(System.out::println);

        // Stream of array
        Stream<String> streamOfArray = Stream.of("a","b","c");
        String[] arr = new String[]{"a","b","c"};
        Stream<String> streamOfArrayFull = Arrays.stream(arr);
        Stream<String> streamOfArrayPart = Arrays.stream(arr,1,3);
        //streamOfArrayPart.forEach(System.out::println);

        // Stream builder
        Stream<String> streamBuilder = Stream.<String>builder().add("a").add("b").add("c").build();
        //streamBuilder.forEach(System.out::println);

        // Stream.generate() -> generates the infinite stream -> use the limit() method to limit the stream size
        Stream<String> streamGenerated = Stream.generate(() -> "element").limit(10);
        //streamGenerated.forEach(System.out::println);

        // Stream.iterate()
        Stream<Integer> streamIterated = Stream.iterate(40, n -> n + 2).limit(20);
        //streamIterated.forEach(System.out::println);

        // primitive streams
        IntStream intStream = IntStream.range(1,3);
        LongStream longStream = LongStream.rangeClosed(1,3);
        Random random = new Random();
        DoubleStream doubleStream = random.doubles(3);
        //doubleStream.forEach(System.out::println);

        // Stream of String
        IntStream streamOfChars = "abc".chars();
        //streamOfChars.forEach(System.out::println);
        //streamOfChars.mapToObj(c -> Character.valueOf((char)c)).forEach(System.out::println);
        Stream<String> streamOfString = Pattern.compile(", ").splitAsStream("a, b, c");
        //streamOfString.forEach(System.out::println);

        // Stream of File
        Path path = Paths.get("D:\\Tech\\learning\\k8s\\kubectl-commands.txt");
        //Stream<String> streamOfFileLines = Files.lines(path);
        Stream<String> streamOfFileLines = Files.lines(path, Charset.forName("UTF-8"));
        //streamOfFileLines.forEach(System.out::println);

        // Referencing a stream
        // We can instantiate a stream, and have an accessible reference to it, as long as only intermediate
        // operations are called. Executing a terminal operation make a stream inaccessible.
        // So Java8 streams can't be reused once the TERMINAL operation is called on it.
        Stream<String> stream = Stream.of("a","b","c").filter(elem -> elem.contains("b"));
        //Optional<String> anyElem = stream.findAny();
        //System.out.println("Is element present? " + anyElem.isPresent());
        // following will throw an IllegalStateException because stream has already been used by findAny() terminal method call.
        //Optional<String> firstElem = stream.findFirst();
        // So to make previous code work we can make following changes
        List<String> elems = stream.collect(Collectors.toList());
        Optional<String> anyElement = elems.stream().findAny();
        Optional<String> firstElement = elems.stream().findFirst();

        // Stream pipeline
        Stream<String> onceModifiedStream = Stream.of("abcd","bbcd","cbcd").skip(1);
        Stream<String> twiceModifiedStream = Stream.of("abcd","bbcd","cdcd")
                                                    .skip(1)
                                                    .map(e -> e.substring(0,3));
        // A stream by itself is worthless; the user is interested in the result of the terminal operation, which
        // can be a value of some type, or an action applied to every element of the stream.
        // WE CAN USE ONLY ONE TERMINAL OPERATION PER STREAM
        List<String> strList = Arrays.asList("abc1","abc2","abc3");
        long size = strList.stream().skip(1).map(str -> str.substring(0,3)).sorted().count();
        //System.out.println(size);

        // LAZY INVOCATION

        // Intermediate operations are lazy.
        // they will be invoked only if it is necessary for the terminal operation to execute
        List<String> aList = Arrays.asList("abc1","abc2","abc3");
        count = 0;
        Stream<String> lazyInvocStream = aList.stream().filter(elem -> {
           wasCalled();
           return elem.contains("2");
        });
        // count will still be 0 because intermediate operation will not be invoked unless there is terminal operation.
        // System.out.println("count is: " + count);

        // stream pipeline executes vertically
        Optional<String> firstEle = aList.stream().filter(element -> {
            LOGGER.info("filter() is called for '" + element + "'");
            return element.contains("2");
        }).map(element -> {
            LOGGER.info("map() is called for '" + element + "'");
            return element.toUpperCase();
        }).findFirst();

        //System.out.println(firstEle.orElse("NOT PRESENT"));

        // ORDER OF EXECUTION

        // From the performance point of view, the right order is one of the most important aspects
        // of chaining operation in the stream pipeline

        // wasCalled() method will be called 3 times
        long size2 = aList.stream().map(element -> {
            wasCalled();
            return element.substring(0,2);
        }).skip(2).count();

        // wasCalled method will be called 1 time only
        long size3 = aList.stream().skip(2)
                .map(element -> {
                    return element.substring(0,3);
                }).count();

        // INTERMEDIATE operations which reduce the size of the stream should be placed before operations which
        // are applying to each element. So we need to keep method such as skip(), filter() and distinct()
        // at the top of our stream pipeline.

        // STREAM REDUCTION

        // reduce() method
        OptionalInt reduced = IntStream.range(1, 4).reduce((a,b) -> a + b);
        //System.out.println(reduced.orElse(0));
        int reducedTwoParams = IntStream.range(1, 4).reduce(10, (a,b) -> a + b);
        //System.out.println(reducedTwoParams);

        // COMBINER will NOT be called as this is not a parallel operation
        int reducedParams = Stream.of(1,2,3)
                .reduce(10, (a,b) -> a + b, (a,b) -> {
                    LOGGER.info("combiner was called");
                    return a + b;
                });
        //System.out.println(reducedParams);

        // COMBINER will be called as this is a parallel stream
        int reducedParallel = Arrays.asList(1,2,3).parallelStream()
                .reduce(10, (a, b) -> a + b, (a, b) -> {
                    LOGGER.info("COMBINER was called with a=" + a + ", b=" + b);
                    return a + b;
                });
        //System.out.println(reducedParallel);;

        // collect() method
        List<Product> productList = Arrays.asList(new Product(23, "potatoes"),
                new Product(14,"orange"), new Product(13, "lemon"),
                new Product(23, "bread"), new Product(13, "sugar"));
        // converting a stream to collection
        List<String> listOfProductNames = productList.stream().map(Product::getName).collect(Collectors.toList());
        //listOfProductNames.forEach(name -> System.out.println(name));
        // reducing/converting to concatenated string of product names
        String prodNamesString = productList.stream().map(Product::getName)
                                    .collect(Collectors.joining(", ", "[", "]"));
        //System.out.println(prodNamesString);

        // processing the average value of all product prices
        double averagePrice = productList.stream().collect(Collectors.averagingInt(Product::getPrice));
        //System.out.println(averagePrice);

        // processing the sum of all the product prices
        int allProductPrice = productList.stream().collect(Collectors.summingInt(Product::getPrice));
        //System.out.println(allProductPrice);

        // collecting statistical information about stream's elements
        IntSummaryStatistics statistics = productList.stream().collect(Collectors.summarizingInt(Product::getPrice));
        //System.out.println(statistics);

        // grouping products by price
        Map<Integer, List<Product>> productByPrice = productList.stream()
                                                        .collect(Collectors.groupingBy(Product::getPrice));
//        for(Integer priceKey: productByPrice.keySet()){
//            System.out.println("Key: " + priceKey);
//            List<Product> products = productByPrice.get(priceKey);
//            products.forEach(System.out::println);
//        }

        // dividing product list elements according to some predicate
        Map<Boolean, List<Product>> mapPartitioned = productList.stream()
                .collect(Collectors.partitioningBy(product -> product.getPrice() > 15));

/*        for(Boolean key: mapPartitioned.keySet()){
            System.out.println("Key: " + key);
            List<Product> products = mapPartitioned.get(key);
            products.forEach(System.out::println);
        }*/

        // pushing the collector to perform additional transformation
        Set<Product> unmodifiableProductSet = productList.stream()
                .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));

        //unmodifiableProductSet.stream().forEach(System.out::println);
        //unmodifiableProductSet.add(new Product(10,"this should give error"));

        // custom collector
        Collector<Product, ?, LinkedList<Product>> toLinkedList =
                Collector.of(LinkedList::new, LinkedList::add,
                        (first,second) -> {
                            first.addAll(second);
                            return first;
                        });

        LinkedList<Product> linkedListOfProducts = productList.stream().collect(toLinkedList);
        //linkedListOfProducts.stream().forEach(System.out::println);

        // Parallel Streams
        Stream<Product> streamOfCollection = productList.parallelStream();
        boolean isParallel = streamOfCollection.isParallel();
        //System.out.println("isParallel ? " + isParallel);
        boolean bigPrice = streamOfCollection.map(product -> product.getPrice() * 12)
                                .anyMatch(price -> price > 200);
        //System.out.println("bigPrice ? " + bigPrice);

        // If the source stream is other than a Collection or an array, then parallel() method should be used
        IntStream intStreamParallel = IntStream.range(1,150).parallel();
        boolean parallelHaiKya = intStreamParallel.isParallel();
        //System.out.println("isParallel ? " + parallelHaiKya);
        // Under the hood Stream API automatically uses the ForkJoin framework to execute operations in parallel.
        // When using streams api in parallel mode, avoid using blocking operations. It is also best to use parallel
        // mode when tasks need a similar amount of time to execute. If one tasks take much longer than the other,
        // it can slow down the complete app's workflow.

        // The stream in parallel mode can be converted to the sequential mode by using the sequential() method
        IntStream intStreamSequential = intStreamParallel.sequential();
        //System.out.println("isParallel ? " + intStreamSequential.isParallel());

        // CONCLUSION

        // The Stream API is a powerful, but simple to understand set of tools for processing the sequence of
        // elements. When used properly, it allows to reduce a huge amount of boilerplate code, create more readable
        // programs, and improve an app's productivity.

        // In real/production code, DO NOT leave an instantiated stream unconsumed i.e. without calling a terminal
        // operation or close() method, ELSE it will lead to memory leaks.
    }

    private static void wasCalled(){
        count++;
    }
    public Stream<String> streamOf(List<String> list){
        return list == null ? Stream.empty() : list.stream();
    }

    private static Stream<String> getStreamOfCollection(){
        Collection<String> collection = Arrays.asList("a","b","c");
        Stream<String> streamOfCollection = collection.stream();
        return streamOfCollection;
    }
}
