package io.semper.reformanda.maven.dependencyversion;

import io.semper.reformanda.maven.dependencyversion.CreatePropertyAddPredicate;
import io.semper.reformanda.maven.dependencyversion.PropertySet;
import org.junit.Test;

import java.util.Collections;
import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PropertyAddPredicateTest {

    @Test
    public void shouldIncludeWhenBothNull() {
        final Predicate<String> resultPredicate = new CreatePropertyAddPredicate().apply(new PropertySet());
        assertThat(resultPredicate.test("foo"), is(true));
    }

    @Test
    public void shouldIncludeWhenBothEmpty() {
        final PropertySet propertySet = new PropertySet();
        propertySet.setIncludes(Collections.emptySet());
        propertySet.setExcludes(Collections.emptySet());

        final Predicate<String> resultPredicate = new CreatePropertyAddPredicate().apply(propertySet);
        assertThat(resultPredicate.test("foo"), is(true));
    }

    @Test
    public void shouldIncludeWhenOnIncludeNotExclude() {
        final PropertySet propertySet = new PropertySet();
        propertySet.setIncludes(Collections.singleton("foo"));
        propertySet.setExcludes(Collections.emptySet());

        final Predicate<String> resultPredicate = new CreatePropertyAddPredicate().apply(propertySet);
        assertThat(resultPredicate.test("foo"), is(true));
    }

    @Test
    public void shouldIncludeWhenOnIncludeAndExclude() {
        final PropertySet propertySet = new PropertySet();
        propertySet.setIncludes(Collections.singleton("foo"));
        propertySet.setExcludes(Collections.singleton("foo"));

        final Predicate<String> resultPredicate = new CreatePropertyAddPredicate().apply(propertySet);
        assertThat(resultPredicate.test("foo"), is(true));
    }

    @Test
    public void shouldIncludeWhenOnExcludeNotInclude() {
        final PropertySet propertySet = new PropertySet();
        propertySet.setIncludes(Collections.emptySet());
        propertySet.setExcludes(Collections.singleton("foo"));

        final Predicate<String> resultPredicate = new CreatePropertyAddPredicate().apply(propertySet);
        assertThat(resultPredicate.test("foo"), is(false));
    }

    @Test
    public void shouldExcludeWhenOnNeitherExcludeNorInclude() {
        final PropertySet propertySet = new PropertySet();
        propertySet.setIncludes(Collections.singleton("foo"));
        propertySet.setExcludes(Collections.singleton("bar"));

        final Predicate<String> resultPredicate = new CreatePropertyAddPredicate().apply(propertySet);
        assertThat(resultPredicate.test("fubar"), is(false));
    }
}
