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
* 非 apt 场景：A 依赖 B，B 依赖 C
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
