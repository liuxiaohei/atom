package org.atom.functions;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 用于lambda表达式屏蔽受检异常
 *
 * @author ld
 */
public class Try {

    /**
     * 屏蔽受检异常
     */
    public static <T, R> Function<T, R> of(UncheckedFunction<T, R> mapper) {
        Objects.requireNonNull(mapper);
        return t -> {
            try {
                return mapper.apply(t);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    /**
     * 屏蔽受检异常 并给出默认值默认值
     */
    public static <T, R> Function<T, R> of(UncheckedFunction<T, R> mapper, Supplier<R> defaultValue) {
        Objects.requireNonNull(mapper);
        return t -> {
            try {
                return mapper.apply(t);
            } catch (Exception ex) {
                return Optional.ofNullable(defaultValue).map(Supplier::get).orElse(null);
            }
        };
    }

    @FunctionalInterface
    public interface UncheckedFunction<T, R> {
        R apply(T t) throws Exception;
    }
}