package org.github.spook.pets.guis;

import org.github.spook.pets.PetAPI;
import org.github.spook.pets.entity.MConf;
import org.github.spook.pets.enums.Perm;
import org.github.spook.pets.enums.Tier;
import org.github.spook.pets.Pet;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import com.massivecraft.massivecore.chestgui.ChestGui;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class SubPetGui extends ChestActionAbstract {

  private final Player player;
  private final List<Pet> pets;

  public SubPetGui(Player player, Tier tier) {
    this.player = player;
    this.pets =
        MConf.get().pets.stream().filter(pet -> pet.getTier() == tier).collect(Collectors.toList());
  }

  @Override
  public boolean onClick(InventoryClickEvent event) {
    openChestGui();
    return true;
  }

  private void openChestGui() {
    final Inventory inventory =
        Bukkit.createInventory(null, (int) Math.ceil((double) pets.size() / 9) * 9, "Pets");
    final ChestGui gui = ChestGui.getCreative(inventory);
    gui.setAutoclosing(true);
    gui.setAutoremoving(true);
    gui.setSoundOpen(null);
    gui.setSoundClose(null);

    for (Pet pet : pets) {
      final int slot = gui.getInventory().firstEmpty();
      gui.getInventory().setItem(slot, pet.toItemStack());
      gui.setAction(
          slot,
          new ChestActionAbstract() {
            @Override
            public boolean onClick(InventoryClickEvent event) {
              PetAPI.givePet(player, pet, Perm.GIVE);
              return true;
            }
          });
    }

    this.player.openInventory(gui.getInventory());
  }
}
