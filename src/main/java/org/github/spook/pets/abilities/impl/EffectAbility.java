package org.github.spook.pets.abilities.impl;

import org.github.spook.pets.Pets;
import org.github.spook.pets.Util;
import org.github.spook.pets.abilities.IAbility;
import org.github.spook.pets.enums.Lang;
import org.github.spook.pets.Pet;
import org.github.spook.pets.AbilityArgs;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EffectAbility implements IAbility {

  @Override
  public boolean execute(Pet pet, Player player, Event event, AbilityArgs args) {

    final PotionEffectType effect = Util.parseEffect(args.get());

    if (effect == null) {
      Lang.INVALID_CONFIG.send(player, pet.getInternalName());
      return false;
    }

    final int duration = args.get(), amplifier = args.get();

    if (player.hasPotionEffect(effect)
        && player.getActivePotionEffects().stream()
            .anyMatch(potionEffect -> potionEffect.getAmplifier() >= amplifier - 1)) {
      return false;
    }

    if (!player.addPotionEffect(new PotionEffect(effect, duration * 20, amplifier - 1))) {
      Lang.INVALID_POTION_EFFECT.send(player, effect.getName());
      return false;
    }

    Pets.get()
        .getCooldownManager()
        .addCooldown(
            player.getUniqueId(), pet.getInternalName(), Util.parseCooldown(pet.getCooldown()));

    return true;
  }
}
