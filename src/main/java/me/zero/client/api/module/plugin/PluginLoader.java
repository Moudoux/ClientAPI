package me.zero.client.api.module.plugin;

import com.google.gson.GsonBuilder;
import me.zero.client.api.module.Module;
import me.zero.client.api.util.Messages;
import me.zero.client.api.util.logger.Level;
import me.zero.client.api.util.logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Used to Load plugins
 *
 * @author Brady
 * @since 1/26/2017 12:00 PM
 */
public final class PluginLoader {

    /**
     * The list of the Plugins discovered
     */
    private final List<Plugin> plugins = new ArrayList<>();

    /**
     * Directory containing possible plugins
     */
    private final String pluginDir;

    public PluginLoader(String pluginDir) {
        this.pluginDir = pluginDir;
        this.loadPlugins();
    }

    /**
     * Loads plugins
     */
    private void loadPlugins() {
        File dir = new File(this.pluginDir);
        File[] files = dir.listFiles();
        if (files == null)
            return;

        Arrays.stream(files).forEach(file -> {
            if (file.getAbsolutePath().endsWith(".jar")) {
                loadPlugin(file);
                Logger.instance.logf(Level.INFO, Messages.PLUGIN_LOAD, file.getAbsolutePath());
            }
        });
    }

    /**
     * Loads a single plugin from the file
     *
     * @param file The file of the plugin
     */
    private void loadPlugin(File file) {
        JarFile jarFile;

        try {
            jarFile = new JarFile(file);
        } catch (IOException e) {
            Logger.instance.logf(Level.WARNING, Messages.PLUGIN_JARFILE_CREATE, e);
            return;
        }

        JarEntry pJson = jarFile.getJarEntry("plugin.json");

        if (pJson == null)
            return;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(pJson)));
            PluginInfo info = new GsonBuilder().setPrettyPrinting().create().fromJson(reader, PluginInfo.class);

            if (info != null) {
                ClassLoader classLoader = new URLClassLoader(new URL[] { file.toURI().toURL() }, this.getClass().getClassLoader());

                Plugin plugin;

                try {
                    plugin = (Plugin) classLoader.loadClass(info.getMain()).newInstance();
                    plugin.setInfo(info);
                    plugins.add(plugin);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    Logger.instance.logf(Level.WARNING, Messages.PLUGIN_INSTANTIATION, e);
                    return;
                }

                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry e = entries.nextElement();
                    String name = e.getName();

                    if (name.endsWith(".class")) {
                        try {
                            Class clazz = Class.forName(name.substring(0, name.length() - 6).replace('/', '.'), true, classLoader);

                            if (clazz != null && clazz.getSuperclass().equals(Module.class)) {
                                try {
                                    plugin.loadModule((Module) clazz.newInstance());
                                } catch (InstantiationException | IllegalAccessException exception) {
                                    Logger.instance.logf(Level.WARNING, Messages.PLUGIN_CANT_CREATE_MODULE, exception);
                                }
                            }
                        } catch (ClassNotFoundException exception) {
                            Logger.instance.logf(Level.WARNING, Messages.PLUGIN_CANT_LOAD_CLASS, name);
                        }
                    }
                }
            }
        } catch (IOException e) {
            Logger.instance.logf(Level.WARNING, Messages.PLUGIN_CANT_CREATE_INPUTSTREAM, file.getAbsolutePath());
        }
    }

    /**
     * @return The list of plugins that were discovered
     */
    public final List<Plugin> getPlugins() {
        return this.plugins;
    }
}
