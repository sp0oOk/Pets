package org.github.spook.pets.engine;

import com.massivecraft.massivecore.Engine;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.github.spook.pets.Pet;
import org.github.spook.pets.PetAPI;
import org.github.spook.pets.Pets;
import org.github.spook.pets.abilities.impl.CageAbility;

public class EnginePets extends Engine {

  // -------------------------------------------- //
  // INSTANCE & CONSTRUCT
  // -------------------------------------------- //
  private static final EnginePets i = new EnginePets();

  public static EnginePets get() {
    return i;
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onBlockPlace(BlockPlaceEvent event) {
    if (event.getItemInHand().getType() == Material.SKULL_ITEM
        && new NBTItem(event.getItemInHand()).hasKey("petItem")) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onDamage(EntityDamageEvent event) {
    if (event.getEntity() instanceof Player) {
      PetAPI.updateHotbarPets((Player) event.getEntity(), true, "EntityDamageEvent", event);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onInteract(PlayerInteractEvent event) {
    final Pet pet;
    if (event.hasItem()
        && (event.getAction() == Action.RIGHT_CLICK_AIR
            || event.getAction() == Action.RIGHT_CLICK_BLOCK)
        && event.getItem().getType() == Material.SKULL_ITEM
        && (pet = PetAPI.getPetFromItem(event.getItem()).orElse(null)) != null
        && (pet.getOnActivate().containsKey("PlayerInteractEvent"))) {
      final int slot = event.getPlayer().getInventory().getHeldItemSlot();
      PetAPI.runEffects(
          event.getItem(),
          event.getPlayer(),
          event,
          pet.getOnActivate().get("PlayerInteractEvent"),
          slot,
          true);
    }
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    Pets.get().getCooldownManager().removeAllCooldowns(event.getPlayer().getUniqueId());
  }

  @EventHandler
  public void onShutdown(PluginDisableEvent event) {
    if (event.getPlugin().equals(Pets.get())) {
      ((CageAbility) Pets.get().getAbilities().get("cage"))
          .getCages()
          .forEach(
              (uuid, pair) -> {
                final Player player = Bukkit.getPlayer(uuid);

                if (player != null && player.isOnline()) {
                  player.removeMetadata("pets_ability_caged", Pets.get());
                }

                if (Bukkit.getScheduler().isCurrentlyRunning(pair.getRight())) {
                  Bukkit.getScheduler().cancelTask(pair.getRight());
                }

                pair.getLeft().forEach(b -> b.setType(Material.AIR));
              });
    }
  }
}
