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

import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Evaluates artifact string identifiers to determine whether or not they should be included in the properties
 * render according to include/exclude rules.
 */
public class CreatePropertyAddPredicate implements Function<PropertySet, Predicate<String>> {

    @Override
    public Predicate<String> apply(final PropertySet propertySet) {
        final Predicate<String> onIncludesList = new SetContains<String>().apply(propertySet.getIncludes());
        final Predicate<String> includesEmpty = s -> Optional.ofNullable(propertySet.getIncludes()).orElse(Collections.emptySet()).isEmpty();
        final Predicate<String> onExcludesList = new SetContains<String>().apply(propertySet.getExcludes());

        return onIncludesList.or(onExcludesList.negate().and(includesEmpty));
    }
}
