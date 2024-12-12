import static java.util.Map.entry;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;

void main() {
  var input = "5910927 0 1 47 261223 94788 545 7771";

  println(Stream.iterate(Arrays.stream(input.split(" "))
              .mapToLong(Long::parseLong).boxed().collect(groupingBy(i -> i, counting())),
          m -> m.entrySet().stream()
              .flatMap(e -> {
                var s = e.getKey();
                var count = e.getValue();
                if (s == 0) {
                  return Stream.of(entry(1L, count));
                }
                var length = 1 + (int) Math.log10(s);
                if (length % 2 == 0) {
                  var divisor = (long) Math.pow(10, length / 2);
                  return Stream.of(entry(s / divisor, count), entry(s % divisor, count));
                }
                return Stream.of(entry(Math.multiplyExact(2024L, s), count));
              })
              .collect(groupingBy(Map.Entry::getKey, summingLong(Map.Entry::getValue))))
      .limit(76)
      .reduce((_, v) -> v).orElseThrow().values().stream().mapToLong(v -> v).sum());
}
