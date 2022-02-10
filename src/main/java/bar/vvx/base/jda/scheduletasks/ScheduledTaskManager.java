package bar.vvx.base.jda.scheduletasks;

import bar.vvx.base.jda.Main;
import bar.vvx.base.jda.scheduletasks.impl.minecraft.servers.ScheduledTask$ServerStatusChecker$Localhost;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledTaskManager {
    private final List<ScheduledTask> scheduledTasks;

    public ScheduledTaskManager() {
        this.scheduledTasks = new ArrayList<>();
        this.addScheduledTask();
    }

    private void addScheduledTask() {
        // SERVER STATUS
        {
            addScheduledTask(new ScheduledTask$ServerStatusChecker$Localhost());
        }
    }

    private void addScheduledTask(ScheduledTask scheduledTask) {
        this.scheduledTasks.add(scheduledTask);
    }

    public void ScheduledTaskInitialize() {
        while (Main.getJda() == null) {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        this.scheduledTasks.forEach((t) -> {
            try {
                t.init();
            } catch (Exception e) {
                e.printStackTrace();
            }
            scheduledExecutorService.scheduleAtFixedRate(t.start(), 0, t.delay, t.timeUnit);
        });
    }
}
