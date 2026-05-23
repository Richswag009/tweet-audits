package org.richcodes.analyser;

@FunctionalInterface
public interface ThrowingSupplier<T> {
    T get() throws Exception;
}
