package bar.vvx.base.jda.scheduletasks;

import org.apache.commons.lang3.Validate;

import java.util.concurrent.TimeUnit;

public abstract class ScheduledTask {
    protected int delay;
    protected TimeUnit timeUnit;

    public ScheduledTask() {
        ScheduledTaskInfo scheduledTaskInfo = this.getClass().getAnnotation(ScheduledTaskInfo.class);
        Validate.notNull(scheduledTaskInfo, "ScheduleTaskInfo annotation not found on class " + this.getClass().getName());
        this.delay = scheduledTaskInfo.delay();
        this.timeUnit = scheduledTaskInfo.timeUnit();
    }

    protected abstract void init() throws Exception;

    protected abstract Runnable start();
}
