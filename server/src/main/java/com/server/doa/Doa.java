package com.server.doa;

import java.util.Collection;
import java.util.Optional;

public interface Doa<T> {
    Optional<T> get();
    Collection<T> getAll();
    boolean save(T instance);

}
