package org.github.spook.pets.integration;

import com.massivecraft.massivecore.Integration;

public class IntegrationAdvancedEnchants extends Integration {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //
    private static final IntegrationAdvancedEnchants i = new IntegrationAdvancedEnchants();
    public static IntegrationAdvancedEnchants get() { return i; }

    public IntegrationAdvancedEnchants() {
        setPluginName("AdvancedEnchantments");
    }
}
