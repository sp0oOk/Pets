package org.github.spook.pets.task;

import org.github.spook.pets.PetAPI;
import com.massivecraft.massivecore.ModuloRepeatTask;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.entity.Player;

public class TaskUpdateHotbar extends ModuloRepeatTask {

  private static TaskUpdateHotbar i = new TaskUpdateHotbar();
  public static TaskUpdateHotbar get() { return i; }

  @Override
  public void invoke(long now) {
    for (Player player : MUtil.getOnlinePlayers()) {
      PetAPI.updateHotbarPets(player, false, null, null);
    }
  }

  @Override
  public long getDelayMillis() {
    return 1000L;
  }
}
