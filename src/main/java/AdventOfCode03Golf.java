void main() {
  var input = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))";

  println(Pattern.compile("mul\\((\\d{1,3})\\,(\\d{1,3})\\)").matcher(input).results()
      .mapToInt(result -> Integer.parseInt(result.group(1)) * Integer.parseInt(result.group(2)))
      .sum());
}