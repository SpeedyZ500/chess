# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Server Design Diagram
[![Sequence Diagram](Phase2ChessServerDesign.svg)](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5ks9K+KDvvorxLAC5wFrKaooOUCAHjytLlqOIELEs2IJoYLphm6ZIUgaSEjqMJpEuGFocjAABmPK2gagrCjAQGhqRbqdph5QGo6zqXjB5QgBAwy8CgVSgZ+sIfuhXGJgSOEsh6uqZLG-rTiGrrSpGFHRjASkOlJWHJpBJb7oe2a5pg-4gtBJRXFMQErF806zs2EyTN+V6WYU2Q9jA-ZODA3SlrZyGjPZNZBk584uTABymMuq7eH4gReCg6B7gevjMMe6SZJgXkXkU1DXtIACiu7FfUxXNC0D6qE+3SOY26Dtr+ZxAiWDVzuZhktexMAlEglGWBFAbhY1aAYXKWEySxckwCAqQoCADbDR16AkUyrEaeU3K8hFMA8uyzEbepVngjAACEirKnpnYWeUABqlADYQ8SJKZCB5t1bK9fB9gZSNdZjRNMEarJpIwJCwwQDQSkAzOQNHWaEaFJaMileVxXaUG8aTbd3XlMZGXvZ9bUeQV1mBW5hVk3lYB9gO-lJD00VmJw8XroEkK2ru0IwAA4qOrJZaeuXnswVnXnz5VVfYo71aNnU-myd0wKtaBdaT32TeU-WDStCtrTd2EzeD82LctY1wxF61I+R200TAe0HcYiNkUmPFOvKF1XSqmF46TD1PdRICvWgxOmBZWu8TAyCxALoyqLCwOe1NMhg+6MDkmA8dqEnrubSjFE7baOcaGp5ru+TZ05xUjRGwZAd7tCpfh5Hlc-lcfSywn4yVP03coAAktI4wAIy9gAzAALDATwnpkBoVp8Tw6AgoANovo67F8A8AHJb-8MCNFTxyV7T9N+UOXeC73FT96Ow9j5PM9z9l+rBfc1ZTKv6+byFy9TD3gfRcR8lxs08AlDc2AfBQGwNweAClDA5xSG-UWORxYe2vLUBozRMAtAHvLQGc4hxANGAcWmUcKYVClnefBctggG3VkrBuqZyhq0ZqWUhKAIKa1Ol7XWQ1LZq2TtxNOJsM5myWvrIhhty7I3ZPbXaY19pJBdnIti2tvZKl9rjSuKtHpQGeiHAgb01BmTbh7M6no9Q51hFwkR0kxHHXBtRTgtj7H53UoXRRJdRz0TCFwzxFc+GwRgD7I201nEZzQCgZI7jRz72IkE+RqMtI53rn+fGCCvQmLDmYj6GtUwaOpp3Aej9yjj2nifDsZ8xYX0ZvfUY5SYCVKnjFVmK4IEcwCJYFAyoIDJBgAAKQgAdZBAQf5LTQeYShWDKS0IIQwmRz5ehwOAL0qAcAIDwSgLMMp0hqktTuuwgKPQ1kbK2TsvZD8DmFKgpY-hQdBFzitgjP2xsol8QWlIoRjCbZu28TAYujtlHOxMOo9umjLraIya1VhMADFGNDuHO5NSHmhIAFajLQLYtW7414XO2dAa5TTpAONTnIvCVIOicm8MMaR8M5z-ILgozOFI7QdgYkpCKyTikpw4vabQESnFI3KK4jg8TSXMq8ay4FyCGLD15ZC6OOccYgxCeUOA3yGz7UojAc5lBLnQH2qyEIvxdDwLGX4oV7yPmitVjaA09ZbF0W0Pi9ZhqiW7P5ta+Q0qK6Aq0q6+QMBIA+tGIKfQ+qCWep2bClWIyeSty+sqimzNDmdnPj5BmpzoowFiuAtciUAheHWV2L0sBgDYDgS9XJKCRYUIlmmCoJUyoVSqi7ZhmTG7ptRT1TRIBuB4Dzu8yJ9rB0VuDcAPOELAWtoxqrZUHKoAOghRqmAOZwzCspXNIdUBVXaBnenO2aM22Y2AEug98glXrs3W6bd6c+J7p0oe-1KSKLzoqouhAWM4xCrXei8od6WSmFtd2+FiAK3Jt4U2myGbanoPqbmsBK4gA)

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
