# DiceNotation Library
A full fledged Dice Notation (a.k.a `d20 + 2`) Java library.

The library was based of [Kaiper](https://github.com/Avarel/Kaiper) and even do mathematical expressions like `2 + 8 / 5`.

Licensed under the [Apache 2.0 License](https://github.com/arudiscord/dice-notation/blob/master/LICENSE).

### Installation

![Latest Version](https://api.bintray.com/packages/arudiscord/maven/dice-notation/images/download.svg)

Using in Gradle:

```gradle
repositories {
  jcenter()
}

dependencies {
  compile 'pw.aru.libs:dice-notation:LATEST' // replace LATEST with the version above
}
```

Using in Maven:

```xml
<repositories>
  <repository>
    <id>central</id>
    <name>bintray</name>
    <url>http://jcenter.bintray.com</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>pw.aru.libs</groupId>
    <artifactId>dice-notation</artifactId>
    <version>LATEST</version> <!-- replace LATEST with the version above -->
  </dependency>
</dependencies>
```

### Usage

The easiest way to start is by using the `SimpleDice` class.

```java
SimpleDice dice = new SimpleDice();

int value = dice.resolve("d20 + 2").intValue();
```

You can extend the class and override behaviour to your liking.

Alternatively, you can use the classes `DiceLexer`, `DiceParser`, `DiceSolver`, `DicePreEvaluator` and `DiceEvaluatorBuilder`.

```java
int value = new DiceParser(new DiceLexer("d20 + 2"))
            .parse()
            .accept(new DiceSolver(this::roll).andFinally(builder.build()));
```

The `DiceParser` generates AST which prints the original notation back with `Expr#toString`.

```java
String rolledDice = new DiceParser(new DiceLexer("d20+2"))
            .parse()
            .accept(new DiceSolver(this::roll)).toString();
```

### Support

Support is given on [Aru's Discord Server](https://discord.gg/URPghxg)

[![Aru's Discord Server](https://discordapp.com/api/guilds/403934661627215882/embed.png?style=banner2)](https://discord.gg/URPghxg)
