int[] parseArray(String line) {
  var tokens = line.split(" ");
  return Arrays.stream(tokens).mapToInt(Integer::parseInt).toArray();
}

boolean isDistanceValid(int left, int right) {
  var distance = Math.abs(left - right);
  return distance >= 1 && distance <= 3;
}

enum Order { ASCENDING, DESCENDING }

Order order(int left, int right) {
  return left < right ? Order.ASCENDING : Order.DESCENDING;
}

boolean isArrayValid(int[] values) {
  var order = (Order) null;
  var left = values[0];
  for(var i = 1; i < values.length; i++) {
    var right = values[i];
    if (!isDistanceValid(left, right)) {
      return false;
    }
    var o = order(left, right);
    if (order != null && o != order) {
      return false;
    }
    order = o;
    left = right;
  }
  return true;
}

void main() {
  var text = """
      7 6 4 2 1
      1 2 7 8 9
      9 7 6 2 1
      1 3 2 4 5
      8 6 4 4 1
      1 3 6 7 9
      """;

  println(text.lines().filter(l -> isArrayValid(parseArray(l))).count());
}