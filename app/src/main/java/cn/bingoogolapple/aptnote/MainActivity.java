package cn.bingoogolapple.aptnote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import cn.bingoogolapple.aptnote.annotation.ExecutableAnnotation;
import cn.bingoogolapple.aptnote.annotation.TypeAnnotation;
import cn.bingoogolapple.aptnote.annotation.VariableAnnotation;
import cn.bingoogolapple.aptnote.annotation.ioc.BindView;
import cn.bingoogolapple.aptnote.api.ViewInjector;

@TypeAnnotation("测试TypeAnnotation")
public class MainActivity extends AppCompatActivity {
    @VariableAnnotation("测试VariableAnnotation1")
    @BindView(R.id.tv_main_test)
    TextView mTestTv;

    @VariableAnnotation("测试VariableAnnotation2")
    @BindView(R.id.et_main_test)
    EditText mTestEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInjector.injectView(this);
        mTestTv.setText("Annotation Processor");
        mTestEt.setText("Annotation Processor");
    }

    @ExecutableAnnotation("测试ExecutableAnnotation")
    public void test(TextView testTv, Long l1, long l2) {
    }
}
