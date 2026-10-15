# 来源与致谢

本项目是社区维护的独立兼容补丁，不是以下项目的官方版本。修改日期：2026-10-16。

- **lloy9d / Don't Punch My TACZ**：https://github.com/lloy9d/dont-punch-my-tacz 。基于 `forge/1.20.1` 的 `2a988cb`（0.5.2）移植物品兼容列表、Carry On、隐身、盔甲袖子、弩和持有物品修复。保留 `dev.dontpunchmytacz` 来源包名；修改初始化、黑名单迁移、可选模组检查及错误处理。本项目整体采用 GPL-3.0-only，见 LICENSE。
- **Punchy**：提供第一人称动画与握持点接口。仅链接用户安装的 Punchy 2.8f，不打包或再分发其代码和资产。
- **xTracr / RealCamera**：https://github.com/xTracr/RealCamera 。复用公开的 `DisableHelper.RENDER_HANDS` 判断；合并此前本地制作的 Punchy RealCamera Compat 1.0.1。感谢其真实第一人称视角实现。
- **Create Team / Create**：https://github.com/Creators-of-Create/Create 。研究 1.20.1 / 6.0.8 的 `ExtendoGripRenderHandler` 和 `ExtendoGripItemRenderer`，机械手伸缩与末端持物的数值和变换改编自其 MIT 授权代码。模型和纹理完全由用户安装的 Create 提供。

Create 代码的 MIT 许可：

Copyright (c) The Create Team / The Creators of Create

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
