void main() {
  var input = """
      3   4
      4   3
      2   5
      1   3
      3   9
      3   3
      """;
  var s1 = input.lines()
      .mapToInt(l -> Integer.parseInt(l, 0, l.indexOf(' '), 10))
      .sorted().spliterator();
  var s2 = input.lines()
      .mapToInt(l -> Integer.parseInt(l, l.lastIndexOf(' ') + 1, l.length(), 10))
      .sorted().spliterator();
  println(StreamSupport.intStream(new Spliterator.OfInt() {
    int first;
    public boolean tryAdvance(IntConsumer action) {
      return s1.tryAdvance((int v) -> first = v) && s2.tryAdvance((int v2) -> action.accept(Math.abs(v2 - first)));
    }
    public OfInt trySplit() { return null; }
    public long estimateSize() { return 0;}
    public int characteristics() { return 0; }
  }, false).sum());
}