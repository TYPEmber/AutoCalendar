package typember.autocalendar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.RadioButton;
import android.widget.RadioGroup;



import typember.autocalendar.tool.Constant;
import typember.autocalendar.AutoCalendarService;
import typember.autocalendar.R;


/**
 * Created by nono0 on 2016/11/22.
 */

public class SettingActivity extends Activity implements CompoundButton.OnCheckedChangeListener,View.OnClickListener{
    String TAG = getClass().getName();

    ViewGroup mRootView ;
    Switch mRunSwitch;
    Button mTestBtn;
    EditText mTestText;
    private RadioGroup apiKeyGroup;
    private RadioButton apiKey0;
    private RadioButton apiKey1;
    public int bufferAPIkey = 0;
    public boolean GoogleAPICalled = true;



    public static SettingActivity forAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRootView = (ViewGroup) findViewById(R.id.root_view);
        mRunSwitch = (Switch) findViewById(R.id.sw_run);
        mRunSwitch.setOnCheckedChangeListener(this);
        mRunSwitch.setChecked(true);
        mTestBtn = (Button)findViewById(R.id.test_ws);
        mTestBtn.setOnClickListener(this);
        mTestText = (EditText) findViewById(R.id.test_text);

        apiKeyGroup = (RadioGroup)findViewById(R.id.api_group);
        apiKey0 = (RadioButton)findViewById(R.id.apiKey0);
        apiKey1 = (RadioButton)findViewById(R.id.apiKey1);

        forAPI=this;//为识别apiKey建立

        apiKeyGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(apiKey0.getId() == checkedId){
                    bufferAPIkey = 0;
                }
                if(apiKey1.getId() == checkedId)
                {
                    bufferAPIkey = 1;
                }
            }
        });
    }



    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b){
            AutoCalendarActivity.isShowing = false;//控制剪贴板监听的开启与关闭
            startService(new Intent(this,AutoCalendarService.class));
        }else{
            AutoCalendarActivity.isShowing = true;
            stopService(new Intent(this,AutoCalendarService.class));
        }
    }

    @Override
    public void onClick(View view) {
        String text = mTestText.getText().toString();
        Intent intent = new Intent(getApplication(), AutoCalendarActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constant.CLIPBOARD_TEXT,text);
        startActivity(intent);
    }
}

