void main() {
  var input = """
      #####
      .####
      .####
      .####
      .#.#.
      .#...
      .....
      
      #####
      ##.##
      .#.##
      ...##
      ...#.
      ...#.
      .....
      
      .....
      #....
      #....
      #...#
      #.#.#
      #.###
      #####
      
      .....
      .....
      #.#..
      ###..
      ###.#
      ###.#
      #####
      
      .....
      .....
      .....
      #....
      #.#..
      #.#.#
      #####
      """;
  var width = 5;
  var height = 6;

  var locks = new ArrayList<int[]>();
  println(Stream.of(input.lines().iterator())
      .<int[]>mapMulti((it, c) -> Stream.iterate(it, Iterator::hasNext, v -> v)
          .map(Iterator::next)
          .filter(Predicate.not(String::isEmpty))
          .forEach(line -> {
            var kind = line.charAt(0);
            var s = kind == '#' ? '.' : '#';
            (kind == '#' ? (Consumer<int[]>) locks::add : c).accept(IntStream.range(0, height).boxed()
                .gather(Gatherers.fold(() -> IntStream.range(0, width).map(_ -> height).toArray(),
                    (p, j) -> {
                      var l = it.next();
                      IntStream.range(0, l.length()).filter(i -> l.charAt(i) == s).forEach(i -> p[i] = Math.min(p[i], j));
                      return p;
                    })).findFirst().orElseThrow());
          })
      )
      .flatMap(key -> locks.stream().filter(lock -> IntStream.range(0, lock.length).allMatch(i -> lock[i] <= key[i])))
      .count());
}
