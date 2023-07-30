# Spring framework web custom implementation

Implementation contains basic Spring web features like: `MVC`, `Controller`, `DispatcherServlet`, `Filter`, different web annotations.

### Motivation

To gain deeper understanding how Spring Web works under the hood and which problems solves.

## How to use the framework implementation

- clone this project
- execute gradle task called `shadowJar`, running which jar file will be created and placed into `build/libs` folder with
  name `spring-web-1.0.0-all.jar`.
- create your own `gradle` project
- add the jar to `gradle` `dependencies`

```
    implementation files('C:\\space\\projects\\spring-web\\build\\libs\\spring-web-1.0.0-all.jar')
```

- Add on your main class annotation `@SpringWebApplication` - will help to resolve main class lication
- Add to `main` method

```java
    WebApplication.run("com.example",TomcatWebServer.class);
```

Application will start on port `8080`

You can override port in `application.properties` file via `server.port` property.

The rest workflow is identical to Spring Web (create controller via `@Contoller` and add endpoints..).

_Note: All responses are returned in JSON format as per that moment_