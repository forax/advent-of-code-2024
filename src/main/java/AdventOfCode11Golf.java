void main() {
  var input = "125 17";

  println(Stream.iterate(Arrays.stream(input.split(" ")).mapToInt(Integer::parseInt).toArray(),
          values -> Arrays.stream(values)
              .flatMap(s -> {
                if (s == 0) {
                  return IntStream.of(1);
                }
                var length = 1 + (int) Math.log10(s);
                if (length % 2 == 0) {
                  var divisor = (int) Math.pow(10, length / 2);
                  return IntStream.of(s / divisor, s % divisor);
                }
                return IntStream.of(Math.multiplyExact(2024, s));
              })
              .toArray())
      .limit(26)
      .reduce((_, v) -> v).orElseThrow().length);
}
