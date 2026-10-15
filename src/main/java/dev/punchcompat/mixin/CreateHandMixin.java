package dev.punchcompat.mixin;

import dev.punchcompat.CreateGripCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.client.event.RenderHandEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.simibubi.create.content.equipment.extendoGrip.ExtendoGripRenderHandler", remap = false)
public abstract class CreateHandMixin {
    @Inject(method = "onRenderPlayerHand", at = @At("HEAD"), cancellable = true, require = 1)
    private static void punchCompat$deferToAnimatedGrip(RenderHandEvent event, CallbackInfo ci) {
        var player = Minecraft.m_91087_().f_91074_;
        if (CreateGripCompat.ownsMainHand(player)) {
            // The offhand grip is already part of the main-hand assembly, even during
            // Punchy's equip transition when vanilla rendering may be allowed through.
            if (event.getHand() == InteractionHand.OFF_HAND && CreateGripCompat.isGrip(player.m_21206_()))
                event.setCanceled(true);
            ci.cancel();
        }
    }
}
