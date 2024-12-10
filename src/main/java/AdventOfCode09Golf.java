import static java.util.stream.IntStream.range;

void main() {
  var input = "2333133121414131402";

  class Pump {
    int index;
    int end = input.length() - 1;
    int count;
    int current;

    int next() {
      while (count == 0) {
        if (index == end) {
          return -1;
        }
        count = input.charAt(end) - '0';
        current = end / 2;
        end -= 2;
      }
      count--;
      return current;
    }

    void decode(IntConsumer consumer) {
      int v;
      for(; index <= end; index += 2) {
        var quantity = input.charAt(index) - '0';  // read number
        range(0, quantity).forEach(_ -> consumer.accept(index / 2));
        if (index == end) {
          break;
        }
        var spaces = input.charAt(index + 1) - '0';  // read spaces
        for(var j = 0; j < spaces; j++) {
          v = next();
          if (v == -1) {
            return;
          }
          consumer.accept(v);
        }
      }
      if (count != 0) {
        range(0, count).forEach(_ -> consumer.accept(current));
      }
    }
  }

  var pump = new Pump();
  println(Stream.of("").mapMultiToInt((_, c) -> pump.decode(c)).boxed()
      .gather(Gatherer.ofSequential(() -> new Object() {int count;}, (c, v, d) -> d.push(c.count++ * v)))
      .mapToInt(v -> (int) v)
      .sum());
}
