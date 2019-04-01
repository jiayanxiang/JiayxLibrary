package com.jyx.mylibrary.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Activity fragment inject.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ActivityFragmentInject {
    /**
     * Content view id int.
     *
     * @return the int
     */
    int contentViewId() default -1;

    /**
     * Menu id int.
     *
     * @return the int
     */
    int menuId() default -1;

    /**
     * Toolbar title int.
     *
     * @return the int
     */
    int toolbarTitle() default -1;

    /**
     * Navigation id int.
     *
     * @return the int
     */
    int navigationId() default -1;
}
