package org.github.spook.pets.integration.worldguard;

import com.massivecraft.massivecore.Engine;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class EngineWorldGuard extends Engine {

  // -------------------------------------------- //
  // INSTANCE & CONSTRUCT
  // -------------------------------------------- //
  private static final EngineWorldGuard i = new EngineWorldGuard();

  public static EngineWorldGuard get() {
    return i;
  }

  // -------------------------------------------- //
  // FIELDS
  // -------------------------------------------- //
  private WorldGuardPlugin worldGuard;

  // -------------------------------------------- //
  // OVERRIDE
  // -------------------------------------------- //

  @Override
  public void setActiveInner(boolean active) {
    this.worldGuard = active ? WorldGuardPlugin.inst() : null;
  }

  /**
   * Check if a player can pvp at a location
   *
   * @param player The player
   * @return True if the player can pvp
   */
  public boolean isPvpEnabledAt(Player player) {
    return worldGuard
            .getRegionManager(player.getWorld())
            .getApplicableRegions(player.getLocation())
            .queryState(worldGuard.wrapPlayer(player), DefaultFlag.PVP)
        != StateFlag.State.DENY;
  }

  /**
   * Check if a player can build at a location
   *
   * @param player The player
   * @param location The location
   * @return True if the player can build
   */
  public boolean canBuildAt(Player player, Location location) {
    final LocalPlayer localPlayer = worldGuard.wrapPlayer(player);
    return worldGuard
        .getRegionManager(location.getWorld())
        .getApplicableRegions(location)
        .testState(localPlayer, DefaultFlag.BUILD);
  }
}
