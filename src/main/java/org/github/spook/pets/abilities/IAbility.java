package org.github.spook.pets.abilities;

import org.github.spook.pets.Pet;
import com.massivecraft.massivecore.Args;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public interface IAbility {

  boolean execute(Pet pet, Player player, Event event, Args args);
}
