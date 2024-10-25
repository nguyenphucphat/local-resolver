package com.ia03.annotation;

import java.lang.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("isAuthenticated()")
public @interface IsAuthenticated {}
