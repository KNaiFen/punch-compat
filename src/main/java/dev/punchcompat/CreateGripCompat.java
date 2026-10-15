package dev.punchcompat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.content.equipment.extendoGrip.ExtendoGripItem;
import com.simibubi.create.content.equipment.extendoGrip.ExtendoGripRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import punchy.client.state.HandEquipStateMachine;
import punchy.config.PunchyConfig;

/** Create's assembly is drawn at Punchy's animated grip, never in camera space. */
public final class CreateGripCompat {
    public static boolean renderingGrip;

    public static boolean isGrip(ItemStack stack) {
        return stack.m_41720_() instanceof ExtendoGripItem;
    }

    public static boolean ownsMainHand(LocalPlayer player) {
        return PunchyConfig.isModEnabled()
                && !PunchyConfig.isHandBlacklisted(player, InteractionHand.MAIN_HAND)
                && !HandEquipStateMachine.shouldAllowVanillaHandOut(InteractionHand.MAIN_HAND);
    }

    public static boolean isRelocatedOffhand(net.minecraft.world.entity.player.Player player, HumanoidArm arm) {
        return arm != player.m_5737_() && isGrip(player.m_21206_());
    }

    public static boolean render(ItemInHandRenderer renderer, LocalPlayer player, HumanoidArm arm,
                                 PoseStack pose, MultiBufferSource buffers, int light, float partialTick) {
        if (arm != player.m_5737_()) return false;
        ItemStack main = player.m_21205_();
        ItemStack off = player.m_21206_();
        boolean inOffhand = isGrip(off);
        if (!inOffhand && !isGrip(main)) return false;

        float swing = player.m_21324_(partialTick);
        if (swing > 0 && 1 - swing > ExtendoGripRenderHandler.mainHandAnimation)
            ExtendoGripRenderHandler.mainHandAnimation = 0.95f;
        float animation = ExtendoGripRenderHandler.lastMainHandAnimation
                + partialTick * (ExtendoGripRenderHandler.mainHandAnimation - ExtendoGripRenderHandler.lastMainHandAnimation);
        animation = animation * animation * animation;
        boolean left = arm == HumanoidArm.LEFT;
        float flip = left ? -1 : 1;
        ItemDisplayContext context = left ? ItemDisplayContext.THIRD_PERSON_LEFT_HAND : ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
        ItemStack grip = inOffhand ? off : main;
        pose.m_85836_();
        renderingGrip = true;
        try {
            // The model display transform remains a normal held-item transform. The scoped
            // Create renderer mixin enables extension/finger animations for this draw only.
            renderer.m_269530_(player, grip, context, left, pose, buffers, light);
            if (inOffhand && !main.m_41619_()) {
                Minecraft mc = Minecraft.m_91087_();
                ForgeHooksClient.handleCameraTransforms(pose, mc.m_91291_().m_174264_(grip, null, null, 0), context, left);
                // Attachment offsets and block handling adapted from Create 6.0.8 (MIT).
                pose.m_252880_(flip * -0.05f, 0.15f, -1.2f - animation * 2.25f);
                if (main.m_41720_() instanceof BlockItem && mc.m_91291_().m_174264_(main, null, null, 0).m_7547_()) {
                    pose.m_252781_(Axis.f_252436_.m_252977_(flip * 45));
                    pose.m_252880_(flip * 0.15f, -0.15f, -0.05f);
                    pose.m_85841_(1.25f, 1.25f, 1.25f);
                }
                renderer.m_269530_(player, main, context, left, pose, buffers, light);
            }
        } finally {
            renderingGrip = false;
            pose.m_85849_();
        }
        return true;
    }
}
