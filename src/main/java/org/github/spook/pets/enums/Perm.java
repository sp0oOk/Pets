package org.github.spook.pets.enums;

import org.github.spook.pets.Pets;
import com.massivecraft.massivecore.Identified;
import com.massivecraft.massivecore.util.PermissionUtil;
import org.bukkit.permissions.Permissible;

public enum Perm implements Identified {
  PETS,
  GIVE,
  VERSION,
  GUI;

  private final String id = PermissionUtil.createPermissionId(Pets.get(), this);

  @Override
  public String getId() {
    return id;
  }

  public boolean has(Permissible permissible, boolean verbose) {
    return PermissionUtil.hasPermission(permissible, this, verbose);
  }

  public boolean has(Permissible permissible) {
    return this.has(permissible, true);
  }
}
