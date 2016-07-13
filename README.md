
# Set Properties for Maven Dependency Versions
dependencyversion-maven-plugin makes it easy to set and use version properties for dependencies that are declared in a different context.

## Why would I need to use this?
 - When importing dependencies from a BOM - Since imported BOMs do not allow for property import, this plugin provides a way to access the version information from said boms.
 - When a parent pom declares a dependency, but not a version - Most commonly this can be solved by simply declaring a pom in the parent property.  However, there are instances in which the parent pom is not under the control of downstream consumers.
 - For transient dependency compatibility - for example, an upstream dependency may declare an API, and your pom declares an implementation with a version corresponding to the upstream API.  This plugin would provide an easy way to keep the two in sync.

## Usage Example
To use the dependencyversion-maven-plugin, just include the plugin in your build

    <build>
       .
       .
        <plugins>
            .
            .
            <plugin>
                <groupId>io.reformanda.semper</groupId>
                <artifactId>dependencyversion-maven-plugin</artifactId>
                <version>1.0.0</version>
                <executions>
                    <execution>
                        <id>set-all</id>
                        <goals>
                            <goal>set-version</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

This configuration will set a version property for _all_ project dependencies (both declared and transitive) in the following format:

> groupId:artifactId:type[:classifier]._version_

For eample, a dependency declared in an imported BOM like this:

	<dependency>
	    <groupId>io.undertow</groupId>
	    <artifactId>undertow-core</artifactId>
	    <version>1.3.15.Final</version>
	</dependency>

Would result in the following property:

> io.undertow:undertow-core:jar.version=1.3.15.Final

### Including and Excluding Dependencies
By default, all declared and transitive dependencies that are resolved against the declaring project will be included.  However, inclusion and exclusion of dependencies from property generation can be accomplished using the ``<configuration>`` element.  

To Include *only* specified dependencies:

                        <configuration>
                            <propertySets>
                                <propertySet>
                                    <includes>
                                        <include>io.undertow:undertow-core:jar</include>
                                    </includes>
                                </propertySet>
                            </propertySets>
                        </configuration>

Or to include all resolved dependencies *except* those on the excludes list:

                        <configuration>
                            <propertySets>
                                <propertySet>
                                    <excludes>
                                        <exclude>org.jboss.xnio:xnio-api:jar</exclude>
                                        <exclude>org.jboss.xnio:xnio-nio:jar</exclude>
                                    </excludes>
                                </propertySet>
                            </propertySets>
                        </configuration>

### Omitting Transitive Dependencies
The plugin will create properties for **all** resolved dependencies - transitives included.  To shrink the scope of property creation to only declared dependencies, simply omit set the ``<transitive>`` configuration element to ``false``.

                        <configuration>
                            <propertySets>
                                <propertySet>
                                    <transitive>false</transitive>
                                </propertySet>
                            </propertySets>
                        </configuration>

### Use a Custom Property Suffix
By default, **.version** is the suffix appended to all properties.  This suffix setting can be altered by configuring the ``<suffix>`` element:

                        <configuration>
                            <propertySets>
                                <propertySet>
                                    <suffix>fromBom</suffix>
                                </propertySet>
                            </propertySets>
                        </configuration>

The above setting would generate properties like this:

> io.undertow:undertow-core:jar.fromBom=1.3.15.Final

