/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.semper.reformanda.maven.dependencyversion;

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
