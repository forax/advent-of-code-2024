void main() {
  var input = "2333133121414131402";
  println(Stream.of(IntStream.range(0, input.length())
          .mapToObj(i -> (i % 2 == 0 ? "" + (i / 2) : ".").repeat(input.charAt(i) - '0'))
          .<StringBuilder>collect(StringBuilder::new, StringBuilder::append, StringBuilder::append))
      .map(b -> IntStream.iterate(b.indexOf("."), i -> i != -1, i -> b.indexOf(".", i)).boxed()
          .reduce(b, (_, i) -> {
            b.setCharAt(i, b.charAt(b.length() - 1));
            b.setLength(b.length() - 1);
            return b;
          }, (_,_) -> null))
      .flatMap(b -> b.toString().chars().mapToObj(c -> c - '0'))
      .<Integer>gather(Gatherer.ofSequential(() -> new Object() {int count;}, (c, v, d) -> d.push(v * c.count++)))
      .reduce(0, Integer::sum));
}