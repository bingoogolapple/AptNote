Gradle 3.x 插件，apt 打包上传学习笔记
============

### 发布到本地 maven 私服

* compiler 中用 implementation 来依赖 annotation
  * 开发时 implementation project(':annotation')
  * 打包时 implementation "cn.bingoogolapple:bga-aptnote-annotation:${VERSION_NAME}"
  * auto-service 和 javapoet 需要用 implementation 来引入
  * implementation 来依赖打包后生成的 pom 文件中是 scope 是 runtime
* api 中用 api 来依赖 annotation
  * 开发时 api project(':annotation')
  * 打包时 api "cn.bingoogolapple:bga-aptnote-annotation:${VERSION_NAME}"
  * api 来依赖打包后生成的 pom 文件中 scope 时 compile
  * 具体项目中使用时末尾别加 @aar，会传递依赖 annotation
  * compileOnly 来依赖 support 包，打包后生成的 pom 文件中不会有对应的依赖
* 上传的先后顺序
  *