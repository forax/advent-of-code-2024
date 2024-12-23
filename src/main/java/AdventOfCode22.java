long mix(long secret, long value) {
  return value ^ secret;
}

long prune(long secret) {
  return secret % 16777216;
}

long next(long secret) {
  secret = prune(mix(secret, secret * 64));
  secret = prune(mix(secret, secret / 32));
  secret = prune(mix(secret, secret * 2048));
  return secret;
}

long next(long secret, int times) {
  for(var i = 0; i < times; i++) {
    secret = next(secret);
  }
  return secret;
}

void main() {
  var input = """
      1
      10
      100
      2024
      """;

  println(input.lines().mapToLong(Long::parseLong)
      .map(secret -> next(secret, 2000))
      .sum());
}
