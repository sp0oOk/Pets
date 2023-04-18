package org.github.spook.pets;

import org.github.spook.pets.abilities.IAbility;
import org.github.spook.pets.abilities.PvPRegionOnly;
import org.github.spook.pets.abilities.RequiredEvent;
import org.github.spook.pets.entity.MConf;
import org.github.spook.pets.enums.ActivationType;
import org.github.spook.pets.enums.Lang;
import org.github.spook.pets.enums.Perm;
import org.github.spook.pets.integration.worldguard.IntegrationWorldGuard;
import com.google.common.collect.Sets;
import com.massivecraft.massivecore.Args;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.Txt;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PetAPI {

  private static final String ABILITY_REGEX = "\\[(\\w+)]";

  /**
   * Get the pet from the ItemStack
   *
   * @param stack The ItemStack
   * @return The pet
   */
  public static Optional<Pet> getPetFromItem(@Nonnull ItemStack stack) {
    final NBTItem item;
    if (stack.getType() != Material.SKULL_ITEM || !(item = new NBTItem(stack)).hasKey("petItem")) {
      return Optional.empty();
    }

    final NBTCompound compound = item.getCompound("petItem");

    return MConf.get().pets.stream()
        .filter(p -> p.getInternalName().equals(compound.getString("internalName")))
        .findFirst();
  }

  /**
   * Tries to give the player the pet
   *
   * @param player The player
   * @param pet The pet
   * @param permission The permission (if null, no permission check will be done)
   */
  public static void givePet(Player player, Pet pet, Perm permission) {

    if (permission != null && !player.hasPermission(permission.getId())) {
      return;
    }

    player.getInventory().addItem(pet.toItemStack());
  }

  /**
   * Run Raw Abilities
   *
   * <p>Example: [command] true fly %player%
   *
   * @param stack ItemStack with pet data
   * @param player Player to run abilities on
   * @param event Event to pass to abilities
   * @param effects List of effects and their respective level requirement
   */
  public static void runEffects(
      ItemStack stack,
      Player player,
      Event event,
      Map<Integer, List<String>> effects,
      int slot,
      boolean update) {

    for (Map.Entry<Integer, List<String>> entry : effects.entrySet()) {
      for (String effect : entry.getValue()) {

        final int levelRequired = entry.getKey();

        final Optional<Pet> pet = getPetFromItem(stack);

        if (!pet.isPresent()) {
          continue;
        }

        final int currentPetLevel = getPetLevel(stack);

        if (currentPetLevel < levelRequired && levelRequired != 0) {
          continue;
        }

        final String[] split = effect.split(" ");
        final String ability = split[0].replaceAll(ABILITY_REGEX, "$1");
        final String[] args = new String[split.length - 1];
        System.arraycopy(split, 1, args, 0, args.length);
        final IAbility abilityInstance = getAbility(ability);

        if (abilityInstance == null) {
          continue;
        }

        final Method method;

        try {
          method =
              abilityInstance
                  .getClass()
                  .getDeclaredMethod(
                      "execute", Pet.class, Player.class, Event.class, Args.class);
        } catch (NoSuchMethodException e) {
          e.printStackTrace(); // Should never occur as abilities implement IAbility
          continue;
        }

        final RequiredEvent event1 = method.getAnnotation(RequiredEvent.class);

        if (event1 != null
            && !event1.value().getSimpleName().equals(event.getClass().getSimpleName())) {
          continue;
        }

        if (IntegrationWorldGuard.get().isIntegrationActive()
            && method.getAnnotation(PvPRegionOnly.class) != null
            && !IntegrationWorldGuard.get().getEngine().isPvpEnabledAt(player)) {
          Lang.PVP_REGION_ONLY.send(player);
          continue;
        }
        if (Pets.get()
                .getCooldownManager()
                .isOnCooldown(player.getUniqueId(), pet.get().getInternalName())
            && pet.get().getActivationType() != ActivationType.PASSIVE) {
          Lang.COOLDOWN.send(
              player,
              TimeDiffUtil.getFormalTimeFromMillis(
                  Pets.get()
                          .getCooldownManager()
                          .getCooldown(player.getUniqueId(), pet.get().getInternalName())
                      - System.currentTimeMillis()));
          break;
        }

        if (!abilityInstance.execute(
            pet.get(), player, event, Args.create(Util.box(args), 0))) {
          break;
        }

        if (update) {
          updateAttributes(player, stack, pet.get(), slot);
        }
      }
    }
  }

  /**
   * Get the level of a pet from the ItemStack
   *
   * @param stack The ItemStack
   * @return The level of the pet
   */
  public static int getPetLevel(ItemStack stack) {
    final NBTItem item;
    if (stack.getType() != Material.SKULL_ITEM || !(item = new NBTItem(stack)).hasKey("petItem")) {
      return 0;
    }

    final NBTCompound compound = item.getCompound("petItem");

    return compound.getInteger("level");
  }

  /**
   * Do pet updating, updates all attributes e.g. current exp, required exp, level, etc.
   *
   * @param player Player to update pet for
   * @param stack ItemStack with pet data
   * @param pet Pet to update
   */
  public static void updateAttributes(Player player, ItemStack stack, Pet pet, int slot) {
    final NBTItem item = new NBTItem(stack);
    final NBTCompound compound = item.getCompound("petItem");
    int level = compound.getInteger("level");
    int exp = compound.getInteger("exp");
    int requiredExp = compound.getInteger("requiredExp");

    if (exp >= requiredExp && level < pet.getMaxLevel()) {
      level += 1;
      exp = 0;
      requiredExp = pet.getXpPerLevel() * level;
      compound.setInteger("level", level);
      compound.setInteger("exp", exp);
      compound.setInteger("requiredExp", requiredExp);
      Lang.LEVEL_UP.send(player, parse(pet.getDisplayName(), pet, level, exp, requiredExp), level);
    }

    if (level != pet.getMaxLevel()) {
      exp = Util.randomInt(pet.getMinXPIncrease(), pet.getMaxXPIncrease()) + exp;
      compound.setInteger("exp", exp);
    }

    item.mergeCompound(compound);

    final ItemStack itemStack = item.getItem();
    final ItemMeta meta = itemStack.getItemMeta();

    meta.setDisplayName(parse(pet.getDisplayName(), pet, level, exp, requiredExp));

    meta.setLore(getLore(pet, level, exp, requiredExp));

    itemStack.setItemMeta(meta);

    player.getInventory().setItem(slot, itemStack);
    player.updateInventory();
  }

  /**
   * Convenience method for parsing every string in lore against this definitely placeholder system
   * in place (didn't see a need, as it's like 5 placeholders) see {@link #parse(String, Pet, int,
   * int, int)} for more info
   *
   * @param pet Pet to parse lore for
   * @param level Current level of pet
   * @param exp Current exp of pet
   * @param requiredExp Required exp for next level
   * @return List of parsed lore
   */
  public static List<String> getLore(Pet pet, int level, int exp, int requiredExp) {
    return pet.getLore().stream()
        .map(s -> parse(s, pet, level, exp, requiredExp))
        .collect(Collectors.toList());
  }

  /**
   * Update the hotbars in a player's inventory, this is used for passive pets
   *
   * @param player Player to update hotbar for
   * @param runEffects Whether to run effects or not (or just add xp)
   * @param eventRaw Event to run effects for (PlayerInteractEvent, PlayerMoveEvent, etc.)
   * @param event Event object
   */
  public static void updateHotbarPets(
      Player player, boolean runEffects, String eventRaw, Event event) {
    Set<String> petInternalNames = Sets.newHashSet();
    for (int i = 0; i < 9; i++) {
      final ItemStack item = player.getInventory().getItem(i);
      final Pet pet;
      if (item == null
          || item.getType() != Material.SKULL_ITEM
          || (pet = PetAPI.getPetFromItem(item).orElse(null)) == null
          || pet.getActivationType() != ActivationType.PASSIVE) {
        continue;
      }
      String petInternalName = pet.getInternalName();
      if (petInternalNames.contains(petInternalName)) {
        return;
      }

      petInternalNames.add(petInternalName);
      final boolean contains = pet.getOnActivate().containsKey("AlwaysOnTick");

      if (runEffects && eventRaw != null && event != null || contains) {
        PetAPI.runEffects(
            item,
            player,
            event,
            pet.getOnActivate().get(eventRaw == null ? "AlwaysOnTick" : eventRaw),
            i,
            contains);
      } else {
        PetAPI.updateAttributes(player, item, pet, i);
      }
    }
    player.updateInventory();
  }

  /**
   * Parses a string against a range of placeholders
   *
   * @param s String to parse
   * @param pet Pet to parse string for
   * @param level Current level of pet
   * @param exp Current exp of pet
   * @param requiredExp Required exp for next level
   * @return Parsed string
   */
  public static String parse(String s, Pet pet, int level, int exp, int requiredExp) {
    return Txt.colorize(
        s.replace("{tier-color-bold}", pet.getTier().getBoldColor())
            .replace("{level}", String.valueOf(level))
            .replace("{exp}", String.valueOf(exp))
            .replace("{required-exp}", String.valueOf(requiredExp))
            .replace("{pet-type}", pet.getActivationType().getColoredBoldName())
            .replace("{cooldown}", Util.parseCooldownString(pet.getCooldown()))
            .replace(
                "{progress-bar}",
                getProgressBar(exp, requiredExp, 20, '|', ChatColor.GREEN, ChatColor.RED)));
  }

  /**
   * Creates progress bar
   *
   * @param current Current progress
   * @param max Max progress
   * @param totalBars Total bars to display
   * @param symbol Symbol to use for bars
   * @param completedColor Color to use for completed bars
   * @param notCompletedColor Color to use for not completed bars
   * @return Progress bar
   */
  public static String getProgressBar(
      int current,
      int max,
      int totalBars,
      char symbol,
      ChatColor completedColor,
      ChatColor notCompletedColor) {
    float percent = (float) current / max;
    int progressBars = (int) (totalBars * percent);

    String completed =
        completedColor + new String(new char[progressBars]).replace("\0", String.valueOf(symbol));
    String notCompleted =
        notCompletedColor
            + new String(new char[totalBars - progressBars]).replace("\0", String.valueOf(symbol));

    return completed + notCompleted;
  }

  /**
   * Get a pet by its internal name
   *
   * @param string Internal name of pet
   * @return Pet else null
   */
  public static Pet getByName(String string) {
    return MConf.get().pets.stream()
        .filter(p -> p.getInternalName().equals(string))
        .findFirst()
        .orElse(null);
  }

  /**
   * Get an ability by its internal name
   *
   * @param name Internal name of ability
   * @return Ability else null
   */
  public static IAbility getAbility(String name) {
    return Pets.get().getAbilities().getOrDefault(name, null);
  }
}
