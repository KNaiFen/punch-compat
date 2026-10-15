"""Apply the production mixins to installed classes, without launching Minecraft."""
import argparse
import json
import os
from pathlib import Path
import subprocess

parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument("instance", type=Path)
parser.add_argument("--fa-project", type=Path)
args = parser.parse_args()
root = Path(__file__).resolve().parent
check = root / "build/verify"
check.mkdir(parents=True, exist_ok=True)
jar = root / "build/punch-compat-1.0.0-forge-1.20.1.jar"
libs = args.instance.resolve().parent.parent / "libraries"
classpath = [str(check), str(jar), (root / "build/classpath.txt").read_text(encoding="utf-8"),
             str(libs / "net/minecraft/launchwrapper/1.12/launchwrapper-1.12.jar")]
if args.fa_project:
    fa = args.fa_project.resolve()
    classpath += [str(fa / "build/create-faplayer-compat-1.0.0-forge-1.20.1.jar"),
                  (fa / "build/classpath.txt").read_text(encoding="utf-8")]
for name, package, mixins, refmap in [
    ("punchy", "punchy.mixin.client", ["ItemInHandRendererMixin", "ItemInHandRendererArmRedirectMixin"], "punchy.refmap.json"),
    ("realcamera", "com.xtracr.realcamera.mixin", ["MixinItemInHandRenderer"], "realcamera-common-refmap.json"),
]:
    (check / f"verify-{name}.json").write_text(json.dumps({
        "required": True, "minVersion": "0.8.5", "package": package,
        "compatibilityLevel": "JAVA_17", "client": mixins, "refmap": refmap,
        "injectors": {"defaultRequire": 1}
    }), encoding="utf-8")
for config, name in [("punch_compat.mixins.json", "patch"), ("dontpunchmytacz.mixins.json", "legacy")]:
    patch = json.loads((root / "src/main/resources" / config).read_text())
    # Forge discovery is not running in this isolated host; enable the exact shipped mixins explicitly.
    del patch["plugin"]
    (check / f"verify-{name}.json").write_text(json.dumps(patch), encoding="utf-8")
cp = os.pathsep.join(classpath)
subprocess.run(["javac", "--release", "17", "-encoding", "UTF-8", "-proc:none", "-cp", cp,
                "-d", str(check), *map(str, sorted((root / "verify").glob("*.java")))], check=True)
subprocess.run(["java", "-cp", cp, "PackMetadataCheck", str(jar)], cwd=check, check=True)
targets = ["net.minecraft.client.renderer.ItemInHandRenderer", "punchy.client.render.PunchyArmRenderer",
           "punchy.client.animation.PunchyAnimationManager", "punchy.client.state.HandEquipStateMachine",
           "com.simibubi.create.content.equipment.extendoGrip.ExtendoGripRenderHandler",
           "com.simibubi.create.content.equipment.extendoGrip.ExtendoGripItemRenderer"]
configs = "verify-punchy.json,verify-realcamera.json,verify-legacy.json,verify-patch.json"
subprocess.run(["java", "-cp", cp, "IntegrationCheck", configs, ",".join(targets)], cwd=check, check=True)
if args.fa_project:
    subprocess.run(["java", "-cp", cp, "PackMetadataCheck", str(fa / "build/create-faplayer-compat-1.0.0-forge-1.20.1.jar")], cwd=check, check=True)
    fa_targets = [targets[-2], "traben.entity_model_features.models.parts.EMFModelPart"]
    subprocess.run(["java", "-cp", cp, "IntegrationCheck", "create_faplayer_compat.mixins.json", ",".join(fa_targets)], cwd=check, check=True)
    subprocess.run(["java", "-cp", cp, "IntegrationCheck", configs + ",create_faplayer_compat.mixins.json",
                    ",".join(targets + fa_targets[-1:])], cwd=check, check=True)
