package dev.punchcompat;

import java.util.List;
import java.util.Set;
import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class CompatMixinPlugin implements IMixinConfigPlugin {
    @Override public boolean shouldApplyMixin(String target, String mixin) {
        var mods = LoadingModList.get();
        if (mods.getModFileById("punchy") == null) return false;
        if (mixin.endsWith("RealCameraMixin")) return mods.getModFileById("realcamera") != null;
        if (mixin.contains("Create")) return mods.getModFileById("create") != null;
        if (mixin.endsWith("HandEquipStateMachineMixin")) return mods.getModFileById("carryon") != null;
        return true;
    }
    @Override public void onLoad(String mixinPackage) {}
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> mine, Set<String> others) {}
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String name, ClassNode node, String mixin, IMixinInfo info) {}
    @Override public void postApply(String name, ClassNode node, String mixin, IMixinInfo info) {}
}
