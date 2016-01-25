#Set Solver
This repo contains code written in Java 8 that solves the card game of Set, with configuration possible for:

* an arbitrary number of dimensions 
* an arbitrary number of values for each dimension
* an arbitrary number of cards that make up a set

##Compilation

Compiling this project requires maven and Java 8.  
To compile, navigate to  ../[repoLocation]/SetSolver/ where a pom.xml file is located,and run the command: <br>
mvn clean install

This will compile the project, run unit tests, and create a jar file at the location ../[repoLocation]/SetSolver/target/SetSolver-0.0.1-SNAPSHOT-jar-with-dependencies.jar

A precompiled jar has also been provided in ../[repoLocation]/SetSolver/jars/SetSolver-0.0.1-SNAPSHOT-jar-with-dependencies.jar

##Running The Solver

###SetSolverWithInputDeck
The class lwinikor.set.solver.SetSolverWithInputDeck contains a main method that can find all sets given: 

* the location of an input file that contains a JSON list of JSON objects representing Set cards
* the number of cards that make up a set

####Input
An example of the contents of an expected input file is:

```json
[
{"shading":"solid","number":"3","shape":"oval","color":"purple"},
{"shading":"outlined","number":"3","shape":"squiggle","color":"red"},
{"shading":"solid","number":"1","shape":"diamond","color":"red"},
{"shading":"striped","number":"2","shape":"diamond","color":"red"},
{"shading":"solid","number":"2","shape":"oval","color":"green"},
{"shading":"striped","number":"1","shape":"squiggle","color":"green"},
{"shading":"striped","number":"2","shape":"squiggle","color":"purple"},
{"shading":"striped","number":"3","shape":"oval","color":"red"}
]
```

In this case, the dimensions are color, number, shading, and shape, and for each card, those dimensions are mapped to various dimensionValues. 

The number of dimensions per card, the naming of the dimensions, and the possible dimension values can be set to any string values.

If the input file is found to be invalid, it will be reported by the program. Likely reasons are:

* the file does not exist
* the file does not contain valid json, and cannot be parsed
* not all cards specified in the file contain the same number of dimensions. (Eg. if one card is missing a dimension named "color", while others do specify this dimension, the entire input is considered invalid)
* dimension or dimensionValues are empty strings
* no dimensions or dimensionValues specified for a given card
* one or more individual cards contains multiple entries for the same dimension

All dimensions and dimensionValues are case sensitive, so "color" and "Color" would be considered different dimensions.

Duplicate cards are allowed in the input. The solver will consider the 2 duplicate cards as separate, and so the output may contain sets that look like they are duplicated, but are actually distinct sets using each of the duplicate cards separately.

#####Input Examples
Various input file examples are available at ../[repoLocation]/SetSolver/src/main/resources/inputDecks

####Output
The output of this program is a JSON list of JSON lists printed to stdout, with each inner list representing a set found in the input.  An example of the output is:

```json
[
[
{"shading":"solid","number":"3","shape":"oval","color":"purple"},
{"shading":"outlined","number":"3","shape":"squiggle","color":"red"},
{"shading":"striped","number":"3","shape":"diamond","color":"green"}
],
[
{"shading":"solid","number":"3","shape":"oval","color":"purple"},
{"shading":"solid","number":"1","shape":"diamond","color":"red"},
{"shading":"solid","number":"2","shape":"squiggle","color":"green"}
],
[
{"shading":"solid","number":"3","shape":"oval","color":"purple"},
{"shading":"striped","number":"2","shape":"diamond","color":"red"},
{"shading":"outlined","number":"1","shape":"squiggle","color":"green"}
]
]
```
Each inner list describes the cards that make up the set

####Command Line Options

Usage for the program is as follows: <br>
usage: SetSolverWithInputDeck <br>
 -d <arg>   Location of SetDeck input file. <br>
 -n <arg>   Number of cards that make up a Set. <br>
 -v         Print additional debug info. <br>

After compiling or locating the jar, you can run the command:

java -cp ./SetSolver-0.0.1-SNAPSHOT-jar-with-dependencies.jar lwinikor.set.solver.SetSolverWithInputDeck -d ./inputFile.json -n 3

If you specify -v, in addition to the found sets, a couple of lines listing the number of sets found and the time taken will be printed to stdout.

If you specify -n to an invalid value, it will default to 3.

###SetSolverWithConfig
The class lwinikor.set.solver.SetSolverWithConfig contains a main method that can find all sets given:

* the location of a configuration file that contains a JSON object which specifies the rules of the Set game 

This solver is not the main task of this project, however it can be used to more easily generate input to be run with SetSolverWithInputDeck

####Input
An example of the contents of an expected input file is:

```json
{
	"dimensionValues": {
		"color": ["red", "green", "purple"],
		"shape": ["diamond", "oval", "squiggle"],
		"shading": ["solid", "striped", "outlined"],
		"number": ["1", "2", "3"]
	},
	"numCardsPerSet" : 3,
	"numCardsToPlay" : 81
}
```
* dimensionValues specifies all of the possible dimensions mapped to the possible values per dimension
* numCardsPerSet which specifies how many cards make up a set
* numCardsToPlay the number of random cards to be taken from the generated Set Deck.

A configuration may be considered invalid if:

* dimensionValues are not specified
* numCardsPerSet < 3
* numCardsToPlay < numCardsPerSet

If numCardsToPlay is specified to a number larger than the number of possible cards in the deck as specified by dimensionValues, the program will default to using the number of cards in the deck.

#####Input Examples
Various input file examples are available at ../[repoLocation]/SetSolver/src/main/resources/inputDecks/setGameConfigs

####Output
This solver takes in the configuration file and will generate a full deck (all possible cards) based on the dimensionValues specified.  It will then draw numCardsToPlay random cards from that deck, and print those cards out to stdOut.  It will then find all sets of size numCardsPerSet, and print those sets out to stdOut.

####Command Line Options
Usage for the program is as follows:
usage: SetSolverWithConfig
 -c <arg>   Location of SetGameConfig configuration file.

After compiling or locating the jar, you can run the command:

java -cp ./SetSolver-0.0.1-SNAPSHOT-jar-with-dependencies.jar lwinikor.set.solver.SetSolverWithInputDeck -c ./inputConfigFile.json

###Unit Tests
Various JUnit tests that exercise validity and functionality are available in ../[repoLocation]/SetSolver/src/test/java/lwinikor/set

###Runtime
Given the potential variability of the inputs and rules of the game, the solution checks all combinations of size n (n being the configured number of cards that make up a set) from the input deck.
For the standard full Set deck, there are 81 cards, and 3 cards make up a set, so the number of combinations, 81 C 3 = 85320.
Increasing the number of cards that make up a set to 4, number of combinations greatly increases, 81 C 4 = 1663740
Increasing the number of cards that make up a set to 6, number of cominations increases even further, 81 C 6 = 324540216

The runtime is tied to the number of combinations.  Running with any configurations that could be found in a real game of Set should return instantly.  When dealing with configurations that greatly increase number of combinations, response may take some time to return. (Timed roughly .5 seconds per 1 million combinations on a macbook pro).  
