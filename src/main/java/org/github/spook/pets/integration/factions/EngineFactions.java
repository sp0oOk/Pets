package org.github.spook.pets.integration.factions;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.Engine;
import org.bukkit.entity.Player;

public class EngineFactions extends Engine {

  // -------------------------------------------- //
  // INSTANCE & CONSTRUCT
  // -------------------------------------------- //

  private static final EngineFactions i = new EngineFactions();

  public static EngineFactions get() {
    return i;
  }

  // -------------------------------------------- //
  // FIELDS
  // -------------------------------------------- //

  private Factions factions;

  // -------------------------------------------- //
  // OVERRIDE
  // -------------------------------------------- //

  @Override
  public void setActiveInner(boolean active) {
    this.factions = active ? Factions.get() : null;
  }

  public Factions getFactionsInstance() {
    return factions;
  }

  /**
   * Can players damage each other
   *
   * @param players The players
   * @return True if the players can damage each other
   */
  public boolean canDamage(Player... players) {
    if (players.length % 2 != 0) {
      throw new IllegalArgumentException("You must provide an even number of players");
    }

    for (int i = 0; i < players.length; i += 2) {
      if (!canDamage(players[i], players[i + 1])) {
        return false;
      }
    }
    return true;
  }

  /**
   * Can a player damage another player
   *
   * @param player The player
   * @param playerSecond The second player
   * @return True if the player can damage the second player
   */
  private boolean canDamage(Player player, Player playerSecond) {
    final MPlayer mplayer = MPlayer.get(player);
    final MPlayer mplayerSecond = MPlayer.get(playerSecond);

    return mplayer.getFaction().getRelationTo(mplayerSecond) == Rel.ENEMY;
  }
}
