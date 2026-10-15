"""Build Forge 1.20.1 SRG sources with JDK 17+ and an installed game instance."""
import argparse
import hashlib
import json
import os
from pathlib import Path
import shutil
import subprocess
from zipfile import ZipFile, ZIP_DEFLATED

parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument("instance", type=Path)
args = parser.parse_args()
root = Path(__file__).resolve().parent
instance = args.instance.resolve()
libraries = instance.parent.parent / "libraries"
profile = json.loads((instance / (instance.name + ".json")).read_text(encoding="utf-8"))
forge = next(lib["name"].split(":")[2] for lib in profile["libraries"]
             if lib["name"].startswith("net.minecraftforge:fmlloader:"))
build = root / "build"
classes = build / "classes"
if classes.exists():
    shutil.rmtree(classes)
classes.mkdir(parents=True)
classpath = [libraries / f"net/minecraftforge/forge/{forge}/forge-{forge}-client.jar",
             libraries / f"net/minecraftforge/forge/{forge}/forge-{forge}-universal.jar",
             libraries / f"net/minecraftforge/fmlcore/{forge}/fmlcore-{forge}.jar",
             libraries / f"net/minecraftforge/fmlloader/{forge}/fmlloader-{forge}.jar",
             libraries / f"net/minecraftforge/javafmllanguage/{forge}/javafmllanguage-{forge}.jar",
             libraries / "net/minecraft/client/1.20.1-20230612.114412/client-1.20.1-20230612.114412-srg.jar"]
for lib in profile["libraries"]:
    artifact = lib.get("downloads", {}).get("artifact")
    if artifact and (libraries / artifact["path"]).is_file():
        classpath.append(libraries / artifact["path"])
for token in ["punchy-2.8f-", "realcamera-1.20.1-forge-0.7.8-beta", "create-1.20.1-6.0.8.jar"]:
    jars = [p for p in (instance / "mods").glob("*.jar*") if token in p.name]
    if len(jars) != 1:
        raise SystemExit(f"Expected exactly one dependency containing {token}: {jars}")
    classpath.extend(jars)
    with ZipFile(jars[0]) as archive:
        for name in archive.namelist():
            if name.startswith("META-INF/jarjar/") and name.endswith(".jar"):
                target = build / "deps" / Path(name).name
                target.parent.mkdir(exist_ok=True)
                target.write_bytes(archive.read(name))
                classpath.append(target)
cp = os.pathsep.join(map(str, classpath))
(build / "classpath.txt").write_text(cp, encoding="utf-8")
subprocess.run(["javac", "--release", "17", "-encoding", "UTF-8", "-proc:none",
                "-cp", cp, "-d", str(classes),
                *map(str, sorted((root / "src/main/java").rglob("*.java")))], check=True)
jar = build / "punch-compat-1.0.0-forge-1.20.1.jar"
with ZipFile(jar, "w", ZIP_DEFLATED) as archive:
    archive.writestr("META-INF/MANIFEST.MF", "Manifest-Version: 1.0\r\n"
                     "MixinConfigs: dontpunchmytacz.mixins.json,punch_compat.mixins.json\r\n\r\n")
    for directory in [classes, root / "src/main/resources"]:
        for path in sorted(directory.rglob("*")):
            if path.is_file():
                archive.write(path, path.relative_to(directory).as_posix())
    archive.write(root / "LICENSE", "META-INF/LICENSE")
    archive.write(root / "NOTICE.md", "META-INF/NOTICE.md")
print(jar)
print("SHA-256:", hashlib.sha256(jar.read_bytes()).hexdigest())
