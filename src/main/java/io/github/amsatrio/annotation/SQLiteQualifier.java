package io.github.amsatrio.annotation;

import javax.inject.Qualifier;
import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD, TYPE, METHOD})
public @interface SQLiteQualifier {
    
}
