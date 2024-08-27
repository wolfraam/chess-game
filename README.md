chess-game - A Chess Java Library
===

chess-game is a java chess library which can
- generate the legal chess moves of a position 
- determine check / mate / draw
- determine the FEN notation of a position 
- generate the moves of a game in SAN, FAN, LAN and UCI notation in different languages
- parse moves in SAN, FAN, LAN and UCI notation in different languages
- determine the chess opening of a game
- import and export games in PGN notation

## Features
- Supports Android 5+

## Code Quality
![LINE](https://img.shields.io/badge/line--coverage-99.86%25-brightgreen.svg)
![BRANCH](https://img.shields.io/badge/branch--coverage-95.04%25-brightgreen.svg)
![COMPLEXITY](https://img.shields.io/badge/complexity-2.97-brightgreen.svg)

## Gradle

```
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.wolfraam:chessgame:1.5")
}
```

## Maven

```
<dependency>
  <groupId>io.github.wolfraam</groupId>
  <artifactId>chessgame</artifactId>
  <version>1.5</version>
</dependency>
```

## Playing moves

```java
import io.github.wolfraam.chessgame.ChessGame;

ChessGame chessGame = new ChessGame();
Move move = new Move(Square.E2, Square.E4);

// Check whether move is allowed:
if(chessGame.isLegalMove(move)) {
  // Execute the move on the board:
  chessGame.playMove(move);
}

// Or play a move by supplying the move notation:
chessGame.playMove(NotationType.SAN, "c5");

// Play several moves, seperate the moves by spaces or commas:
chessGame.playMoves(NotationType.SAN, "Nf3 d6");
```

## Get board info

```java
// print ASCII representation of the board
System.out.println(chessGame.getASCII());
// output:
// rnbqkbnr
// pppp_ppp
// ________
// ____p___
// ____P___
// __N_____
// PPPP_PPP
// R_BQKBNR

// Get FEN diagram
System.out.println(chessGame.getFen());
// output:
// rnbqkbnr/pppp1ppp/8/4p3/4P3/2N5/PPPP1PPP/R1BQKBNR b KQkq - 1 2
    
// Get the piece on A1 
System.out.println(chessGame.getPiece(Square.A1));
// ouput: WHITE_ROOK
```

## Get game result
```java
ChessGame chessGame = new ChessGame();
chessGame.playMoves(NotationType.SAN, "e4 f6 Nc3 g5");
System.out.println(chessGame.getGameResultType()); // output: null
chessGame.playMove(NotationType.SAN, "Qh5");
System.out.println(chessGame.getGameResultType()); // output: WHITE_WINS
```

## Move notation
chess-game supports 4 move notation types:
- Short algebraic notation (SAN). For example: Nc3
- Long algebraic notation (LAN). For example: Nb1-c3
- Figurine algebraic notation (FAN). For example: ♞c3
- The notation used in the UCI (Universal Chess Interface) protocol, which is
  used to communicate with chess engines. For example: b1c3

```java
// Find all legal moves and get SAN notation of them:
System.out.println(chessGame.getLegalMoves().stream()
    .map(move -> chessGame.getNotation(NotationType.SAN, move))
    .sorted().collect(Collectors.joining(",")));

// output:
// Ba3,Bb4,Bc5,Bd6,Be7,Ke7,Na6,Nc6,Ne7,Nf6,Nh6,Qe7,Qf6,Qg5,Qh4,a5,a6,b5,b6,c5,c6,d5,d6,f5,f6,g5,g6,h5,h6
```

## Move notation in other languages

The SAN en LAN notations support different languages. For example in French (language code: "fr") the notation for the 
knight (which is "N" in english) is "C":

```java
ChessGame chessGame = new ChessGame();
chessGame.playMoves(NotationType.SAN, "Nc3 e5");
System.out.println(chessGame.getNotationList(NotationType.LAN, "fr"));
// output:
// [Cb1-c3, e7-e5]
```

## Chess Opening
Determining the opening name of a game:
```java
ChessGame chessGame = new ChessGame();
chessGame.playMoves(NotationType.SAN, "e4 c5 Nf3 d6 d4 cxd4 Nxd4 Nf6 Nc3 a6");
ChessOpening chessOpening = chessGame.getChessOpening();
System.out.println(chessOpening.eco); // output: B90
System.out.println(chessOpening.name); // output: Sicilian
System.out.println(chessOpening.variation); // output: Najdorf
System.out.println(chessOpening.getFullName()); // output: Sicilian / Najdorf (B90)
```

## Export PGN games
To export games in Portable Game Notation (PGN):

```java
import io.github.wolfraam.chessgame.pgn.PGNExporter;

ChessGame chessGame = new ChessGame();
chessGame.playMoves(NotationType.SAN, "e4 c5 Nf3");
chessGame.getPGNData().setPGNTag(PGNTag.EVENT, "Test Event");
chessGame.getPGNData().setPGNTag(PGNTag.RESULT, "1/2-1/2");
PGNExporter pgnExporter = new PGNExporter(System.out);
pgnExporter.write(chessGame);
// output:
// [Event "Test Event"]
// [Result "1/2-1/2"]
// 
// 1. e4 1... c5 2. Nf3 1/2-1/2

pgnExporter.write(chessGame2); // etc.
```

## Import PGN games
To import games in PGN:

```java
import io.github.wolfraam.chessgame.pgn.PGNImporter;

PGNImporter pgnImporter = new PGNImporter();
pgnImporter.setOnGame((game) -> {
  System.out.println("Imported a game with moves:" + game.getMoves());
});
pgnImporter.setOnError(System.out::println);
pgnImporter.setOnWarning(System.out::println);

pgnImporter.run(new File("/temp/games.pgn"));
```

