package bar.vvx.base.jda.scheduletasks;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScheduledTaskInfo {
    int delay();

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
