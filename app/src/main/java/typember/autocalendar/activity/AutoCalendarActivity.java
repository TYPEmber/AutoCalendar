package typember.autocalendar.activity;


/**
 * Created by nono0 on 2016/11/22.
 */

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.google.android.gms.vision.text.Text;
import com.google.api.client.util.DateTime;

import typember.autocalendar.tool.Constant;
import typember.autocalendar.tool.HTTPRequest;
import typember.autocalendar.tool.IResponse;
import typember.autocalendar.widget.AutoExpandLinearLayout;

import android.app.TimePickerDialog;

import typember.autocalendar.widget.ExpandWordView;
import typember.autocalendar.R;

import static typember.autocalendar.activity.SettingActivity.forAPI;


public class AutoCalendarActivity extends Activity implements View.OnClickListener {
    AutoExpandLinearLayout mAutoLayout;
    Button mCopyBtn;
    Button mConfirmBtn;
    Button mCancelBtn;
    Button mSummaryBtn;
    Button mLocationBtn;
    Button mDescriptionBtn;
    TextView tStartTimeYMD;
    TextView tStartTimeHMS;
    TextView tEndTimeYMD;
    TextView tEndTimeHMS;
    TextView tNotificationTime;
    TextView tNotificationSTime;
    EditText tSummary;
    EditText tLocation;
    EditText tDescription;

    public static boolean isShowing = false;


    @Override
    protected void onStart() {
        super.onStart();
        //isShowing = true;
    }

    @Override
    protected void onStop() {
        //isShowing = false;
        super.onStop();
    }

    // 定义5个记录当前时间的变量
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    // 定义在EditText中显示当前日期、时间的方法

    /*public String IntToString(int i){
        String s = String.valueOf(i);
        return s;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand);
        registerReceiver(receiver, new IntentFilter(Constant.FINISH_EXPAND_ACTIVITY));
        mAutoLayout = (AutoExpandLinearLayout) findViewById(R.id.auto_layout);
        mCopyBtn = (Button) findViewById(R.id.btn_copy);
        mConfirmBtn = (Button) findViewById(R.id.btn_confirm);
        mCancelBtn = (Button) findViewById(R.id.btn_cancel);
        mSummaryBtn = (Button) findViewById(R.id.btn_summary);
        mLocationBtn = (Button) findViewById(R.id.btn_location);
        mDescriptionBtn = (Button) findViewById(R.id.btn_description);
        tStartTimeYMD = (TextView) findViewById(R.id.startYMDValue);
        tStartTimeHMS = (TextView) findViewById(R.id.startHMSValue);
        tEndTimeYMD = (TextView) findViewById(R.id.endYMDValue);
        tEndTimeHMS = (TextView) findViewById(R.id.endHMSValue);
        tSummary = (EditText) findViewById(R.id.summaryValue);
        tLocation = (EditText) findViewById(R.id.locationValue);
        tDescription = (EditText) findViewById(R.id.descriptionValue);
        tNotificationTime = (TextView) findViewById(R.id.notificationValue);
        tNotificationSTime = (TextView) findViewById(R.id.notificationSValue);
        ((TextView) findViewById(R.id.id_of_TextView)).requestFocus();

        mCopyBtn.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        mSummaryBtn.setOnClickListener(this);
        mLocationBtn.setOnClickListener(this);
        mDescriptionBtn.setOnClickListener(this);

        Calendar c = Calendar.getInstance();//默认时间相关设置
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        char[] YMD = new char[10];
        char[] HMS = new char[8];
        //char[] ZONE = new char[6];
        DateTime now = new DateTime(System.currentTimeMillis());
        String nowBuffer = String.valueOf(now);
        nowBuffer.getChars(0, 10, YMD, 0);
        nowBuffer.getChars(11, 19, HMS, 0);
        //nowBuffer.getChars(23,29,ZONE,0);//可用于改进自动选择时区

        tStartTimeYMD.setText(new String(YMD));
        tEndTimeYMD.setText(new String(YMD));
        tStartTimeHMS.setText(new String(HMS));
        tEndTimeHMS.setText(new String(HMS));
        tNotificationTime.setText("30");
        tNotificationSTime.setText("10");

        String text = getIntent().getStringExtra(Constant.CLIPBOARD_TEXT);
        requestServe(text);

        /*DatePicker datePicker = (DatePicker)
                findViewById(R.id.datePicker);
        //TimePicker timePicker = (TimePicker)
         //       findViewById(R.id.timePicker);
        // 获取当前的年、月、日、小时、分钟
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        // 初始化DatePicker组件，初始化时指定监听器
        datePicker.init(year, month, day, new OnDateChangedListener()
        {

            @Override
            public void onDateChanged(DatePicker arg0, int year
                    , int month, int day)
            {
                AutoCalendarActivity.this.year = year;
                AutoCalendarActivity.this.month = month;
                AutoCalendarActivity.this.day = day;
                // 显示当前日期、时间
                Toast.makeText(AutoCalendarActivity.this,"您选择的日期："+year+"年  "
                        +month+"月  "+day+"日", Toast.LENGTH_SHORT).show();
            }
        });*/


        //Button dateBn = (Button)findViewById(R.id.btn_setStartTime);
        //Button timeBn = (Button)findViewById(R.id.timeBn);
        //为“设置日期”按钮绑定监听器。

        tStartTimeYMD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Calendar c = Calendar.getInstance();

                // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
                new DatePickerDialog(AutoCalendarActivity.this,
                        // 绑定监听器
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker dp, int year,
                                                  int month, int dayOfMonth) {
                                month += 1;
                                String Month = "-mm";
                                String Day = "-dd";
                                if (month < 10)
                                    Month = "-0" + String.valueOf(month);
                                else
                                    Month = "-" + String.valueOf(month);
                                if (dayOfMonth < 10)
                                    Day = "-0" + String.valueOf(dayOfMonth);
                                else
                                    Day = "-" + String.valueOf(dayOfMonth);
                            tStartTimeYMD.setText(year+Month+Day);
                            tEndTimeYMD.setText(year+Month+Day);

                        }
            }
            //设置初始日期
                        ,c.get(Calendar.YEAR)
                        ,c.get(Calendar.MONTH)
                        ,c.get(Calendar.DAY_OF_MONTH)).

            show();
        }
    }

    );
    //为“设置时间”按钮绑定监听器。
    tStartTimeHMS.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View source){
        Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来。
        new TimePickerDialog(AutoCalendarActivity.this,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int hourOfDay,
                                          int minute) {
                        String Hour = "-hh";
                        String Minute = "-mm";
                        if (hourOfDay < 10)
                            Hour = "0" + String.valueOf(hourOfDay);
                        else
                            Hour = String.valueOf(hourOfDay);
                        if (minute < 10)
                            Minute = ":0" + String.valueOf(minute);
                        else
                            Minute = ":" + String.valueOf(minute);
                        tStartTimeHMS.setText(Hour+Minute+":00");
                        tEndTimeHMS.setText(Hour+Minute+":01");
                    }
                }
                //设置初始时间
                , c.get(Calendar.HOUR_OF_DAY)
                , c.get(Calendar.MINUTE)
                //true表示采用24小时制
                , true).show();
    }
    }

    );
    //为“设置日期”按钮绑定监听器。
    tEndTimeYMD.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View source){
        Calendar c = Calendar.getInstance();
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(AutoCalendarActivity.this,
                // 绑定监听器
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker dp, int year,
                                          int month, int dayOfMonth) {
                        month += 1;
                        String Month = "-mm";
                        String Day = "-dd";
                        if (month < 10)
                            Month = "-0" + String.valueOf(month);
                        else
                            Month = "-" + String.valueOf(month);
                        if (dayOfMonth < 10)
                            Day = "-0" + String.valueOf(dayOfMonth);
                        else
                            Day = "-" + String.valueOf(dayOfMonth);
                        tEndTimeYMD.setText(year+Month+Day);
                    }
                }
                //设置初始日期
                , c.get(Calendar.YEAR)
                , c.get(Calendar.MONTH)
                , c.get(Calendar.DAY_OF_MONTH)).show();
    }
    }

    );
    //为“设置时间”按钮绑定监听器。
    tEndTimeHMS.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View source){
        Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来。
        new TimePickerDialog(AutoCalendarActivity.this,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int hourOfDay,
                                          int minute) {
                        String Hour = "-hh";
                        String Minute = "-mm";
                        if (hourOfDay < 10)
                            Hour = "0" + String.valueOf(hourOfDay);
                        else
                            Hour = String.valueOf(hourOfDay);
                        if (minute < 10)
                            Minute = ":0" + String.valueOf(minute);
                        else
                            Minute = ":" + String.valueOf(minute);
                        tEndTimeHMS.setText(Hour+Minute+":00");
                    }
                }
                //设置初始时间
                , c.get(Calendar.HOUR_OF_DAY)
                , c.get(Calendar.MINUTE)
                //true表示采用24小时制
                , true).show();
    }
    }

    );


    //forAPI=this;//为识别apiKey建立
}

    //public static AutoCalendarActivity forAPI;


    /**
     * 监听RadioGroup
     */
/*
    RadioGroup apiKeyGroup = (RadioGroup)findViewById(R.id.api_group);
    RadioButton apiKey0 = (RadioButton)findViewById(R.id.apiKey0);
    RadioButton apiKey1 = (RadioButton)findViewById(R.id.apiKey1);
    apiKeyGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            if(femaleButton.getId() == checkedId){
                System.out.println("famale");
                Toast.makeText(RadioTest.this, "famle", Toast.LENGTH_SHORT).show();
            }
            else if(maleButton.getId() == checkedId)
            {
                System.out.println("male");
            }
        }
    });
*/
    //public int bufferAPIkey = 0;

    /*public int APIkey_select() {//检测apiKey选择按钮

        return bufferAPIkey;
    }*/

    protected void requestServe(String text) {
        HTTPRequest.getSplitChar(text, new IResponse() {
            @Override
            public void finish(final String[] words) {
                if (words != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int count = 0;
                            for (String word : words) {
                                ExpandWordView expandWordView = new ExpandWordView(getApplication(), word, count);
                                count++;
                                mAutoLayout.addView(expandWordView);
                            }
                        }
                    });
                }
            }

            @Override
            public void failure(final String errorMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    Intent intent = new Intent();

    @Override
    public void onClick(View view) {
        String str = null;
        int count = mAutoLayout.getChildCount();
        StringBuilder builder = new StringBuilder();
        switch (view.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_copy:
                for (int i = 0; i < count; i++) {
                    CheckBox checkBox = (CheckBox) mAutoLayout.getChildAt(i);
                    if (checkBox.isChecked()) {
                        builder.append(checkBox.getText());
                    }
                }
                str = builder.toString();
                if (TextUtils.isEmpty(str)) {
                    Toast.makeText(getApplicationContext(), "没有选中词组", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "[" + str + "] 已经复制到剪切板", Toast.LENGTH_SHORT).show();
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    clipboardManager.setText(str);
                }
                break;
            case R.id.btn_summary:
                for (int i = 0; i < count; i++) {
                    CheckBox checkBox = (CheckBox) mAutoLayout.getChildAt(i);
                    if (checkBox.isChecked()) {
                        builder.append(checkBox.getText());
                    }
                }

                str = builder.toString();
                if (TextUtils.isEmpty(str)) {
                    Toast.makeText(getApplicationContext(), "没有选中词组", Toast.LENGTH_SHORT).show();
                } else {
                    tSummary.setText(str);
                    for (int i = 0; i < count; i++) {
                        CheckBox checkBox = (CheckBox) mAutoLayout.getChildAt(i);
                        checkBox.setChecked(false);
                    }
                    //intent.putExtra("Summary",str);
                }
                break;
            case R.id.btn_location:
                for (int i = 0; i < count; i++) {
                    CheckBox checkBox = (CheckBox) mAutoLayout.getChildAt(i);
                    if (checkBox.isChecked()) {
                        builder.append(checkBox.getText());
                    }
                }

                str = builder.toString();
                if (TextUtils.isEmpty(str)) {
                    Toast.makeText(getApplicationContext(), "没有选中词组", Toast.LENGTH_SHORT).show();
                } else {
                    tLocation.setText(str);
                    for (int i = 0; i < count; i++) {
                        CheckBox checkBox = (CheckBox) mAutoLayout.getChildAt(i);
                        checkBox.setChecked(false);
                    }
                    //intent.putExtra("Location",str);
                }
                break;
            case R.id.btn_description:
                for (int i = 0; i < count; i++) {
                    CheckBox checkBox = (CheckBox) mAutoLayout.getChildAt(i);
                    if (checkBox.isChecked()) {
                        builder.append(checkBox.getText());
                    }
                }

                str = builder.toString();
                if (TextUtils.isEmpty(str)) {
                    Toast.makeText(getApplicationContext(), "没有选中词组", Toast.LENGTH_SHORT).show();
                } else {
                    tDescription.setText(str);
                    for (int i = 0; i < count; i++) {
                        CheckBox checkBox = (CheckBox) mAutoLayout.getChildAt(i);
                        checkBox.setChecked(false);
                    }
                    //intent.putExtra("Description",str);
                }
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(tSummary.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "至少填写日程名称", Toast.LENGTH_SHORT).show();
                } else {
                    intent.putExtra("Summary", tSummary.getText().toString());
                    intent.putExtra("Location", tLocation.getText().toString());
                    intent.putExtra("Description", tDescription.getText().toString());
                    intent.putExtra("startTimeYMD", tStartTimeYMD.getText().toString());
                    intent.putExtra("startTimeHMS", tStartTimeHMS.getText().toString());
                    intent.putExtra("endTimeYMD", tEndTimeYMD.getText().toString());
                    intent.putExtra("endTimeHMS", tEndTimeHMS.getText().toString());

                    /*Double bufferS = Double.parseDouble(tStartTimeYMD.getText().toString() + tStartTimeHMS.getText().toString());
                    Double bufferE = Double.parseDouble(tEndTimeYMD.getText().toString() + tEndTimeHMS.getText().toString());*/
                    String bufferS = tStartTimeYMD.getText().toString() + tStartTimeHMS.getText().toString();
                    String bufferE = tEndTimeYMD.getText().toString() + tEndTimeHMS.getText().toString();
                    boolean buffer = false;
                    for (int i = 0; i < bufferE.length(); i++){
                        if(bufferE.charAt(i)-bufferS.charAt(i) > 0) {
                            buffer = true;
                            break;
                        }
                    }
                    if (buffer) {
                        intent.putExtra("notificationTime", tNotificationTime.getText().toString());
                        intent.putExtra("notificationSTime", tNotificationSTime.getText().toString());
                        intent.setClass(AutoCalendarActivity.this, LogInActivity.class);
                        if (!forAPI.GoogleAPICalled)//forAPI.GoogleAPICalled在没调用API之前为true，调用后一定变为false
                            LogInActivity.instance.finish();//用以避免重复建立日程
                        forAPI.GoogleAPICalled = true;
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "结束时间需要晚于开始时间", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };
}
