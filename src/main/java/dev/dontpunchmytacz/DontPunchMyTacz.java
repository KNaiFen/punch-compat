package dev.dontpunchmytacz;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(DontPunchMyTacz.MOD_ID)
public final class DontPunchMyTacz {
    public static final String MOD_ID = "punch_compat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public DontPunchMyTacz(FMLJavaModLoadingContext context) {
        context.getModEventBus().addListener(this::onClientSetup);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("punchy")) event.enqueueWork(BlacklistForce::apply);
    }
}
