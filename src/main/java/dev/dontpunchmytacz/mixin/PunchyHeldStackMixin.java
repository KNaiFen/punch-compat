package dev.dontpunchmytacz.mixin;

import dev.dontpunchmytacz.DontPunchMyTacz;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "punchy.client.animation.PunchyAnimationManager", remap = false)
public class PunchyHeldStackMixin {

    private static boolean dontpunchmytacz$reported = false;

    @Inject(method = "resolveRenderStack(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/HumanoidArm;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"), cancellable = true, require = 1, remap = false)
    private static void dontpunchmytacz$keepLiveHeldStack(Player player, HumanoidArm arm, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack rendered = cir.getReturnValue();
        if (player == null || arm == null || rendered == null || rendered.m_41619_()) {
            return;
        }
        ItemStack live = player.m_21120_(arm == player.m_5737_() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
        if (live == rendered || live.m_41619_() || !ItemStack.m_41656_(live, rendered) || live.m_41613_() != rendered.m_41613_()) {
            return;
        }
        cir.setReturnValue(live);
        cir.cancel();
        if (!dontpunchmytacz$reported) {
            dontpunchmytacz$reported = true;
            DontPunchMyTacz.LOGGER.info("Kept the held stack live for Punchy");
        }
    }
}
