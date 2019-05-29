package com.jz.alarmsample;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;


import java.util.Calendar;

/**
 * 定时提醒工具类
 *
 * @author Jack Zhang
 * create at 2019-05-28 15:51
 */
public class AlarmManagerUtils
{
  public static final String ID = "ID";
  public static final String INTERVAL_MILLIS = "INTERVAL_MILLIS";
  public static final String SOUND_OR_VIBRATOR = "SOUND_OR_VIBRATOR";
  public static final String TIPS = "TIPS";

  /**
   * 设置定时提醒，供AlarmService使用
   *
   * @author Jack Zhang
   * create at 2019-05-28 15:53
   */
  public static void setAlarmTime(Context context, long timeInMillis, Intent intent)
  {
    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

//    PendingIntent sender = PendingIntent.getBroadcast(context, intent.getIntExtra("id", 0), intent, PendingIntent.FLAG_CANCEL_CURRENT);
    PendingIntent sender = PendingIntent.getService(context, intent.getIntExtra(ID, 0), intent, PendingIntent.FLAG_CANCEL_CURRENT);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
      am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, sender);
    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
    {
      AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(timeInMillis, sender);
      am.setAlarmClock(alarmClockInfo, sender);
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
      am.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, sender);
  }

  /**
   * @param flag            周期性时间间隔的标志,flag = 0 表示一次性的闹钟, flag = 1 表示每天提醒的闹钟(1天的时间间隔),flag = 2 表示按周每周提醒的闹钟（一周的周期性时间间隔）
   * @param hour            时
   * @param minute          分
   * @param second          秒
   * @param id              闹钟的id
   * @param week            week=0表示一次性闹钟或者按天的周期性闹钟，非0 的情况下是几就代表以周为周期性的周几的闹钟
   * @param tips            闹钟提示信息
   * @param soundOrVibrator 0表示只有震动提醒 1表示只有铃声提醒 2表示声音和震动都执行
   */
  @SuppressLint("ShortAlarm")
  public static void setAlarm(Context context, int flag, int hour, int minute, int second, int id, int week, String tips, int soundOrVibrator)
  {
    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    Calendar calendar = Calendar.getInstance();
    long intervalMillis = 0;
    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hour, minute, second);

    if (flag == 0)
      intervalMillis = 0;
    else if (flag == 1)
      intervalMillis = AlarmManager.INTERVAL_DAY;
    else if (flag == 2)
      intervalMillis = AlarmManager.INTERVAL_DAY * 7;

    Intent intent = new Intent(AlarmService.ACTION);
    intent.putExtra(ID, id);
    intent.putExtra(TIPS, tips);
    intent.putExtra(INTERVAL_MILLIS, intervalMillis);
    intent.putExtra(SOUND_OR_VIBRATOR, soundOrVibrator);

//    PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    PendingIntent sender = PendingIntent.getService(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);

    long time = calMethod(week, calendar.getTimeInMillis());
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
      am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, sender);
    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
    {
      AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(time, sender);
      am.setAlarmClock(alarmClockInfo, sender);
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
      am.setExact(AlarmManager.RTC_WAKEUP, time, sender);
    else
      am.setRepeating(AlarmManager.RTC_WAKEUP, time, intervalMillis, sender);// 可能存在不精确的问题
  }

  public static void cancelAlarm(Context context, String action, int id)
  {
    Intent intent = new Intent(action);
//    PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    PendingIntent pi = PendingIntent.getService(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    am.cancel(pi);
  }

  /**
   * triggerAtMillis 计算方法
   *
   * @param weekflag 传入的是周几
   * @param dateTime 传入的是时间戳（设置当天的年月日+从选择框拿来的时分秒）
   * @return 返回起始闹钟时间的时间戳
   */
  private static long calMethod(int weekflag, long dateTime)
  {
    long time = 0;
    //weekflag == 0表示是按天为周期性的时间间隔或者是一次行的，weekfalg非0时表示每周几的闹钟并以周为时间间隔
    if (weekflag != 0)
    {
      Calendar c = Calendar.getInstance();
      int week = c.get(Calendar.DAY_OF_WEEK);
      if (1 == week)
      {
        week = 7;
      } else if (2 == week)
      {
        week = 1;
      } else if (3 == week)
      {
        week = 2;
      } else if (4 == week)
      {
        week = 3;
      } else if (5 == week)
      {
        week = 4;
      } else if (6 == week)
      {
        week = 5;
      } else if (7 == week)
      {
        week = 6;
      }

      if (weekflag == week)
      {
        if (dateTime > System.currentTimeMillis())
        {
          time = dateTime;
        } else
        {
          time = dateTime + 7 * 24 * 3600 * 1000;
        }
      } else if (weekflag > week)
      {
        time = dateTime + (weekflag - week) * 24 * 3600 * 1000;
      } else
      {
        time = dateTime + (weekflag - week + 7) * 24 * 3600 * 1000;
      }
    } else
    {
      if (dateTime > System.currentTimeMillis())
      {
        time = dateTime;
      } else
      {
        time = dateTime + 24 * 3600 * 1000;
      }
    }
    return time;
  }


}
