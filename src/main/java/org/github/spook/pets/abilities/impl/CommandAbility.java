package org.github.spook.pets.abilities.impl;

import org.github.spook.pets.abilities.IAbility;
import org.github.spook.pets.abilities.RequiredEvent;
import org.github.spook.pets.Pet;
import java.util.regex.Pattern;

import org.github.spook.pets.AbilityArgs;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

public class CommandAbility implements IAbility {

  private static final Pattern PATTERN = Pattern.compile("\\[command] (true|false) ");

  @Override
  @RequiredEvent(PlayerInteractEvent.class)
  public boolean execute(Pet pet, Player player, Event event, AbilityArgs args) {

    final boolean asOp = args.get();
    final String command =
        PATTERN.matcher(args.getAll()).replaceAll("").replace("{player}", player.getName());

    if (asOp) {
      player.getServer().dispatchCommand(player.getServer().getConsoleSender(), command);
    } else {
      player.performCommand(command);
    }

    return true;
  }
}
