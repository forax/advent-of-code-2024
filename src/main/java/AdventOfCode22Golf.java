record Expr(long[] bits) {
  public long eval(long value) {
    return IntStream.range(0, 64).mapToLong(i -> ((long) Long.bitCount(value & bits[i]) & 1) << i).reduce(0L, (a, b) -> a | b);
  }

  Expr lshift(int shift) {
    var nbits = new long[64];
    System.arraycopy(bits, 0, nbits, shift, 64 - shift);
    return new Expr(nbits);
  }

  Expr rshift(int shift) {
    var nbits = new long[64];
    System.arraycopy(bits, shift, nbits, 0, 64 - shift);
    return new Expr(nbits);
  }

  Expr mask(int size) {
    var nbits = new long[64];
    System.arraycopy(bits, 0, nbits, 0, size);
    return new Expr(nbits);
  }

  Expr xor(Expr expr) {
    return new Expr(IntStream.range(0, 64).mapToLong(i -> bits[i] ^ expr.bits[i]).toArray());
  }
}

Expr mix(Expr secret, Expr expr) {
  return expr.xor(secret);
}

Expr prune(Expr secret) {
  return secret.mask(24);
}

Expr next(Expr secret) {
  secret = prune(mix(secret, secret.lshift(6)));
  secret = prune(mix(secret, secret.rshift(5)));
  secret = prune(mix(secret, secret.lshift(11)));
  return secret;
}

Expr next(Expr secret, int times) {
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

  var expr = new Expr(IntStream.range(0, 64).mapToLong(i -> 1L << i).toArray());
  var next = next(expr, 2000);
  println(input.lines().mapToLong(Long::parseLong).map(next::eval).sum());
}
