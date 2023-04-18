package org.github.spook.pets;

import org.github.spook.pets.enums.ActivationType;
import org.github.spook.pets.enums.Tier;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Data(staticConstructor = "create")
@Setter
public class Pet {
  private final String internalName;
  private final String displayName;
  private final List<String> lore;
  private final String textureURL;
  private final ActivationType activationType;
  private final Tier tier;
  private final List<String> disabledWorlds;
  private final int minXPIncrease;
  private final int maxXPIncrease;
  private final int maxLevel;
  private final int xpPerLevel;
  private final String cooldown;
  private final Map<String, Map<Integer, List<String>>> onActivate;
  private transient ItemStack itemStack;

  /** Get the ItemStack for the pet */
  public ItemStack toItemStack() {
    ItemStack stack = Util.getCustomSkull(textureURL);
    final NBTItem nbtItem = new NBTItem(stack);
    final NBTCompound compound = nbtItem.addCompound("petItem");
    compound.setString("internalName", internalName);
    compound.setInteger("level", 1);
    compound.setInteger("exp", 0);
    compound.setInteger("requiredExp", xpPerLevel);
    nbtItem.mergeCompound(compound);
    stack = nbtItem.getItem();
    final ItemMeta meta = stack.getItemMeta();
    meta.setDisplayName(PetAPI.parse(displayName, this, 1, 0, xpPerLevel));
    meta.setLore(PetAPI.getLore(this, 1, 0, xpPerLevel));
    stack.setItemMeta(meta);
    return stack;
  }
}
