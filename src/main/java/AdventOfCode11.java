void main() {
  var input = "125 17";

  var map = Arrays.stream(input.split(" ")).mapToInt(Integer::parseInt).boxed()
      .collect(Collectors.groupingBy(i -> i, Collectors.counting()));
  for(var i = 0; i < 25; i++) {
    var newMap = new HashMap<Integer, Long>();
    map.forEach((s, count) -> {
      if (s == 0) {
        newMap.merge(1, count, Math::addExact);
        return;
      }
      var length = 1 + (int) Math.log10(s);
      if (length % 2 == 0) {
        var divisor = (int) Math.pow(10, length / 2);
        newMap.merge(s / divisor, count, Math::addExact);
        newMap.merge(s % divisor, count, Math::addExact);
        return;
      }
      newMap.merge(Math.multiplyExact(2024, s), count, Math::addExact);
    });
    map = newMap;
  }
  println(map.values().stream().mapToLong(v -> v).sum());
}
