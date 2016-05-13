package com.samistine.samistineutilities;

import com.samistine.samistineutilities.norainfall.NoRainFall;
import com.samistine.samistineutilities.nosandfall.NoSandFall;
import com.samistine.samistineutilities.physicsdisabler.JCPhysicsDisabler;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A collection of utilities that are meant to aid in the running of a server.
 * This is the manager that handles loading, reloading, and disabling the
 * individual utilities.
 *
 * <table style="border: 1px solid black;">
 * <caption>Features</caption>
 * <tr>
 * <th>Feature</th>
 * <th>Original Plugin/Source</th>
 * </tr>
 * <tr>
 * <td>{@link JCPhysicsDisabler}</td>
 * <td><a href="https://github.com/derjasper/PhysicsDisabler/">JCPhysicsDisabler</a></td>
 * </tr>
 * </table>
 *
 * @author Samuel Seidel
 */
public final class SamistineUtilities extends JavaPlugin {

    JCPhysicsDisabler jc;
    NoSandFall nsf;
    NoRainFall nrf;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.jc = new JCPhysicsDisabler().withPlugin(this).start();
        this.nsf = new NoSandFall().withPlugin(this).start();
        this.nrf = new NoRainFall().withPlugin(this).start();
    }

    @Override
    public void onDisable() {
        this.jc.onDisable();
        this.nsf.onDisable();
        this.nrf.onDisable();
    }

}
