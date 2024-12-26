record Safeguard(boolean lock, int[] pins) {
  @Override
  public String toString() {
    return (lock ? "lock " : "key ") + Arrays.toString(pins);
  }
}

<T> Gatherer<T, ?, List<T>> windowWhile(Predicate<? super T> predicate) {
  return Gatherer.ofSequential(() -> new Object() { ArrayList<T> list = new ArrayList<>(); },
      (state, element, downstream) -> {
        if (predicate.test(element)) {
          var list = state.list;
          state.list = new ArrayList<>();
          return downstream.push(list);
        }
        state.list.add(element);
        return true;
      },
      (state, downstream) -> {
        if (!state.list.isEmpty()) {
          downstream.push(state.list);
        }
      });
}

Safeguard computeSafeguard(List<String> schematics) {
  var firstLine = schematics.getFirst();
  var c = firstLine.charAt(0);
  var pins = IntStream.range(0, firstLine.length())
      .map(j -> (int) IntStream.range(1, schematics.size()).takeWhile(i -> schematics.get(i).charAt(j) == c).count())
      .toArray();
  return new Safeguard(c == '#', pins);
}

boolean match(Safeguard lock, Safeguard key) {
  for(var i = 0; i < lock.pins.length; i++) {
    if (lock.pins[i] > key.pins[i]) {
      return false;
    }
  }
  return true;
}

long countMatch(List<Safeguard> locks, List<Safeguard> keys) {
  var sum = 0;
  for(var lock : locks) {
    for(var key : keys) {
      if (match(lock, key)) {
        sum++;
      }
    }
  }
  return sum;
}

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

  var safeguardMap = input.lines()
      .gather(windowWhile(String::isEmpty))
      .map(this::computeSafeguard)
      .collect(Collectors.partitioningBy(Safeguard::lock));
  println(countMatch(safeguardMap.get(true), safeguardMap.get(false)));
}
