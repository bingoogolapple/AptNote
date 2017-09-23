### 在生成的代码中一定要慎重出现编译时注解，把控好代码逻辑，避免出现死循环

从输出 process 方法日志可见多扫描了一次

```
注: ====================================== ExecutableProcessor init ======================================
注: ====================================== ExecutableProcessor getSupportedSourceVersion ======================================
注: ====================================== ExecutableProcessor getSupportedAnnotationTypes ======================================
注: ====================================== ExecutableProcessor process START ======================================
注: ExecutableProcessor : packageName = cn.bingoogolapple.aptnote
注: ExecutableProcessor : className = MainActivity
注: ExecutableProcessor : fullClassName = cn.bingoogolapple.aptnote.MainActivity
注: ExecutableProcessor : superClassName = android.support.v7.app.AppCompatActivity
注: ExecutableProcessor : value = 测试ExecutableAnnotation
注: ExecutableProcessor : methodName = test
注: ExecutableProcessor : parameterName = testTv
注: ExecutableProcessor : parameterKind = android.widget.TextView
注: ExecutableProcessor : parameterName = l1
注: ExecutableProcessor : parameterKind = java.lang.Long
注: ExecutableProcessor : parameterName = l2
注: ExecutableProcessor : parameterKind = long
注: ====================================== ExecutableProcessor process END ======================================
注: ====================================== TypeProcessor init ======================================
注: ====================================== TypeProcessor getSupportedSourceVersion ======================================
注: ====================================== TypeProcessor getSupportedAnnotationTypes ======================================
注: ====================================== TypeProcessor process END ======================================
警告: TypeProcessor : packageName = cn.bingoogolapple.aptnote
警告: TypeProcessor : className = MainActivity
警告: TypeProcessor : fullClassName = cn.bingoogolapple.aptnote.MainActivity
注: TypeProcessor : superClassName = android.support.v7.app.AppCompatActivity
注: TypeProcessor : value = 测试TypeAnnotation
注: ====================================== TypeProcessor process END ======================================
注: ====================================== VariableProcessor init ======================================
注: ====================================== VariableProcessor getSupportedSourceVersion ======================================
注: ====================================== VariableProcessor getSupportedAnnotationTypes ======================================
注: ====================================== VariableProcessor process END ======================================
注: VariableProcessor : packageName = cn.bingoogolapple.aptnote
注: VariableProcessor : className = MainActivity
注: VariableProcessor : fullClassName = cn.bingoogolapple.aptnote.MainActivity
注: VariableProcessor : superClassName = android.support.v7.app.AppCompatActivity
注: VariableProcessor : value = 测试VariableAnnotation1
注: VariableProcessor : variableName = mTestTv
注: VariableProcessor : type = android.widget.TextView
注: VariableProcessor : packageName = cn.bingoogolapple.aptnote
注: VariableProcessor : className = MainActivity
注: VariableProcessor : fullClassName = cn.bingoogolapple.aptnote.MainActivity
注: VariableProcessor : superClassName = android.support.v7.app.AppCompatActivity
注: VariableProcessor : value = 测试VariableAnnotation2
注: VariableProcessor : variableName = mTestEt
注: VariableProcessor : type = android.widget.EditText
注: ====================================== VariableProcessor process END ======================================
注: ====================================== ExecutableProcessor process START ======================================
注: ====================================== ExecutableProcessor process END ======================================
注: ====================================== TypeProcessor process END ======================================
注: ====================================== TypeProcessor process END ======================================
注: ====================================== VariableProcessor process END ======================================
注: ====================================== VariableProcessor process END ======================================
```

* APT 可以扫描源码中的所有注解，依据这些注解来生成代码
* 生成的代码中如果也有注解同样可以被扫描到，并且用于代码生成
    * APT 第一次扫描源码中的所有注解，扫描结束后生成代码，之后再扫描一次，以保证生成的代码中的注解也可以被扫描到
    * 第二次扫描到注解后继续生成代码，类似于递归一样的「扫描 - 代码生成 - 扫描 - 代码生成 - 扫描 - 代码生成」一直到扫描到的注解为0时停止
