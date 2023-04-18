package org.github.spook.pets.cmd;

import org.github.spook.pets.Pets;
import org.github.spook.pets.enums.Perm;
import com.massivecraft.massivecore.command.MassiveCommandVersion;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

public class CmdPetsVersion extends MassiveCommandVersion {

  public CmdPetsVersion() {
    super(Pets.get());
    addRequirements(RequirementHasPerm.get(Perm.VERSION));
    addAliases("ver");
  }
}
