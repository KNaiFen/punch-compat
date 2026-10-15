import java.io.File;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.repository.Pack;

/** Exercise Minecraft's actual resource-pack info loader against the finished JAR. */
public final class PackMetadataCheck {
    public static void main(String[] args) {
        Pack.Info info = Pack.m_246334_("mod:punchy_realcamera_compat",
                name -> new FilePackResources(name, new File(args[0]), false));
        if (info == null || info.resourceFormat() != 15) {
            throw new AssertionError("Missing or invalid Minecraft 1.20.1 pack metadata");
        }
        System.out.println("PASS: Minecraft loaded Pack.Info from the finished JAR, pack_format=" + info.resourceFormat());
    }
}
