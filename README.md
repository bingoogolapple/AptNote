Gradle 3.x 插件，apt 打包上传学习笔记
============

## 发布到本地 repo 和 nexus 私服

* compiler 中用 implementation 来依赖 annotation
  * 开发时 implementation project(':annotation')
  * 打包时 implementation "cn.bingoogolapple:bga-aptnote-annotation:${VERSION_NAME}"
  * compiler 中的 auto-service 和 javapoet 需要用 implementation 来引入
  * implementation 来依赖打包后生成的 pom 文件中是 scope 是「runtime」
* api 中用 api 来依赖 annotation
  * 开发时 api project(':annotation')
  * 打包时 api "cn.bingoogolapple:bga-aptnote-annotation:${VERSION_NAME}"
  * 用 compile 来依赖打包后生成的 pom 文件中 annotation 的 scope 是「compile」
    * 在 app 中使用时，如果末尾不加 @aar，会传递依赖 annotation
  * 用 api 来依赖打包后生成的 pom 文件中 scope 是「compile」
    * 在 app 中使用时，如果末尾不加 @aar，会传递依赖 annotation
  * 用 implementation 来依赖打包后生成的 pom 文件中 scope 是「compile」
    * 在 app 中使用时，如果末尾不加 @aar，会传递依赖 annotation「但在开发期间需要 app 单独在添加 annotation 的依赖」
  * compileOnly 来依赖 support 包，打包后生成的 pom 文件中不会有对应的依赖
* 上传的先后顺序
  * 先 annotation，然后 api 或 compiler

### 结论

* 依赖 support 时都用 compileOnly
* compiler 中用 implementation 来依赖其他模块
* api 中用 api 来依赖其他模块
* 先发布 annotation，然后将 api 和 compiler 中对 annotation 的依赖改为依赖仓库里的，最后再发布 api 或 compiler
* 开发期间，api 会传递依赖，implementation 不会传递依赖；发布后 api 和 implementation 都会传递依赖
* 场景：A 依赖 B，B 依赖 C
  * 如果想 A 不能直接使用 C，那么开发期间和发布时 B 依赖 C 都用 implementation（发布后 A 还是能直接使用 C 的，只是开发期间 A 不能直接使用 C，因为发布后 pom 中依赖 C 的节点都是 compile）
  * 如果想 A 能直接使用 C，那么开发期间和发布时 B 依赖 C 都用 api
  * Android 模块依赖其他模块时，用 api 和 implementation 最终生成的 pom 中的依赖节点都是 compile
  * Java 模块依赖其他模块时，用 implementation 最终生成的 pom 中的依赖节点是 runtime

## 发布到 jcenter

* compiler 中用 implementation 来依赖 annotation
  * 开发时 implementation project(':annotation')
  * 打包时 implementation "cn.bingoogolapple:bga-aptnote-annotation:${VERSION_NAME}"
  * compiler 中的 auto-service 和 javapoet 需要用 implementation 来引入
  * implementation 来依赖打包后生成的 pom 文件中是 scope 是「runtime」
* api 中用 compile 来依赖 annotation
  * 开发时 compile project(':annotation')
  * 打包时 compile "cn.bingoogolapple:bga-aptnote-annotation:${VERSION_NAME}"
  * 用 compile 来依赖打包后生成的 pom 文件中 annotation 的 scope 是「runtime」
    * 在 app 中使用时，如果末尾不加 @aar，会传递依赖 annotation
  * 用 api 来依赖打包后生成的 pom 文件中「没有 annotation」
    * 在 app 中需要单独添加 annotation 的依赖
  * 用 implementation 来依赖打包后生成的 pom 文件中「没有 annotation」
    * 在 app 中需要单独添加 annotation 的依赖
  * compileOnly 来依赖 support 包，打包后生成的 pom 文件中不会有对应的依赖
* 上传的先后顺序
  * 先发布 annotation，然后将 api 和 compiler 中对 annotation 的依赖改为依赖仓库里的，最后再发布 api 或 compiler

### 结论

* 依赖 support 时用 compileOnly
* compiler 中用 implementation
* api 中用 compile
* 先发布 annotation，然后将 api 和 compiler 中对 annotation 的依赖改为依赖仓库里的，最后再发布 api 或 compiler
* 场景：A 依赖 B，B 依赖 C
  * 如果想 A 不能直接使用 C，那么开发期间 B 依赖 C 用 implementation，发布时 B 依赖 C 用 compile
  * 如果想 A 能直接使用 C，那么开发期间 B 依赖 C 用 api，发布时 B 依赖 C 用 compile
  * Android 模块依赖其他模块时，用 api 和 implementation 最终生成的 pom 中都没有依赖节点
  * Java 模块依赖其他模块时，用 implementation 最终生成的 pom 中的依赖节点是 runtime

## 作者联系方式

| 个人主页 | 邮箱 |
| ------------- | ------------ |
| <a  href="https://www.bingoogolapple.cn" target="_blank">bingoogolapple.cn</a>  | <a href="mailto:bingoogolapple@gmail.com" target="_blank">bingoogolapple@gmail.com</a> |

| 个人微信号 | 微信群 | 公众号 |
| ------------ | ------------ | ------------ |
| <img width="180" alt="个人微信号" src="https://github.com/bingoogolapple/bga-god-assistant-config/raw/main/images/BGAQrCode.png"> | <img width="180" alt="微信群" src="https://github.com/bingoogolapple/bga-god-assistant-config/raw/main/images/WeChatGroup1QrCode.jpg"> | <img width="180" alt="公众号" src="https://github.com/bingoogolapple/bga-god-assistant-config/raw/main/images/GongZhongHao.png"> |

| 个人 QQ 号 | QQ 群 |
| ------------ | ------------ |
| <img width="180" alt="个人 QQ 号" src="https://github.com/bingoogolapple/bga-god-assistant-config/raw/main/images/BGAQQQrCode.jpg"> | <img width="180" alt="QQ 群" src="https://github.com/bingoogolapple/bga-god-assistant-config/raw/main/images/QQGroup1QrCode.jpg"> |

## 打赏支持作者

如果您觉得 BGA 系列开源库或工具软件帮您节省了大量的开发时间，可以扫描下方的二维码打赏支持。您的支持将鼓励我继续创作，打赏后还可以加我微信免费开通一年 [上帝小助手浏览器扩展/插件开发平台](https://github.com/bingoogolapple/bga-god-assistant-config) 的会员服务

| 微信 | QQ | 支付宝 |
| ------------- | ------------- | ------------- |
| <img width="180" alt="微信" src="https://github.com/bingoogolapple/bga-god-assistant-config/raw/main/images/donate-wechat.jpg"> | <img width="180" alt="QQ" src="https://github.com/bingoogolapple/bga-god-assistant-config/raw/main/images/donate-qq.jpg"> | <img width="180" alt="支付宝" src="https://github.com/bingoogolapple/bga-god-assistant-config/raw/main/images/donate-alipay.jpg"> |

## 作者项目推荐

* 欢迎您使用我开发的第一个独立开发软件产品 [上帝小助手浏览器扩展/插件开发平台](https://github.com/bingoogolapple/bga-god-assistant-config)
