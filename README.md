# euphoria-api
A euphoria.io bot library for java

# Usage

Run `./gradlew publishToMavenLocal` to add the library to your local maven repo,


and then add the latest version to your dependencies

`implementation "io.github.kidofcubes:euphoria-api:0.1.0"`

# Writing a bot

```java
EuphoriaBot bot = new EuphoriaBot("ExampleBotName"); // initiate bot
bot.joinRoom(new URI("wss://euphoria.io/room/testing/ws")); // connect to a websockets endpoint
```

Functionality is added by extending the `EuphoriaBot` class and overriding functions

```java
class ExampleBot extends EuphoriaBot{
    @Override
    public void onJoinRoom(RoomConnection connection){
        connection.sendEuphoriaMessage(new Message("Hello World"));
    }
}
```


