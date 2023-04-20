# 🐾 Minecraft Inventory Pets Plugin

A unique Minecraft pets plugin, meant to mimic TreasurePets as it is discontinued. This plugin allows players to interact with special items or "pets" that have unique abilities that can be upgraded with each use. The plugin is coded against the 1.8.8 spigot API version.

## Abilities

The current abilities that are coded into the plugin are:

• Cage Ability: `[cage] <roof=material> <floor=material> <wall=material> <length,width,height,expire=int>` - Spawns a cube around the affected players in a radius of 8 around the player.

• Command Ability: `[command] <operator=true|false>  <command>` - Executes a server command  as OP/non OP (Supports {player} as placeholder for pet user)

• Effect Ability: `[effect] <potionEffect=PotionEffectType> <duration,amplifier=int>` - Adds a potion effect to the user (Will be changed to effect attacker/defender instead of just attacker soon...)

• Reduce Damage Ability: `[reduce_damage] <causeNeeded=DamageCause> <amt=double>` - Reduces damage for X cause.

More abilities will be added soon! For more information on DamageCause and PotionEffectType, please refer to the [DamageCause documentation](https://helpch.at/docs/1.8/org/bukkit/event/entity/EntityDamageEvent.DamageCause.html) and [PotionEffectType documentation](https://helpch.at/docs/1.8/index.html?org/bukkit/potion/PotionEffectType.html).

## Library Plugin

This plugin is built off the [MassiveCore](https://github.com/MassiveCraft) library plugin (custom fork). The MassiveCore plugin provides useful features, as-well as a-lot of convenience methods and support for things such as hot reloadable configs and easy class serialization/deserialization.

## Contributing

If you have any suggestions or would like to contribute to the plugin by adding new abilities, please feel free to open a pull request or submit an issue on the [GitHub repository](https://github.com/sp0oOk/Pets). All contributions are appreciated as I probably won't have time/motivation to actively maintain this myself. Please note, that the "unique" classes in my fork of MassiveCore are not all that complicated, anyone who wants to contribute to this just get in contact with me if you need assitance.


## Showcase

![](https://i.imgur.com/72iHQTn.gif)
