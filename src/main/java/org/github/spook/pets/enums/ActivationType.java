package org.github.spook.pets.enums;

import org.bukkit.ChatColor;

public enum ActivationType {
  PASSIVE("PASSIVE", ChatColor.YELLOW),
  AGGRESSIVE("AGGRESSIVE", ChatColor.RED),
  ACTIVE("ACTIVE", ChatColor.GREEN);

  private final String name;
  private final ChatColor color;

  ActivationType(String name, ChatColor color) {
    this.name = name;
    this.color = color;
  }

  public String getName() {
    return name;
  }

  public ChatColor getColor() {
    return color;
  }

  public String getColoredBoldName() {
    return color + "" + ChatColor.BOLD + name;
  }
}
