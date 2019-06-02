# xLib `v0.1`

This is a library that I made for all my plugins, it will make your life a lot easier when writing Spigot plugins.

## Features

- Create commands on runtime.
- Store data easy.
- Use config files easy.
- Easy UserInterfaces.

## How to Use

1. Clone this repository
2. Run the command `mvn clean install`
3. Add this dependency to your pom:

```XML
<dependency>
    <groupId>io.github.GeertBraakman</groupId>
    <artifactId>xLib</artifactId>
    <version>${version}</version>
    <scope>compile</scope>
</dependency>
```
4. Add this build configuration to your pom:
```XML
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.0</version>
                <configuration>
                    <source>8</source>
                    <target>8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

## Todo

- Add JavaDoc
- Add database Sources
- Add unit-tests
- Add input methods
- Expand userinterfaces
- Add wiki
