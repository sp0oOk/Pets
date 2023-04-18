package org.github.spook.pets.visuals;

import org.github.spook.pets.Pets;
import com.massivecraft.massivecore.mixin.MixinTitle;
import com.massivecraft.massivecore.particleeffect.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RiftEffect extends BukkitRunnable {

  private final Player player;
  private final Location location;
  private double phi = 0;

  public RiftEffect(Player player) {
    this.player = player;
    this.location = player.getLocation();
  }

  @Override
  public void run() {
    phi += Math.PI / 10;

    for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 40) {
      double r = 1.5;
      double x = r * Math.cos(theta) * Math.sin(phi);
      double y = r * Math.cos(phi) + 1.5;
      double z = r * Math.sin(theta) * Math.sin(phi); // Cartesian to spherical coordinates
      location.add(x, y, z);
      ParticleEffect.DRIP_WATER.display(location, 0, 0, 0, 0, 1);
      location.subtract(x, y, z);
      MixinTitle.get()
          .sendTitleMsg(
              player,
              20,
              20,
              20,
              "&3&l*** &b&lSPACE RIFT &3&l***",
              "&7You are transcending the space-time continuum...");
    }

    if (phi > Math.PI) {
      this.cancel();
    }
  }

  public void start() {
    runTaskTimerAsynchronously(Pets.get(), 0, 1);
  }
}
