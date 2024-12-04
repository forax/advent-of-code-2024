static final Pattern PATTERN = Pattern.compile("mul\\(([0-9]+)\\,([0-9]+)\\)");

record Pair(int left, int right) {}

List<Pair> parse(String text) {
  var pairs = new ArrayList<Pair>();
  var matcher = PATTERN.matcher(text);
  while(matcher.find()) {
    var left = Integer.parseInt(matcher.group(1));
    var right = Integer.parseInt(matcher.group(2));
    pairs.add(new Pair(left, right));
  }
  return pairs;
}

int multiplyAndSum(List<Pair> pairs) {
  return pairs.stream().mapToInt(p -> p.left * p.right).sum();
}

void main() {
  var input = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))";

  println(multiplyAndSum(parse(input)));
}