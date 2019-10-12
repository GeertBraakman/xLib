# xLib `v0.3.1`

This is a library that I made for all my plugins, it will make your life a lot easier when writing Spigot plugins.

## Features

- Create commands on runtime.
- Easy message handler.
- Use config files easy.
- Easy UserInterfaces.

## How to Use

1. Clone this repository
2. Run the command `mvn clean install`
3. Add this dependency to your pom:

```XML
<dependency>
    <groupId>io.github.com.geertbraakman</groupId>
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
                <configuration>
                    <source>8</source>
                    <target>8</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>2.4.3</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

## Todo

- Add JavaDoc
- Add input methods
- Expand userinterfaces
- Add wiki
