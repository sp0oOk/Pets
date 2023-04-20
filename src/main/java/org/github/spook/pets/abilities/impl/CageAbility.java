package org.github.spook.pets.abilities.impl;

import org.github.spook.pets.Pets;
import org.github.spook.pets.Util;
import org.github.spook.pets.abilities.IAbility;
import org.github.spook.pets.abilities.PvPRegionOnly;
import org.github.spook.pets.abilities.RequiredEvent;
import org.github.spook.pets.enums.Lang;
import org.github.spook.pets.integration.worldguard.IntegrationWorldGuard;
import org.github.spook.pets.AbilityArgs;
import com.massivecraft.massivecore.Pair;
import org.github.spook.pets.Pet;
import com.massivecraft.massivecore.MetadataSimple;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.util.MUtil;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class CageAbility implements IAbility {

  private final Map<UUID, Pair<List<Block>, Integer>> cages = new MassiveMap<>();

  @Override
  @RequiredEvent(PlayerInteractEvent.class)
  @PvPRegionOnly
  public boolean execute(Pet pet, Player player, Event _event, AbilityArgs args) {

    final Set<Player> players =
        MUtil.getNearbyPlayers(player, 8.0D, false).stream()
            .filter(nearby -> !isCaged(nearby) && Util.canEffectsOccur(player, nearby))
            .collect(Collectors.toSet());

    if (players.isEmpty()) {
      Lang.PLAYERS_NOT_FOUND.send(player, player.getName());
      return false;
    }
    
    final String roof = args.get(), floor = args.get(), wall = args.get();

    final Material roofMaterial = Util.tryParseMaterial(roof),
        floorMaterial = Util.tryParseMaterial(floor),
        wallMaterial = Util.tryParseMaterial(wall);

    final int length = args.get(), width = args.get(), height = args.get(), expire = args.get();

    if (Util.doAnyEqual(Material.AIR, roofMaterial, floorMaterial, wallMaterial)) {
      Lang.INVALID_CONFIG.send(player, pet.getInternalName());
      return false;
    }

    Lang.DONE_X_TO_Y_PLAYERS.send(player, "Dimensional Cage", players.size());

    players.forEach(
        p -> cage(p, roofMaterial, floorMaterial, wallMaterial, length, width, height, expire));

    Pets.get()
        .getCooldownManager()
        .addCooldown(
            player.getUniqueId(), pet.getInternalName(), Util.parseCooldown(pet.getCooldown()));
    return true;
  }

  private void cage(
      Player p,
      Material roofMaterial,
      Material floorMaterial,
      Material wallMaterial,
      int length,
      int width,
      int height,
      int expire) {
    if (cages.containsKey(p.getUniqueId())
        || IntegrationWorldGuard.get().isIntegrationActive()
            && !IntegrationWorldGuard.get().getEngine().canBuildAt(p, p.getLocation())) {
      return;
    }

    p.setMetadata("pets_ability_caged", new MetadataSimple(Pets.get(), null));

    final Location location = p.getLocation().subtract((float) width / 2, 1.0, (float) length / 2);
    final List<Block> blocks =
        Util.createCube(location, roofMaterial, floorMaterial, wallMaterial, length, width, height);

    if (blocks.isEmpty()) {
      return;
    }

    cages.put(
        p.getUniqueId(),
        Pair.of(
            blocks,
            new BukkitRunnable() {

              int alive = 0;

              @Override
              public void run() {
                alive++;

                if (alive >= expire) {
                  blocks.forEach(b -> b.setType(Material.AIR));
                  p.removeMetadata("pets_ability_caged", Pets.get());
                  cages.remove(p.getUniqueId());
                  cancel();
                }
              }
            }.runTaskTimer(Pets.get(), 20L, 20L).getTaskId()));
  }

  public Map<UUID, Pair<List<Block>, Integer>> getCages() {
    return cages;
  }

  private boolean isCaged(Player player) {
    return player.hasMetadata("pets_ability_caged");
  }
}
