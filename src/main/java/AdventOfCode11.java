void main() {
  var input = "5910927 0 1 47 261223 94788 545 7771";

  var map = Arrays.stream(input.split(" ")).mapToLong(Long::parseLong).boxed()
      .collect(Collectors.groupingBy(i -> i, Collectors.counting()));
  for(var i = 0; i < 75; i++) {
    var newMap = new HashMap<Long, Long>();
    map.forEach((s, count) -> {
      if (s == 0) {
        newMap.merge(1L, count, Math::addExact);
        return;
      }
      var length = 1 + (int) Math.log10(s);
      if (length % 2 == 0) {
        var divisor = (long) Math.pow(10, length / 2);
        newMap.merge(s / divisor, count, Math::addExact);
        newMap.merge(s % divisor, count, Math::addExact);
        return;
      }
      newMap.merge(Math.multiplyExact(2024L, s), count, Math::addExact);
    });
    map = newMap;
  }
  println(map.values().stream().mapToLong(v -> v).sum());
}
