package dev.punchcompat.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.xtracr.realcamera.compat.DisableHelper;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Mixin 0.8.5 inserts HEAD callbacks in ascending priority order (verified on the production renderer).
@Mixin(targets = "net.minecraft.client.renderer.ItemInHandRenderer", priority = 500, remap = false)
public abstract class RealCameraMixin {
    // Forge 1.20.1 production name of renderHandsWithItems; compiled against SRG jars.
    @Inject(
            method = "m_109314_(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/player/LocalPlayer;I)V",
            at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private void punchyRealCamera$hideHandsBeforePunchy(float partialTick, PoseStack poseStack,
            MultiBufferSource.BufferSource buffers, LocalPlayer player, int light, CallbackInfo ci) {
        // Use RealCamera's own per-frame decision, including item and mode exceptions.
        if (DisableHelper.RENDER_HANDS.disabled(player)) {
            ci.cancel();
        }
    }
}
