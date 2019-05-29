package com.jz.alarmsample;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
  private TextView mTextView;
  private int mHour;
  private int mMinute;
  private int mSoundOrVibrator;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mTextView = findViewById(R.id.textView);
    final Calendar calendar = Calendar.getInstance();

    // 选择时间
    findViewById(R.id.button).setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        new TimePickerDialog(MainActivity.this,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener()
                {
                  @Override
                  public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                  {
                    mHour = hourOfDay;
                    mMinute = minute;
                    mTextView.setText("您选择了" + hourOfDay + "时" + minute + "分");
                  }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                , true).show();
      }
    });

    // 是否震动
    Switch switchBtn = findViewById(R.id.switch_btn);
    switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
      {
        mSoundOrVibrator = isChecked ? 2 : 1;
      }
    });

    // 开启定时
    findViewById(R.id.button2).setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        AlarmManagerUtils.setAlarm(MainActivity.this, 0, mHour, mMinute, 0, 0, 0, "提醒时间到了", mSoundOrVibrator);
        Toast.makeText(MainActivity.this, "已开启定时", Toast.LENGTH_SHORT).show();
      }
    });

    // 取消定时
    findViewById(R.id.button3).setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        AlarmManagerUtils.cancelAlarm(MainActivity.this, AlarmService.ACTION, 0);
        Toast.makeText(MainActivity.this, "已取消定时", Toast.LENGTH_SHORT).show();
      }
    });
  }
}
