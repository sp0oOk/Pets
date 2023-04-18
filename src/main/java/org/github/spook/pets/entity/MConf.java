package org.github.spook.pets.entity;

import org.github.spook.pets.enums.ActivationType;
import org.github.spook.pets.enums.Tier;
import org.github.spook.pets.Pet;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import java.util.List;

public class MConf extends Entity<MConf> {

  protected static MConf i;

  public List<Pet> pets =
      MUtil.list(
          // --------------------------------------------------- //
          //                 COMMON PETS                         //
          // --------------------------------------------------- //
          Pet.create(
              "sheepPet",
              "{tier-color-bold}Sheep Pet &7[Level {level}]",
              MUtil.list(
                  "",
                  "{pet-type}",
                  "&fReduces fall damage.",
                  "",
                  "&e&lLevel: {level}",
                  "&e&lEXP: {exp} / {required-exp}",
                  "{progress-bar}"),
              "https://textures.minecraft.net/texture/30f50394c6d7dbc03ea59fdf504020dc5d6548f9d3bc9dcac896bb5ca08587a",
              ActivationType.PASSIVE,
              Tier.COMMON,
              MUtil.list(),
              10,
              100,
              100,
              1000,
              "",
              MUtil.map(
                  "EntityDamageEvent",
                  MUtil.map(
                      25,
                      MUtil.list("[reduce_damage] FALL 5.0"),
                      50,
                      MUtil.list("[reduce_damage] FALL 10.0"),
                      75,
                      MUtil.list("[reduce_damage] FALL 15.0"),
                      100,
                      MUtil.list("[reduce_damage] FALL 20.0")))),
          Pet.create(
              "shieldPet",
              "{tier-color-bold}Shield Pet &7[Level {level}]",
              MUtil.list(
                  "",
                  "{pet-type}",
                  "&fGain a temporary Resistance effect.",
                  "&fCooldown: {cooldown}",
                  "",
                  "&eLevel: {level}",
                  "&eEXP: {exp} / {required-exp}",
                  "{progress-bar}"),
              "https://textures.minecraft.net/texture/b26f501d2117d4397863ce4c9d9b85f267bd58f03cafb25a569929bb588bce71",
              ActivationType.ACTIVE,
              Tier.COMMON,
              MUtil.list(),
              10,
              150,
              100,
              1000,
              "30s",
              MUtil.map(
                  "PlayerInteractEvent",
                  MUtil.map(
                      1,
                      MUtil.list("[effect] DAMAGE_RESISTANCE 6 1"),
                      40,
                      MUtil.list("[effect] DAMAGE_RESISTANCE 6 2"),
                      80,
                      MUtil.list("[effect] DAMAGE_RESISTANCE 8 3"),
                      100,
                      MUtil.list("[effect] DAMAGE_RESISTANCE 8 4")))),
          Pet.create(
              "silverfishPet",
              "{tier-color-bold}Silverfish Pet &7[Level {level}]",
              MUtil.list(
                  "",
                  "{pet-type}",
                  "&fGain a permanent Haste effect.",
                  "",
                  "&e&lLevel: {level}",
                  "&e&lEXP: {exp} / {required-exp}",
                  "{progress-bar}"),
              "https://textures.minecraft.net/texture/c02cb217d829dc356283918e71446a8b9a89cc97c7c1a2135a5276fbb0554e6a",
              ActivationType.PASSIVE,
              Tier.COMMON,
              MUtil.list(),
              10,
              150,
              100,
              1000,
              "5s",
              MUtil.map(
                  "AlwaysOnTick",
                  MUtil.map(
                      1,
                      MUtil.list("[effect] FAST_DIGGING 5 1"),
                      25,
                      MUtil.list("[effect] FAST_DIGGING 5 2"),
                      50,
                      MUtil.list("[effect] FAST_DIGGING 5 3"),
                      75,
                      MUtil.list("[effect] FAST_DIGGING 5 4")))),
          // --------------------------------------------------- //
          //                 RARE PETS                           //
          // --------------------------------------------------- //
          Pet.create(
              "cagePet",
              "{tier-color-bold}Cage Pet &7[Level {level}]",
              MUtil.list(
                  "",
                  "{pet-type}",
                  "&fSummons a dimensional cage for a",
                  "&flimited time.",
                  "&fCooldown: {cooldown}",
                  "",
                  "&eLevel: {level}",
                  "&eEXP: {exp} / {required-exp}",
                  "{progress-bar}"),
              "https://textures.minecraft.net/texture/1427260023187be9b3b1a7ba125d61d5bb21d686257233fb9807a6c213a491e1",
              ActivationType.ACTIVE,
              Tier.COMMON,
              MUtil.list(),
              10,
              150,
              100,
              1000,
              "%range:1-3m%",
              MUtil.map(
                  "PlayerInteractEvent",
                  MUtil.map(1, MUtil.list("[cage] OBSIDIAN OBSIDIAN IRON_FENCE 5 5 5 10")))),
          Pet.create(
              "ironGolem",
              "{tier-color-bold}Iron Golem Pet &7[Level {level}]",
              MUtil.list(
                  "",
                  "{pet-type}",
                  "&fGain temporary absorption hearts.",
                  "&fCooldown: {cooldown}",
                  "",
                  "&eLevel: {level}",
                  "&eEXP: {exp} / {required-exp}",
                  "{progress-bar}"),
              "https://textures.minecraft.net/texture/8b9ec5ee0c02482858dea365c6b313ee3a9435b688aee4752306d80fae87790",
              ActivationType.ACTIVE,
              Tier.MYTHIC,
              MUtil.list(),
              10,
              150,
              100,
              1000,
              "%range:20-25s%",
              MUtil.map(
                  "PlayerInteractEvent",
                  MUtil.map(
                      1,
                      MUtil.list("[effect] ABSORPTION 7 3"),
                      50,
                      MUtil.list("[effect] ABSORPTION 6 4"),
                      75,
                      MUtil.list("[effect] ABSORPTION 5 5"),
                      100,
                      MUtil.list("[effect] ABSORPTION 5 6")))));

  public static MConf get() {
    return MConf.i;
  }
}
