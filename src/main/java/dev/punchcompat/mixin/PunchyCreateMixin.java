package dev.punchcompat.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.punchcompat.CreateGripCompat;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "punchy.client.render.PunchyArmRenderer", remap = false)
public abstract class PunchyCreateMixin {
    @Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true, require = 1)
    private static void punchCompat$hideRelocatedItem(ItemInHandRenderer renderer, PlayerModel<?> model,
            LocalPlayer player, HumanoidArm arm, PoseStack pose, MultiBufferSource buffers, int light,
            Matrix4f root, ModelPart armBone, ModelPart itemBone, ModelPart gripBone, float tick, CallbackInfo ci) {
        if (CreateGripCompat.isRelocatedOffhand(player, arm)) ci.cancel();
    }

    @Inject(method = "renderItemInHand", at = @At(value = "INVOKE", target =
            "Lpunchy/client/render/PunchyArmRenderer;applyGripLocalTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/model/geom/ModelPart;Lorg/joml/Vector3f;)V",
            shift = At.Shift.AFTER), cancellable = true, require = 1)
    private static void punchCompat$renderAtAnimatedGrip(ItemInHandRenderer renderer, PlayerModel<?> model,
            LocalPlayer player, HumanoidArm arm, PoseStack pose, MultiBufferSource buffers, int light,
            Matrix4f root, ModelPart armBone, ModelPart itemBone, ModelPart gripBone, float tick, CallbackInfo ci) {
        if (CreateGripCompat.render(renderer, player, arm, pose, buffers, light, tick)) {
            // Balance the original renderItemInHand push before its early return.
            pose.m_85849_();
            ci.cancel();
        }
    }

    @Inject(method = "renderArm", at = @At("HEAD"), cancellable = true, require = 1)
    private static void punchCompat$hideRelocatedArm(PlayerModel<?> model, AbstractClientPlayer player,
            HumanoidArm arm, PoseStack pose, MultiBufferSource buffers, int light, ResourceLocation skin,
            boolean slim, float tick, CallbackInfo ci) {
        if (CreateGripCompat.isRelocatedOffhand(player, arm)) ci.cancel();
    }

    @Inject(method = "renderArmArmor", at = @At("HEAD"), cancellable = true, require = 1)
    private static void punchCompat$hideRelocatedSleeve(PlayerModel<?> model, AbstractClientPlayer player,
            HumanoidArm arm, PoseStack pose, MultiBufferSource buffers, int light, boolean slim,
            float tick, CallbackInfo ci) {
        if (CreateGripCompat.isRelocatedOffhand(player, arm)) ci.cancel();
    }
}
