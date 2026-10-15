# Punch Compat

Minecraft **1.20.1 / Forge** 的客户端兼容补丁。将 Don't Punch My TACZ 0.5.2 的兼容与修复、Punchy RealCamera Compat 1.0.1，以及新的 Create 机械手兼容合并到一个 MOD。

这是独立社区分支，不是 Punchy、Create 或上游兼容项目的官方发布。

## 功能

- **TACZ 等物品兼容**：继承上游的枪械与特殊物品排除列表，以及 Carry On、隐身手臂、盔甲袖子、弩快速装填和持有物品状态修复。
- **RealCamera**：在 RealCamera 决定隐藏第一人称手部时，先于 Punchy 绘制取消渲染；关闭相机模式后恢复原有行为。
- **Create 机械手**：启用 Punchy 时，机械手使用 Punchy 当前动画握持点，取消 Create 额外绘制的原版手臂。主手、仅副手及双持均保留 Create 的主手操作方式；副手装备时主手物品出现在机械手末端，双持时第二把机械手接在第一把末端。伸缩动作仍由 Create 提供。支持左右惯用手。
- **配置迁移**：首次客户端初始化时通过 Punchy 的配置 API 保留原有设置、迁移兼容列表，并去掉旧兼容补丁加入的 `create:extendo_grip` 排除项。此后如需让某个物品继续使用原版渲染，可在 Punchy 中重新排除它。

关闭或移除 Punchy 后，机械手交回 Create 原有的第一人称渲染。**FA Player / EMF 兼容独立提供在 `create-faplayer-compat` MOD 中**；本 MOD 不依赖 FA、EMF 或它的补丁。

## 版本

| 组件 | 本版针对的版本 |
| --- | --- |
| Minecraft | 1.20.1 |
| Forge | 47.x，构建与字节码检查使用 47.4.13 |
| Punchy | 2.8f |
| RealCamera（可选） | 0.7.8-beta |
| Create（可选） | 6.0.8 |

Punchy、Create、RealCamera 缺席时，相应 Mixin 不加载。当前版本限定了相关 MOD 版本，避免在不同渲染实现上静默失效。TACZ、Carry On 及列表里的其他 MOD 均为可选。

## 安装

1. 完全退出游戏。
2. 移走或禁用旧的 `dont-punch-my-tacz-*.jar` 和 `punchy-realcamera-compat-*.jar`，避免重复注入。
3. 把 `punch-compat-1.0.0-forge-1.20.1.jar` 放入客户端 `mods`。
4. 启动游戏，确认 MOD 列表有 **Punch Compat 1.0.0**。

服务器无需安装。升级前建议保留 `config/punchy/punchy_config.json` 的备份，以便连同旧 JAR 一起恢复。

## 源码构建

需要 Python 3.11+、JDK 17+（`java` 与 `javac` 在 PATH），以及已安装上述版本的 Forge 1.20.1 游戏实例。将实例目录作为参数：

```bash
python build.py "/path/to/.minecraft/versions/YourInstance"
python verify.py "/path/to/.minecraft/versions/YourInstance"
```

构建输出在 `build/`。构建脚本只读取现有依赖，不修改游戏目录、不下载和不打包第三方 MOD。实例 JSON 与目录名应一致；它的上两级应为包含 `libraries/` 的 `.minecraft`。依赖 JAR 文件名需含版本标识。源码直接使用 Forge 1.20.1 生产环境的 SRG 名称，使用 `javac --release 17` 编译，不再经过重映射；请勿把它直接当作 Mojmap 源码放入默认 ForgeGradle 工程。

可与独立 FA 补丁联合检查：

```bash
python verify.py "/path/to/instance" --fa-project ../create-faplayer-compat
```

## 验证范围

已验证：成品 JAR 由 Minecraft/Forge 实际资源包读取器生成有效的 `Pack.Info`（格式 15）；将实际安装的 Punchy、RealCamera、Create 字节码放入 Mixin 0.8.5，验证全部注入点、RealCamera 取消顺序、空主手时的握持点注入，以及与独立 FA 补丁同时应用。

这些是离线集成检查，**不等于游戏已启动或画面已验收**。实机还需检查：主手、仅副手、双持、主手拿工具／方块、左右惯用手、行走／挥动、切换物品、开关 Punchy、开关 RealCamera。第三方自定义枪械等特殊物品继续沿用原来的排除路径。

验证宿主使用旧 LaunchWrapper 初始化 Mixin，会打印 `MixinBootstrap.doInit()` 和 Java 兼容级别提示；成功以程序退出码和 PASS 断言为准，游戏运行时使用 Forge 的 ModLauncher。

## 来源与许可

整体 **GPL-3.0-only**。感谢 [lloy9d / Don't Punch My TACZ](https://github.com/lloy9d/dont-punch-my-tacz)、Punchy、[xTracr / RealCamera](https://github.com/xTracr/RealCamera)、[Create Team](https://github.com/Creators-of-Create/Create)。具体来源、提交、修改说明和 Create 代码许可见 [NOTICE.md](NOTICE.md)；完整 GPL 见 [LICENSE](LICENSE)。所有第三方模型、纹理及 MOD JAR 由用户自行安装，未随本项目再分发。
