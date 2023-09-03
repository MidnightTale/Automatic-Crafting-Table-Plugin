package de.philw.automaticcraftingtable.util;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class FoliaScheduler {

    private static Boolean IS_FOLIA = null;

    private static boolean tryFolia() {
        try {
            Bukkit.getAsyncScheduler();
            return true;
        } catch (Throwable ignored) {
        }
        return false;
    }

    public static Boolean isFolia() {
        if (IS_FOLIA == null) IS_FOLIA = tryFolia();
        return IS_FOLIA;
    }
    public static void runAsyncSchedulerDelay(Plugin plugin, Consumer<ScheduledTask> taskConsumer, long initialDelay) {
        if (isFolia()) {
            Bukkit.getAsyncScheduler().runDelayed(plugin, taskConsumer, initialDelay * 50, TimeUnit.MILLISECONDS);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
            }, initialDelay);
        }
    }
    public static void runAsyncSchedulerFixRate(Plugin plugin, Consumer<ScheduledTask> taskConsumer, long initialDelay, long initialPeriod) {
        if (isFolia()) {
            Bukkit.getAsyncScheduler().runAtFixedRate(plugin, taskConsumer, initialDelay * 50, initialPeriod * 50, TimeUnit.MILLISECONDS);
        } else {
            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            }, initialDelay, initialPeriod);
        }
    }
    public static void runTaskForEntity(Entity entity, Plugin plugin, Runnable entityTask, long initialDelayTicks) {
        if (isFolia()) {
            entity.getScheduler().runDelayed(plugin, task -> entityTask.run(), null, initialDelayTicks);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, entityTask, initialDelayTicks);
        }
    }
}