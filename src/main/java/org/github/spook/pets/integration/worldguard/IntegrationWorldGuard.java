package org.github.spook.pets.integration.worldguard;

import com.massivecraft.massivecore.Integration;

public class IntegrationWorldGuard extends Integration {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //
    private static final IntegrationWorldGuard i = new IntegrationWorldGuard();
    public static IntegrationWorldGuard get() { return i; }

    public IntegrationWorldGuard() {
        setPluginName("WorldGuard");
    }

    @Override
    public EngineWorldGuard getEngine() {
        return EngineWorldGuard.get();
    }

}
