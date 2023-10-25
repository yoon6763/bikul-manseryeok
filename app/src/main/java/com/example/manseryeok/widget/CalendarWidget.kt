package com.example.manseryeok.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.manseryeok.R
import com.example.manseryeok.manseryeokdb.ManseryeokSQLHelper
import com.example.manseryeok.page.MainActivity
import com.example.manseryeok.utils.Utils
import com.gun0912.tedpermission.provider.TedPermissionProvider
import java.util.Calendar


class CalendarWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val manseryeokSQLHelper = ManseryeokSQLHelper(context)
    manseryeokSQLHelper.open()

    val today = Calendar.getInstance()
    val year = today.get(Calendar.YEAR)
    val month = today.get(Calendar.MONTH) + 1
    val day = today.get(Calendar.DAY_OF_MONTH)

    val manseryeok = manseryeokSQLHelper.getDayData(year, month, day)

    manseryeokSQLHelper.close()

    val sb = StringBuilder()

    // hyganji, hmganji, hdganji,
    val yearGanji = manseryeok.cd_hyganjee!!
    val monthGanji = manseryeok.cd_hmganjee!!
    val dayGanji = manseryeok.cd_hdganjee!!
    val timeGanji = Utils.getTimeGanji(dayGanji[0], today[Calendar.HOUR_OF_DAY])

    sb.append("${timeGanji[0]}${dayGanji[0]}${monthGanji[0]}${yearGanji[0]}\n${timeGanji[1]}${dayGanji[1]}${monthGanji[1]}${yearGanji[1]}")

    val widgetText = sb.toString()
    val views = RemoteViews(context.packageName, R.layout.calendar_widget)
    views.setTextViewText(R.id.tv_widget_calendar, widgetText)
    views.setTextViewText(R.id.tv_widget_calendar_date, "${year}년 ${month}월 ${day}일")

    val intent = Intent(TedPermissionProvider.context, MainActivity::class.java) //실행할 액티비티의 클래스

    val pendingIntent = PendingIntent.getActivity(TedPermissionProvider.context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    views.setOnClickPendingIntent(R.id.widget_calendar_container, pendingIntent)

    appWidgetManager.updateAppWidget(appWidgetId, views)
}