package org.github.spook.pets.integration.factions;

import com.massivecraft.massivecore.Integration;

public class IntegrationFactions extends Integration {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //
    private static final IntegrationFactions i = new IntegrationFactions();
    public static IntegrationFactions get() { return i; }

    public IntegrationFactions() {
        setPluginName("Factions");
    }

    @Override
    public EngineFactions getEngine() {
        return EngineFactions.get();
    }
}
