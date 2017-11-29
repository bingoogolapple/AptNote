Gradle 3.x 插件，apt 打包上传学习笔记
============

### 发布到本地 repo 私服

* compiler 中用 implementation 来依赖 annotation
  * 开发时 implementation project(':annotation')
  * 打包时 implementation "cn.bingoogolapple:bga-aptnote-annotation:${VERSION_NAME}"
  * compiler 中的 auto-service 和 javapoet 需要用 implementation 来引入
  * implementation 来依赖打包后生成的 pom 文件中是 scope 是 runtime
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

### 发布到 jcenter

* compiler 中用 implementation 来依赖 annotation
  * 开发时 implementation project(':annotation')
  * 打包时 implementation "cn.bingoogolapple:bga-aptnote-annotation:${VERSION_NAME}"
  * compiler 中的 auto-service 和 javapoet 需要用 implementation 来引入
  * implementation 来依赖打包后生成的 pom 文件中是 scope 是 runtime
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

* compiler 中用 implementation
* api 中用 compile
* 依赖 support 时用 compileOnly
* 先发布 annotation，然后将 api 和 compiler 中对 annotation 的依赖改为依赖仓库里的，最后再发布 api 或 compiler