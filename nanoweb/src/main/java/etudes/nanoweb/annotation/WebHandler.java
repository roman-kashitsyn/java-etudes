package etudes.nanoweb.annotation;

import etudes.nanoweb.HttpMethod;

import java.lang.annotation.*;

/**
 * Annotation for automatic router configuration.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebHandler {
    /**
     * HTTP method to handle. Default is GET.
     * @return HTTP method to handle.
     */
    HttpMethod method() default HttpMethod.GET;

    /**
     * Path to handle.
     * @return path to handle.
     */
    String path();
}
