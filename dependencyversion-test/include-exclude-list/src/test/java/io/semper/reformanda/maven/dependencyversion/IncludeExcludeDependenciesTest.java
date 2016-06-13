package io.semper.reformanda.maven.dependencyversion;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class IncludeExcludeDependenciesTest {

    static Properties properties;

    @BeforeClass
    public static void setUp_beforeClass() throws Exception {
        properties = new Properties();
        properties.load(new FileInputStream("target/test-classes/testPlugin.properties"));
    }

    @Test
    public void shouldIncludeIncluded() throws Exception {
        assertThat(properties.getProperty("undertow.version"), is(equalTo("1.3.15.Final")));
    }

    @Test
    public void shouldExcludeExcluded() throws Exception {
        assertThat(properties.getProperty("xnio.api.version"), is(not(equalTo("3.3.4.Final"))));
    }

    @Test
    public void shouldIncludeDependencyOnBothIncludesAndExcludes() throws Exception {
        assertThat(properties.getProperty("xnio.nio.version"), is(equalTo("3.3.4.Final")));
    }

    @Test
    public void shoudlExcludeDependencyNotOnExplicitIncludes() throws Exception {
        assertThat(properties.getProperty("commons.io"), is(not(equalTo("2.4"))));
    }
}
