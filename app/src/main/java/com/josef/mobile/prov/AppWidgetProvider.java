package com.josef.mobile.prov;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.josef.josefmobile.R;
import com.josef.mobile.SplashActivity;

import org.jetbrains.annotations.NotNull;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.josef.mobile.Config.KEY_BUTTON_TEXT;
import static com.josef.mobile.Config.RECIPE_INDEX;
import static com.josef.mobile.Config.SHAREDPREFERENCES_EDITOR;


public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {
    public static final String ACTION_TOAST = "actionToast";
    public static final String EXTRA_ITEM_POSITION = "extraItemPosition";
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            PendingIntent pendingIntent = getPendingIntent(context);

            SharedPreferences prefs = context.getSharedPreferences(SHAREDPREFERENCES_EDITOR, Context.MODE_PRIVATE);

            String buttonText = prefs.getString(KEY_BUTTON_TEXT + appWidgetId, "press me!");

            int recipeIndex = prefs.getInt(RECIPE_INDEX,0);

            Intent serviceIntent = getServiceIntent(context, recipeIndex,appWidgetId);

            RemoteViews views = getRemoteViews(context, pendingIntent, buttonText, serviceIntent);

            Bundle appWidgetOptions = appWidgetManager.getAppWidgetOptions(appWidgetId);

            resizeWidget(appWidgetOptions, views);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
    public static final String EXTRA_CUSTOM_EXTRAS2 = "custom_extra_2";

    @NotNull
    private Intent getServiceIntent(Context context, int recipeIndex, int appWidgetIndex) {
        Intent serviceIntent = new Intent(context, WidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS,recipeIndex);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,appWidgetIndex);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        return serviceIntent;
    }

    @NotNull
    private RemoteViews getRemoteViews(Context context, PendingIntent pendingIntent, String buttonText, Intent serviceIntent) {
        Intent clickIntent = new Intent(context, AppWidgetProvider.class);
        clickIntent.setAction(ACTION_TOAST);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context,
                0, clickIntent, 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setOnClickPendingIntent(R.id.example_widget_button, pendingIntent);
        views.setCharSequence(R.id.example_widget_button, "setText", buttonText);
        views.setRemoteAdapter(R.id.example_widget_stack_view, serviceIntent);
        views.setEmptyView(R.id.example_widget_stack_view, R.id.example_widget_empty_view);
        views.setPendingIntentTemplate(R.id.example_widget_stack_view, clickPendingIntent);

        return views;
    }

    private PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        resizeWidget(newOptions, views);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private void resizeWidget(Bundle appWidgetOptions, RemoteViews views) {
        int minWidth = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        int maxWidth = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
        int minHeight = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        int maxHeight = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);

        if (maxHeight > 100) {
            views.setViewVisibility(R.id.example_widget_text, View.VISIBLE);
            views.setViewVisibility(R.id.example_widget_button, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.example_widget_text, View.GONE);
            views.setViewVisibility(R.id.example_widget_button, View.GONE);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Toast.makeText(context, "onDeleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context) {
        Toast.makeText(context, "onEnabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisabled(Context context) {
        Toast.makeText(context, "onDisabled", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_TOAST.equals(intent.getAction())) {
            int clickedPosition = intent.getIntExtra(EXTRA_ITEM_POSITION, 0);
            Toast.makeText(context, "Clicked position: " + clickedPosition, Toast.LENGTH_SHORT).show();
            String url = "http://www.joseph3d.com";
            Intent intent1 = new Intent(Intent.ACTION_VIEW);
            intent1.setFlags(FLAG_ACTIVITY_NEW_TASK);
            intent1.setData(Uri.parse(url));
            context.startActivity(intent1);
        }
        super.onReceive(context, intent);
    }
}
