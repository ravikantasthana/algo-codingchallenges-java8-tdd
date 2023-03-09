package codingchallenges.duplicatesinarray;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DuplicatesInArray {

    public static void main(String[] args) {
        int[] arr = new int[] {1,2,3,4,5,5,6,7,8,8,9};
        IntStream.of(arr)
                .mapToObj(Integer::valueOf)
                .collect(Collectors.groupingBy(Integer::intValue))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() > 1)
                .forEach(e -> System.out.println(e.getKey()));
                //.forEach(e -> e.getValue().forEach(System.out::println));
    }
}
