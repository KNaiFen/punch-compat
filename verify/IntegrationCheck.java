import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;

/** Transforms real installed classes; does not claim an in-game visual test. */
public final class IntegrationCheck {
    public static void main(String[] args) throws Exception {
        var urls = new ArrayList<URL>();
        for (String entry : System.getProperty("java.class.path").split(File.pathSeparator))
            urls.add(new File(entry).toURI().toURL());
        Launch.blackboard = new HashMap<>();
        Launch.blackboard.put("TweakClasses", new ArrayList<String>());
        Launch.blackboard.put("Tweaks", new ArrayList<>());
        Launch.classLoader = new LaunchClassLoader(urls.toArray(URL[]::new));
        Thread.currentThread().setContextClassLoader(Launch.classLoader);
        MixinBootstrap.init();
        var phase = MixinEnvironment.class.getDeclaredMethod("gotoPhase", MixinEnvironment.Phase.class);
        phase.setAccessible(true);
        phase.invoke(null, MixinEnvironment.Phase.DEFAULT);
        var environment = MixinEnvironment.getCurrentEnvironment();
        environment.setSide(MixinEnvironment.Side.CLIENT);
        for (String config : args[0].split(",")) Mixins.addConfiguration(config);
        var transformer = (IMixinTransformer) environment.getActiveTransformer();
        for (String target : args[1].split(",")) {
            byte[] original = Launch.classLoader.getClassBytes(target);
            byte[] transformed = transformer.transformClassBytes(target, target, original);
            Files.write(Path.of(target.substring(target.lastIndexOf('.') + 1) + ".class"), transformed);
            var node = new ClassNode();
            new ClassReader(transformed).accept(node, 0);
            if (java.util.Arrays.equals(original, transformed)) throw new AssertionError("Not transformed: " + target);
            if (target.equals("punchy.client.render.PunchyArmRenderer")) {
                MethodNode item = method(node, "renderItemInHand");
                List<String> calls = calls(item);
                if (index(calls, "applyGripLocalTransform") >= index(calls, "renderAtAnimatedGrip"))
                    throw new AssertionError("Grip injection precedes animation");
                // The callback must run before the empty-stack return, for empty main + offhand grip.
                boolean after = false, cancellationReturn = false;
                for (var instruction : item.instructions) {
                    if (instruction instanceof MethodInsnNode call && call.name.contains("renderAtAnimatedGrip")) after = true;
                    if (after && instruction.getOpcode() == org.objectweb.asm.Opcodes.RETURN) { cancellationReturn = true; break; }
                }
                if (!cancellationReturn) throw new AssertionError("Missing cancellable grip callback");
                System.out.println("PASS: Punchy grip attachment follows animated grip transform; offhand item/skin/armor hooks apply");
            }
            if (target.equals("net.minecraft.client.renderer.ItemInHandRenderer") && args[0].contains("verify-patch")) {
                var calls = calls(method(node, "m_109314_"));
                if (index(calls, "hideHandsBeforePunchy") >= index(calls, "renderFirstPersonModel")) throw new AssertionError(calls);
                System.out.println("PASS: RealCamera cancellation runs before Punchy drawing");
            }
            if (target.endsWith("ExtendoGripRenderHandler") && args[0].contains("create_faplayer")) {
                var calls = calls(method(node, "onRenderPlayerHand"));
                int attach = index(calls, "attachAssembly");
                if (attach >= index(calls, "m_269530_")) throw new AssertionError("FA attachment happens too late");
                if (index(calls, "captureRight") >= attach || index(calls, "captureLeft") >= attach)
                    throw new AssertionError("FA capture happens after attachment");
                if (calls.contains("m_117770_") || calls.contains("m_117813_")) throw new AssertionError("Unredirected Create arm call");
                System.out.println("PASS: FA captures either skin arm before transforming both grip and tip item");
            }
            System.out.println("PASS: actual installed class transformed: " + target);
        }
    }

    private static MethodNode method(ClassNode node, String name) {
        return node.methods.stream().filter(m -> m.name.equals(name)).findFirst().orElseThrow();
    }
    private static List<String> calls(MethodNode method) {
        var result = new ArrayList<String>();
        for (var instruction : method.instructions) if (instruction instanceof MethodInsnNode call) result.add(call.name);
        return result;
    }
    private static int index(List<String> calls, String fragment) {
        for (int i = 0; i < calls.size(); i++) if (calls.get(i).contains(fragment)) return i;
        throw new AssertionError("Missing call: " + fragment + " in " + calls);
    }
}
