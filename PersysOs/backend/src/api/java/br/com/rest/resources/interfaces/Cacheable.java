package br.com.rest.resources.interfaces;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Cacheable {
  String cc() default "public, must-revalidate";
}
