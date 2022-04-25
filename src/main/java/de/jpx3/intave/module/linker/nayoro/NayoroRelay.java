package de.jpx3.intave.module.linker.nayoro;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class generated using IntelliJ IDEA
 * Created by Richard Strunk 2022
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NayoroRelay {
  boolean testOnly() default false;
}
