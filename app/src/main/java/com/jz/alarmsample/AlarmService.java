package com.jz.alarmsample;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;


/**
 * Description: 定时提醒服务类
 * Author: Jack Zhang
 * create on: 2019-05-28 11:34
 */
public class AlarmService extends Service
{
  public static final String ACTION = "com.jz.alarmsample.alarm";

  @Override
  public IBinder onBind(Intent intent)
  {
    return null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId)
  {
    Context context = getApplicationContext();
    long intervalMillis = intent.getLongExtra(AlarmManagerUtils.INTERVAL_MILLIS, 0);
    if (intervalMillis != 0)
      AlarmManagerUtils.setAlarmTime(context, System.currentTimeMillis() + intervalMillis, intent);
    Intent clockIntent = new Intent(context, ClockAlarmActivity.class);
    clockIntent.putExtra(AlarmManagerUtils.ID, intent.getIntExtra(AlarmManagerUtils.ID, 0));
    clockIntent.putExtra(AlarmManagerUtils.TIPS, intent.getStringExtra(AlarmManagerUtils.TIPS));
    clockIntent.putExtra(AlarmManagerUtils.SOUND_OR_VIBRATOR, intent.getIntExtra(AlarmManagerUtils.SOUND_OR_VIBRATOR, 0));
    clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(clockIntent);
    return super.onStartCommand(intent, flags, startId);
  }
}
