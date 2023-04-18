package org.github.spook.pets.cmd;

import org.github.spook.pets.enums.Perm;
import org.github.spook.pets.enums.Tier;
import org.github.spook.pets.guis.SubPetGui;
import com.massivecraft.massivecore.SoundEffect;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;

public class CmdPetsGui extends MassiveCommand {

  public CmdPetsGui() {
    addAliases("gui");
    addRequirements(RequirementHasPerm.get(Perm.GUI));
  }

  @Override
  public void perform() {
    if (!senderIsConsole) {
      me.openInventory(getPetGUI().getInventory());
    }
  }

  private ChestGui getPetGUI() {
    final Inventory inventory = Bukkit.createInventory(null, 9, "Pets");
    final ChestGui gui = ChestGui.getCreative(inventory);
    gui.setAutoclosing(true);
    gui.setAutoremoving(true);
    gui.setSoundOpen(SoundEffect.valueOf(Sound.CHEST_OPEN, 1.0F, 1.0F));
    gui.setSoundClose(SoundEffect.valueOf(Sound.CHEST_CLOSE, 1.0F, 1.0F));

    for (Tier tier : Tier.values()) {
      final int slot = gui.getInventory().firstEmpty();
      gui.getInventory().setItem(slot, tier.getGuiCategoryStack());
      gui.setAction(slot, new SubPetGui(this.me, tier));
    }

    return gui;
  }
}
