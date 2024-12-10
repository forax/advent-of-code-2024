String files(String input) {
  var builder = new StringBuilder();
  for (int i = 0; i < input.length(); i++) {
    var letter = i % 2 == 0 ? "" + (i / 2) : ".";
    var repeat = input.charAt(i) - '0';
    builder.append(letter.repeat(repeat));
  }
  return builder.toString();
}

String replaceDots(String text) {
  var builder = new StringBuilder(text);
  var lastIndex = 0;
  for(int index; (index = builder.indexOf(".", lastIndex)) != -1; lastIndex = index) {
    builder.setCharAt(index, builder.charAt(builder.length() - 1));
    builder.setLength(builder.length() - 1);
  }
  return builder.toString();
}

int checksum(String files) {
  return IntStream.range(0, files.length()).map(i -> i * (files.charAt(i) - '0')).sum();
}

void main() {
  var input = "2333133121414131402";
  println(checksum(replaceDots(files(input))));
}