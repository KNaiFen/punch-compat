package dev.dontpunchmytacz.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "punchy.client.render.PunchyArmRenderer", remap = false)
public class PunchyInvisibleArmsMixin {

    @Inject(method = "renderArm", at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private static void dontpunchmytacz$hideInvisibleSkinArm(CallbackInfo ci) {
        Minecraft minecraft = Minecraft.m_91087_();
        if (minecraft != null && minecraft.f_91074_ != null && minecraft.f_91074_.m_20145_()) {
            ci.cancel();
        }
    }
}
