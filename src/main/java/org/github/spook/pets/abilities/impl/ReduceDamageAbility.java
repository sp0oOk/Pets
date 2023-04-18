package org.github.spook.pets.abilities.impl;

import org.github.spook.pets.Util;
import org.github.spook.pets.abilities.IAbility;
import org.github.spook.pets.enums.Lang;
import org.github.spook.pets.Pet;
import com.massivecraft.massivecore.Args;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

public class ReduceDamageAbility implements IAbility {

  @Override
  public boolean execute(Pet pet, Player player, Event event, Args args) {

    final EntityDamageEvent damageEvent;

    if (!(event instanceof EntityDamageEvent)
        || (damageEvent = (EntityDamageEvent) event).isCancelled()
        || !(damageEvent.getEntity() instanceof Player)) {
      return false;
    }

    final EntityDamageEvent.DamageCause causeNeeded = Util.parseDamageCause(args.get());

    if (causeNeeded == null) {
      Lang.INVALID_CONFIG.send(player, pet.getInternalName());
      return false;
    }

    if (damageEvent.getCause() != causeNeeded) {
      return false;
    }

    final double reduceDamagePercent = args.get();

    if (reduceDamagePercent < 0 || reduceDamagePercent > 100) {
      Lang.INVALID_CONFIG.send(player, pet.getInternalName());
      return false;
    }

    final double damage = damageEvent.getDamage();
    final double reduceDamage = damage * (reduceDamagePercent / 100);

    damageEvent.setDamage(damage - reduceDamage);

    Lang.NEGATED_DAMAGE.send(player, pet.getDisplayName(), reduceDamagePercent, causeNeeded.name());
    return true;
  }
}
