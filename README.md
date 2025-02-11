# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Server Design Diagram
[![Sequence Diagram](Phase2ChessServerDesign.svg)](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5ks9K+KDvvorxLAC5wFrKaooOUCAHjytLlqOIELEs2IJoYLphm6ZIUgaSEjqMJpEuGFocjAABmPK2gagrCjAQGhqRbqdph5QGo6zrQU68owCAEDDLwKBVKBn6wh+6FcYmBI4SyHq6pksb+tOIautKkYUdGMDKQ6xiduppIwIRtwoTA06zugJFMqxSaXjB5QmXcswWY2VmmJhnb-mm+6HtmuaYN5bFXmmPRTEBKxfK5c6RTA34hZ22Q9jA-ZODA3SluFyGjJFNZBpZ84TJMBymMuq7eH4gReCg6B7gevjMMe6SZJgSUXkU1DXtIACiu7dfU3XNC0D6qE+3TReg7a-mcQIlhNaCBZBHY8eCMDwfYDUBvlbloBhcpYbJLHyTAkLDBANDKVtdY7dZZoRoUloyCg3ByDpQbxvtXlLT59W+v5CB5t9wWdaFcVTYl55gH2A7pUkYVLpw5XroEkK2ru0IwAA4qOrJNaerWQ2yP7lBUmP9UN9ijuN21zuDybfeU82LbN03sWt0LY6MqhXTON3SQdMhyUZ5JgJzag8wVt1kZp5TcryYusgxTP6XZgtHUZPQVD0lOjAAktIswnpkBrOUKCCgA2JtmTrKAAHKjnsjQHIZEYrXxmva6O+uG81+rZcBZsW1bb4wDb9ujI78UwV9LPlGL6OxMzqbAyUVx9DbqjjJU-Q2-r4wAIy9gAzAALDATxG37RH3NWUw6ObICW-7uxfGHo4t3sMBO3TpxtVDKUw0O6c41nWvD3r0gF8XZcV77wc15OfT10HzefE8bc5f8XcIyungVRu2A+FA2DcPAimGGLKS+wTOTMDx161A0FNU8ENPoEOG8oFHxz07H5lvwtIKbtYLs1iArCWfNPLYXVu6UOFJwHzSlrZB6FE5a2gVkKMIysdAGSFrAj2ucDYwErvPWYy9G6kNDqOcOX9u4uxTqtAhXsiEkP9mQhuTdq6zE-pHfmMdUxx1HAnLAP42TeSuDwpOUFf4CJgAAQgzi4T+LgBIYC2Ggbmn9sRAPsrxEBno9Ri1hFovh0CbLHTQCgZIRiTH0JljpG0WNRzMXMeaVWbNCGmJkcCcocBFJPn+oDFmKcALjxQHncohdS7f2Wr3SG0M0pD0IVnKJJcSpmERnvZGARLDPXgskGAAApCAPInGjECOQhsN9zBExCiTKolI7wtBttTa6c4hwn2ALkqAcAIDwSgNw5hMTpriP-m0yaOiOp6LghzHGEC5x7WjmYu6eFRZzMQS4u65FZY0TKWoTBYzea0xVss8MVwtaEJ9qeShlTKE8LoXghh7sLnMKucbNhgcKEfPud-bif4GZ7OEXTf5f8eidO6b0-pgyJ4QWCarUZGcKiEMaC4G4ElzYQr6dATEMAAC8uKYCKmVFI5aujVoACsSloCMeCygkLoDQrUEi5hjRFl6I1Hg1ZBENl2JQasu0HYGLKQKpoHBqsXYcQ6Js6WwDJW0Cgac3C5kbQGnrDSjFdKsUDL2bMA0SCNJ8ocbRDoMBIB7PMqyWlPStXStsrKmAOZwxeJBbI4pPJAkkpBPfUGwyIa3wSbDTKndSqZLXJVAIXguldi9LAYA2AT6EHiIkK++M+61JBiTHqfUBpDWMMCmasj4aevTdM-iL04RsudIdVxsCQDlukAAIVhPqtxhqs39W6uZYldj7WOrdM6gtPjo14A9UA71VwwaiNVn3ANGUi2AgySuIAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
