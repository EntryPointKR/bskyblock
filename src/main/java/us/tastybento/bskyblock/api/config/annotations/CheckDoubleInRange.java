package us.tastybento.bskyblock.api.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the value of a field must be in a certain range (inclusive).
 * 
 * @author TheElectronWill
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CheckDoubleInRange {
    /** @return the minimum possible value, inclusive */
    double min() default Double.MIN_VALUE;

    /** @return the maximum possible value, inclusive */
    double max() default Double.MAX_VALUE;
}
