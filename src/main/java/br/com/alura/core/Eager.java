package br.com.alura.core;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Qualifier;

import lombok.RequiredArgsConstructor;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Retention(RUNTIME)
@Target(TYPE)
@Documented
@Inherited
public @interface Eager {

    int priority() default Priority.LOWEST;

    interface Priority {
        int LOWEST = Integer.MAX_VALUE;
        int HIGHEST = 1;
    }

    @RequiredArgsConstructor
    @SuppressWarnings("ClassExplicitlyAnnotation")
    final class Literal extends AnnotationLiteral<Eager> implements Eager {

        public static final Literal INSTANTE = new Literal(Priority.LOWEST);

        private final int priority;

        @Override
        public int priority() {
            return this.priority;
        }
    }
}