jaxb-proporder-plugin
===
Maven plugin to change the serialization order of fields in an `@XmlElement`-annotated class (changing the `propOrder` value).

Usage
---
Assuming you would like to change the `propOrder` in generated class `AClass` from `prop1, prop2` to `prop2, prop1`
```xml
<plugin>
	<groupId>org.codehaus.mojo</groupId>
	<artifactId>jaxb2-maven-plugin</artifactId>
	<version>2.2</version>
	<dependencies>
		<dependency>
			<groupId>org.abratuhi.jaxb</groupId>
			<artifactId>jaxb-proporder-plugin</artifactId>
			<version>1.0-SNAPSHOT</version>
		</dependency>
	</dependencies>
	<executions>
		<execution>
			<id>xjc</id>
			<goals>
				<goal>xjc</goal>
			</goals>
		</execution>
	</executions>
	<configuration>
		<sources>
			<source>src/main/xsd/schema.xsd</source>
		</sources>
		<arguments>
			<argument>-XpropOrder</argument>
			<argument>-XpropOrder:AClass:prop2,prop1</argument>
		</arguments>
	</configuration>
</plugin>

```