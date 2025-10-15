package su.nightexpress.nightcore;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.command.CommandManager;
import su.nightexpress.nightcore.command.api.NightPluginCommand;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.PluginDetails;
import su.nightexpress.nightcore.language.LangManager;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangElement;
import su.nightexpress.nightcore.util.wrapper.UniTask;

import java.util.function.Consumer;

public interface NightCorePlugin extends Plugin {

    //boolean isEngine();

    void enable();

    void disable();

    void reload();

    @Deprecated
    NightPluginCommand getBaseCommand();

    @Override
    @NotNull FileConfig getConfig();

    @Deprecated
    @NotNull FileConfig getLang();

    @NotNull PluginDetails getDetails();

    void extractResources(@NotNull String jarPath);

    void extractResources(@NotNull String jarParh, @NotNull String toPath);

    /**
     * Saves and loads {@link LangElement} objects from the provided {@link LangContainer} object into the lang config file according to selected
     * language during the "enable" plugin's phase if the same can not be achieved through {@link NightPlugin#registerLang(Class)}
     * <br>
     * <b>Note:</b> This can not be used outside of the {@link NightPlugin#enable()} phase.
     * @param langContainer LangContainer object with some LangElement fields defined.
     * @see NightPlugin#registerLang(Class)
     */
    void injectLang(@NotNull LangContainer langContainer);

    @NotNull
    default String getNameLocalized() {
        return this.getDetails().getName();
    }

    @NotNull
    default String getPrefix() {
        return this.getDetails().getPrefix();
    }

    @NotNull
    default String[] getCommandAliases() {
        return this.getDetails().getCommandAliases();
    }

    @NotNull
    @Deprecated
    default String getLanguage() {
        return this.getDetails().getLanguage();
    }

    default void info(@NotNull String msg) {
        this.getLogger().info(msg);
    }

    default void warn(@NotNull String msg) {
        this.getLogger().warning(msg);
    }

    default void error(@NotNull String msg) {
        this.getLogger().severe(msg);
    }

    default void debug(@NotNull String msg) {
        this.info("[DEBUG] " + msg);
    }

    @Deprecated
    @NotNull LangManager getLangManager();

    @Deprecated
    @NotNull CommandManager getCommandManager();

    @NotNull
    default BukkitScheduler getScheduler() {
        return this.getServer().getScheduler();
    }

    @NotNull
    default PluginManager getPluginManager() {
        return this.getServer().getPluginManager();
    }

    void runTask(@NotNull Runnable runnable);

    default void runTask(@NotNull Consumer<BukkitTask> consumer) {
        this.getScheduler().runTask(this, consumer);
    }

    default void runTaskAsync(@NotNull Consumer<BukkitTask> consumer) {
        this.getScheduler().runTaskAsynchronously(this, consumer);
    }

    default void runTaskLater(@NotNull Consumer<BukkitTask> consumer, long delay) {
        this.getScheduler().runTaskLater(this, consumer, delay);
    }

    default void runTaskLaterAsync(@NotNull Consumer<BukkitTask> consumer, long delay) {
        this.getScheduler().runTaskLaterAsynchronously(this, consumer, delay);
    }

    default void runTaskTimer(@NotNull Consumer<BukkitTask> consumer, long delay, long interval) {
        this.getScheduler().runTaskTimer(this, consumer, delay, interval);
    }

    default void runTaskTimerAsync(@NotNull Consumer<BukkitTask> consumer, long delay, long interval) {
        this.getScheduler().runTaskTimerAsynchronously(this, consumer, delay, interval);
    }

    @NotNull
    @Deprecated
    default UniTask createTask(@NotNull Runnable runnable) {
        return new UniTask(this, runnable);
    }

    @NotNull
    @Deprecated
    default UniTask createAsyncTask(@NotNull Runnable runnable) {
        return this.createTask(runnable).setAsync();
    }
}
