package org.github.spook.pets.enums;

import com.massivecraft.massivecore.util.Txt;
import org.bukkit.entity.Player;

public enum Lang {
  PLAYERS_NOT_FOUND("<rose>There are no valid players nearby."),
  INVALID_CONFIG("<rose>The pet %s is not configured correctly. Please contact an administrator."),
  COOLDOWN("<rose>You are still on cooldown for %s."),
  LEVEL_UP("<lime>Your %s&a has leveled up to level %s!"),
  INVALID_POTION_EFFECT("<rose>The potion effect %s has failed to be applied."),
  DONE_X_TO_Y_PLAYERS("<lime>%s has been applied to %s players."),
  PVP_REGION_ONLY("<rose>You cannot use this pet in a non PvP region."),
  NEGATED_DAMAGE(
      "<lime>Your %s <lime>pet has negated %f of the <white>%s <lime>damage you just took!");
  private final String message;

  Lang(String message) {
    this.message = message;
  }

  public void send(Player player, Object... args) {
    if (player == null || !player.isOnline()) {
      return;
    }
    player.sendMessage(Txt.parse(String.format(message, args)));
  }
}
