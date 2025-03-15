package UTCN_IMDB.demo.config;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumeratedMessage  {
    String message() default "Invalid genre";
    EnumType value() default EnumType.ORDINAL;
}
