package com.samistine.samistineutilities;

import com.samistine.samistineutilities.physicsdisabler.JCPhysicsDisabler;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A collection of utilities that are meant to aid in the running of a server.
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

    @Override
    public void onEnable() {
        this.jc = new JCPhysicsDisabler().withPlugin(this).start();
    }

    @Override
    public void onDisable() {
        this.jc.onDisable();
    }

}
