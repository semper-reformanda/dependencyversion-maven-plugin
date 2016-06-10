package cain.josh.maven.dependencyVersion;

import edu.emory.mathcs.backport.java.util.Collections;

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
