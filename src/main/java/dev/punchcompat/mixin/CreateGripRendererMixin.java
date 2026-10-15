package dev.punchcompat.mixin;

import dev.punchcompat.CreateGripCompat;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "com.simibubi.create.content.equipment.extendoGrip.ExtendoGripItemRenderer", remap = false)
public abstract class CreateGripRendererMixin {
    @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true, require = 1)
    private ItemDisplayContext punchCompat$animateGrip(ItemDisplayContext context) {
        if (!CreateGripCompat.renderingGrip) return context;
        return context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                ? ItemDisplayContext.FIRST_PERSON_LEFT_HAND : ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
    }
}
