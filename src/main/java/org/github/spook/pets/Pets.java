package org.github.spook.pets;

import org.github.spook.pets.abilities.IAbility;
import org.github.spook.pets.abilities.impl.CageAbility;
import org.github.spook.pets.abilities.impl.CommandAbility;
import org.github.spook.pets.abilities.impl.EffectAbility;
import org.github.spook.pets.abilities.impl.ReduceDamageAbility;
import org.github.spook.pets.entity.MConf;
import com.massivecraft.massivecore.BasicCooldownManager;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

import java.util.Map;
import java.util.UUID;

public final class Pets extends MassivePlugin {
  private static Pets i;
  private BasicCooldownManager<UUID, String> basicCooldownManager;
  private Map<String, IAbility> abilities;

  public Pets() {
    Pets.i = this;
    Pets.i.basicCooldownManager = BasicCooldownManager.create();
    Pets.i.abilities =
        MUtil.map(
            "cage",
            new CageAbility(),
            "command",
            new CommandAbility(),
            "effect",
            new EffectAbility(),
            "reduce_damage",
            new ReduceDamageAbility());
  }

  @Override
  public void onEnableInner() {
    super.onEnableInner();
    activateAuto();
    log(
        Txt.parse(
            "<yellow>Registered <a>%s <yellow>total pets, and <a>%s <yellow>abilities.",
            MConf.get().pets.size(), abilities.size()));
  }

  /**
   * @return The instance of this plugin.
   */
  public static Pets get() {
    return Pets.i;
  }

  /**
   * @return the cooldownManager
   */
  public BasicCooldownManager<UUID, String> getCooldownManager() {
    return basicCooldownManager;
  }

  /**
   * @return the abilities
   */
  public Map<String, IAbility> getAbilities() {
    return abilities;
  }
}
