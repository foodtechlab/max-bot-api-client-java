[![Maven](https://maven-badges.herokuapp.com/maven-central/chat.max/max-bot-api-client-java/badge.svg)](https://maven-badges.herokuapp.com/maven-central/chat.max/max-bot-api-client-java) ![Build](https://github.com/max-messenger/max-bot-api-client-java/workflows/Build/badge.svg?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/max-messenger/max-bot-api-client-java/badge.svg?branch=master)](https://coveralls.io/github/max-messenger/max-bot-api-client-java?branch=master) [![Javadocs](http://www.javadoc.io/badge/chat.max/max-bot-api-client-java.svg)](http://www.javadoc.io/doc/chat.max/max-bot-api-client-java)

# Max Bot API Java Client
This is Java client for Max Bot API. It gives you full access to API in your Java code.

Library has been built based on [Max Bot API Schema](https://github.com/max-messenger/max-bot-api-schema) that is OpenAPI-compliant.

Full documentation of API can be found [here](https://dev.max.ru).

⚠️ This library provides just a thin client to invoke API methods. Take a look at [max-bot-sdk-java](https://github.com/max-messenger/max-bot-sdk-java) framework to build bots.

## Requirements
Minimum required version of Java is 8.

To use Max Bot API you should obtain `ACCESS_TOKEN` for each bot you create.

Talk to [@MasterBot](http://max.ru/MasterBot). It will helps you to create your first bot.

## Dependencies
- [Jackson 2.9.8](https://github.com/FasterXML/jackson)
- [OkHttp 3.12.0](https://github.com/square/okhttp)
- [SLF4J](https://github.com/qos-ch/slf4j)

## Usage
To start using this client add it as Maven dependency:
```xml
<dependency>
    <groupId>ru.max</groupId>
    <artifactId>max-bot-api</artifactId>
    <version>0.0.7-SNAPSHOT</version>
</dependency>
```

### Initialization
The easiest way to create it is to call:
```java
MaxBotAPI api = MaxBotAPI.create("%ACCESS_TOKEN%);
```

It will create client with default Jackson-based serializer and OkHttp-based HTTP client.
If you want to use your own implementation you can implement `MaxTransportClient` and `MaxSerializer` and initialize `MaxClient` with it:

```java
MaxTransportClient transportClient = …;
MaxSerializer serializer = …;
MaxClient client = new MaxClient("%ACCESS_TOKEN%", transportClient, serializer);
MaxBotAPI botAPI = new MaxBotAPI(client);
```

### Making requests
`MaxBotAPI` provides access to all methods supported by the API. All methods return `MaxQuery` object that can be executed **synchronous** or **asynchronous**.

For example:

```java
String fileToken = …;
Long userId = …;

AttachmentRequest fileAttachment = new FileAttachmentRequest(new UploadedInfo(fileToken));
List<AttachmentRequest> attachments = Collections.singletonList(fileAttachment);
NewMessageBody body = new NewMessageBody("hello world!", attachments, null);
SendMessageQuery sendMessageQuery = botAPI.sendMessage(body).userId(userId);

// Sync
SendMessageResult result = sendMessageQuery.execute();

// Async
Future<SendMessageResult> futureResult = sendMessageQuery.enqueue();
```

### Uploading media
Your bot is able to attach some media content to messages. It could be image, video, audio or file.

To attach media to message you should follow two steps:

1. Obtain URL to upload:
```java
UploadEndpoint endpoint = botAPI.getUploadUrl(UploadType.VIDEO).execute();
String uploadUrl = endpoint.getUrl();
```
2. Upload binary data to this url. You can manually execute HTTP-request to send binary data to this url, or use built-in `MaxUploadAPI`:

```java
MaxUploadAPI uploadAPI = new MaxUploadAPI(client);
UploadedInfo uploadedInfo = uploadAPI.uploadFile(uploadUrl, new File("%FILE_PATH%")).execute();
```

#### Important notice
It may take a time for server to process you file (audio/video or binary file). While file is not processed you can't attach it and will get `AttachmentNotReadyException` when calling `sendMessage` method. Try again until you'll get successful result.

### Handling exceptions
All methods can throw two type of exceptions:

1. ClientException: general exception type wrapping all kinds of IO/serialization exceptions.

2. APIException: exception you will get if API was used incorrectly. For example, some arguments are missing or access to requested resource is denied.

### Logging
No logging is performed by default. This project uses SLF4J, so you should bring adapter for logging framework you prefer. For example, add slf4j-log4j12 as dependency:

```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.7.25</version>
</dependency>
```

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
This project is licensed under the [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0).
