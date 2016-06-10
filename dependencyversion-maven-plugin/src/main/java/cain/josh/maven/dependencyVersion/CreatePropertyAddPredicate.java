package cain.josh.maven.dependencyVersion;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Evaluates artifact string identifiers to determine whether or not they should be included in the properties
 * render according to include/exclude rules.  The truth table below specifies behavior, where '1' is a delcared
 * string match, and '0' is a non-match for the declared set.
 *
 *  I | E | Result
 *  ---------------
 *  1 | 1 | included
 *  1 | 0 | included
 *  0 | 1 | excluded
 *  0 | 0 | included
 */
public class CreatePropertyAddPredicate implements Function<PropertySet, Predicate<String>> {

    @Override
    public Predicate<String> apply(final PropertySet propertySet) {
        final Predicate<String> onIncludesList = new SetContains<String>().apply(propertySet.getIncludes());
        final Predicate<String> onExcludesList = new SetContains<String>().apply(propertySet.getExcludes());
        return onIncludesList.or(onExcludesList.negate());
    }
}
