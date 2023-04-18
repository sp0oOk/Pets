package org.github.spook.pets;

import org.github.spook.pets.integration.factions.IntegrationFactions;
import org.github.spook.pets.integration.worldguard.IntegrationWorldGuard;
import com.google.common.collect.Lists;
import com.massivecraft.massivecore.util.TimeUnit;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffectType;

public class Util {

  private static final Pattern RANGE_PATTERN = Pattern.compile("%range:(\\d+)-(\\d+)([sm])%");

  /*
  Checks if a player is in a location that is safe for pet abilities to be used.
   */
  public static boolean canEffectsOccur(Player attacker, Player defender) {

    if (IntegrationFactions.get().isIntegrationActive()
        && !IntegrationFactions.get().getEngine().canDamage(attacker, defender)) {
      return false;
    }

    if (IntegrationWorldGuard.get().isIntegrationActive()
        && !IntegrationWorldGuard.get().getEngine().isPvpEnabledAt(defender)) {
      return false;
    }

    return defender.getGameMode() == GameMode.SURVIVAL;
  }

  /**
   * Parses a cooldown string from config into a long value.
   *
   * @param string The string to parse.
   * @return The parsed long value.
   */
  public static long parseCooldown(String string) {
    if (string.isEmpty()) {
      return -1L;
    }

    final Matcher matcher;

    if ((matcher = RANGE_PATTERN.matcher(string)).matches()) {
      final int min = Integer.parseInt(matcher.group(1));
      final int max = Integer.parseInt(matcher.group(2));
      return (matcher.group(3).equals("s")
              ? TimeUnit.MILLIS_PER_SECOND
              : TimeUnit.MILLIS_PER_MINUTE)
          * (min + (int) (Math.random() * (max - min)));
    }

    final char lastChar = string.charAt(string.length() - 1);
    final int time = Integer.parseInt(string.substring(0, string.length() - 1));

    return lastChar == 's' ? TimeUnit.MILLIS_PER_SECOND : TimeUnit.MILLIS_PER_MINUTE * time;
  }

  /**
   * Parses a cooldown string from config into a string value used in the lore of pets.
   *
   * @param string The string to parse.
   * @return The parsed string value.
   */
  public static String parseCooldownString(String string) {
    if (string.isEmpty()) {
      return "none";
    }

    final Matcher matcher;

    if ((matcher = RANGE_PATTERN.matcher(string)).matches()) {
      final int min = Integer.parseInt(matcher.group(1));
      final int max = Integer.parseInt(matcher.group(2));
      return min + " - " + max + " " + (matcher.group(3).equals("s") ? "seconds" : "minutes");
    }

    final char lastChar = string.charAt(string.length() - 1);
    final int time = Integer.parseInt(string.substring(0, string.length() - 1));

    return time + " " + (lastChar == 's' ? "seconds" : "minutes");
  }

  /**
   * Tries to parse material from string.
   *
   * @param material Material to parse
   * @return Material if found, air if not.
   */
  public static Material tryParseMaterial(String material) {
    try {
      return Material.valueOf(material);
    } catch (IllegalArgumentException e) {
      return Material.AIR;
    }
  }

  /**
   * Compares an object to a list of objects.
   *
   * @param comparing Object to compare
   * @param objects Objects to compare to
   * @return True if any of the objects match the comparing object.
   */
  public static boolean doAnyEqual(Object comparing, Object... objects) {
    for (Object object : objects) {
      if (comparing.equals(object)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Boxes a list of strings to their primitive counterparts.
   *
   * @param args List of strings to box
   * @return List of boxed objects
   */
  public static Object[] box(String[] args) {
    String joined = String.join(" ", args);

    String[] split = joined.split("(?<=\")|(?=\")");

    List<String> list = Lists.newArrayList();

    boolean inQuotes = false;
    for (String s : split) {
      if (s.equals("\"")) {
        inQuotes = !inQuotes;
        continue;
      }
      if (inQuotes) {
        list.add(s);
      } else {
        list.addAll(Arrays.asList(s.trim().split(" ")));
      }
    }

    Object[] boxed = new Object[list.size()];
    for (int i = 0; i < boxed.length; i++) {
      boxed[i] = box(list.get(i));
    }

    return boxed;
  }

  /**
   * Generate a random integer in a range. (convenience method)
   *
   * @param min Minimum value
   * @param max Maximum value
   * @return Random integer in range
   */
  public static int randomInt(int min, int max) {
    return (int) (Math.random() * (max - min + 1) + min);
  }

  /**
   * Generates a cube of blocks around a location.
   *
   * @param location Center of the cube
   * @param roof Roof material
   * @param floor Floor material
   * @param wall Wall material
   * @param length Length of the cube
   * @param width Width of the cube
   * @param height Height of the cube
   * @return List of blocks in the cube
   */
  public static List<Block> createCube(
      Location location,
      Material roof,
      Material floor,
      Material wall,
      int length,
      int width,
      int height) {
    final List<Block> blocks = Lists.newArrayList();

    for (int x = 0; x < width; ++x) {
      for (int y = 0; y < height; ++y) {
        for (int z = 0; z < length; ++z) {
          Block block;
          if (x != 0 && y != 0 && z != 0 && x != width - 1 && y != height - 1 && z != length - 1
              || !(block = location.clone().add(x, y, z).getBlock()).getType().equals(Material.AIR))
            continue;
          if (y == 0) {
            block.setType(floor);
          } else if (y == height - 1) {
            block.setType(roof);
          } else {
            block.setType(wall);
          }
          blocks.add(block);
        }
      }
    }

    return blocks;
  }

  /**
   * Parses a potion effect from a string.
   *
   * @param effect The string to parse.
   * @return The parsed potion effect.
   */
  public static PotionEffectType parseEffect(String effect) {
    try {
      return PotionEffectType.getByName(effect);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  /**
   * Parses a damage cause from a string.
   *
   * @param cause The string to parse.
   * @return The parsed damage cause.
   */
  public static EntityDamageEvent.DamageCause parseDamageCause(String cause) {
    try {
      return EntityDamageEvent.DamageCause.valueOf(cause);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  /**
   * Get a textured skull from a minecraft texture url.
   *
   * @param url The url to get the skull from.
   * @return The skull item-stack.
   */
  public static ItemStack getCustomSkull(String url) {
    final ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
    if (url.isEmpty()) return stack;
    final SkullMeta meta = (SkullMeta) stack.getItemMeta();
    meta.setLore(Lists.newArrayList());
    final GameProfile profile = new GameProfile(UUID.randomUUID(), null);
    final byte[] data =
        Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
    profile.getProperties().put("textures", new Property("textures", new String(data)));
    try {
      final Field field = meta.getClass().getDeclaredField("profile");
      field.setAccessible(true);
      field.set(meta, profile);
    } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException ex) {
      ex.printStackTrace();
    }
    stack.setItemMeta(meta);
    return stack;
  }

  /**
   * Boxes a string to its primitive counterpart.
   *
   * @param s The string to box.
   * @return The boxed object.
   */
  private static Object box(String s) {
    if (s.equalsIgnoreCase("true")) return true;
    if (s.equalsIgnoreCase("false")) return false;
    if (s.matches("-?\\d+")) return Integer.parseInt(s);
    if (s.matches("-?\\d+\\.\\d+")) return Double.parseDouble(s);
    return s;
  }
}
