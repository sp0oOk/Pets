package org.github.spook.pets.enums;

import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum Tier {
  COMMON("Common", ChatColor.GRAY, (short) 7),
  RARE("Rare", ChatColor.AQUA, (short) 3),
  MYTHIC("Mythical", ChatColor.YELLOW, (short) 4),
  OVERPOWERED("Overpowered", ChatColor.DARK_RED, (short) 14),
  EVENT("Event", ChatColor.GREEN, (short) 13);

  private final String name;
  private final ChatColor color;
  private final short durability;

  Tier(final String name, final ChatColor color, short durability) {
    this.name = name;
    this.color = color;
    this.durability = durability;
  }

  public String getName() {
    return this.name;
  }

  public ChatColor getColor() {
    return this.color;
  }

  public short getDurability() {
    return durability;
  }

  public String getBoldColor() {
    return this.color + "" + ChatColor.BOLD;
  }

  public ItemStack getGuiCategoryStack() {
    final ItemStack stack = new ItemStack(Material.STAINED_GLASS_PANE, 1, this.durability);
    final ItemMeta meta = stack.getItemMeta();
    meta.setDisplayName(this.getColor() + this.getName() + " Pets");
    meta.setLore(
        MUtil.list(
            "",
            ChatColor.WHITE + "Click me to view all of the",
            this.getColor() + this.getName() + ChatColor.RESET + " pets.",
            ""));
    stack.setItemMeta(meta);
    return stack;
  }
}
