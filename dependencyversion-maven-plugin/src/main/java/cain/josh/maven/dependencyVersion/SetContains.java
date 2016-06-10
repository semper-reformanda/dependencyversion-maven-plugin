package cain.josh.maven.dependencyVersion;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class SetContains <T> implements Function<Set<T>, Predicate<T>> {

    @Override
    public Predicate<T> apply(Set<T> ts) {
        final Set<T> resultSet = Optional.ofNullable(ts).orElse(Collections.emptySet());
        return resultSet.isEmpty() ? s -> false : resultSet::contains;
    }
}
