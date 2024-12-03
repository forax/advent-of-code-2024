void main() {
  var text = """
      7 6 4 2 1
      1 2 7 8 9
      9 7 6 2 1
      1 3 2 4 5
      8 6 4 4 1
      1 3 6 7 9
      """;

  enum Order { ASCENDING, DESCENDING, INVALID }
  println(text.lines().flatMap(l -> Arrays.stream(l.split(" ")).map(Integer::parseInt)
      .gather(Gatherers.windowSliding(2))
      .map(pair -> switch (pair.getFirst() - pair.getLast()) {
        case -3, -2, -1  -> Order.ASCENDING;
        case 1, 2, 3 -> Order.DESCENDING;
        default -> Order.INVALID;
      })
      .gather(Gatherer.ofSequential(() -> new Object() { Order o; }, (s,  e, _) ->  {
        if (e != Order.INVALID && s.o == null || s.o == e) { s.o = e; return true; }
        s.o = Order.INVALID; return false;
      }, (s, d) -> { if (s.o != Order.INVALID) { d.push(l); }})))
      .count());
}