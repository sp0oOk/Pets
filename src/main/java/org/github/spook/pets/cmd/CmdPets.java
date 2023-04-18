package org.github.spook.pets.cmd;

import org.github.spook.pets.enums.Perm;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

public class CmdPets extends MassiveCommand {

  // -------------------------------------------- //
  // INSTANCE & CONSTRUCT
  // -------------------------------------------- //

  private static final CmdPets i = new CmdPets();

  public static CmdPets get() {
    return i;
  }

  public CmdPets() {
    addAliases("pets");
    addChild(cmdPetsGui);
    addChild(cmdPetsVersion);
    addRequirements(RequirementHasPerm.get(Perm.PETS));
  }

  // -------------------------------------------- //
  // CHILD COMMANDS
  // -------------------------------------------- //

  public CmdPetsGui cmdPetsGui = new CmdPetsGui();
  public CmdPetsVersion cmdPetsVersion = new CmdPetsVersion();
}
