package cain.josh.maven.dependencyversion;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class FilterAllDependenciesTest {

    // TODO test classifier
    // TODO test suffix
    // TODO test filtering - I.E. only specifying certain dependencies

    static Properties properties;

    @BeforeClass
    public static void setUp_beforeClass() throws Exception {
        properties = new Properties();
        properties.load(new FileInputStream("target/test-classes/testPlugin.properties"));
    }

    @Test
    public void shouldFilterRegularDependency() throws Exception {
        assertThat(properties.getProperty("undertow.version"), is(equalTo("1.3.15.Final")));
    }

    @Test
    public void shouldFilterTransitiveDependency() throws Exception {
        assertThat(properties.getProperty("xnio.version"), is(equalTo("3.3.4.Final")));
    }
}
