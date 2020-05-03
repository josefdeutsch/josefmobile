package com.josef.mobile.prov;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.josef.josefmobile.R;
import com.josef.mobile.SplashActivity;

import org.jetbrains.annotations.NotNull;

import static com.josef.mobile.Config.KEY_BUTTON_TEXT;
import static com.josef.mobile.Config.RECIPE_INDEX;
import static com.josef.mobile.Config.SHAREDPREFERENCES_EDITOR;
import static com.josef.mobile.Config.SHOPPINGLIST_TAG;
import static com.josef.mobile.prov.AppWidgetProvider.ACTION_TOAST;

public class AppWidgetConfig extends AppCompatActivity {

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private EditText editTextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_config);

        Intent configIntent = getIntent();
        Bundle extras = configIntent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        editTextButton = findViewById(R.id.edit_text_button);
    }

    public void confirmConfiguration(View v) {
        onConfiguration();
    }

    private void onConfiguration() {
        if (isRepositoryEmpty()) {
            alertDialog();
        } else {
            initAppWidgetManager();
        }
    }

    private boolean isRepositoryEmpty() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(SHAREDPREFERENCES_EDITOR, Context.MODE_PRIVATE);
        if (!prefs.contains(RECIPE_INDEX)) return true;
        return false;
    }

    private void initAppWidgetManager() {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        PendingIntent pendingIntent = getPendingIntent();

        String buttonText = editTextButton.getText().toString();

        Intent serviceIntent = getServiceIntent();

        RemoteViews views = getRemoteViews(pendingIntent, buttonText, serviceIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);

        supplyKeyButtonSharedPreferences(buttonText);

        Intent resultValue = getResult();

        setResult(RESULT_OK, resultValue);
        finish();
    }

    @NotNull
    private Intent getResult() {
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        return resultValue;
    }

    private void supplyKeyButtonSharedPreferences(String buttonText) {
        SharedPreferences prefs = getSharedPreferences(SHAREDPREFERENCES_EDITOR, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_BUTTON_TEXT + appWidgetId, buttonText);
        editor.apply();
    }

    @NotNull
    private RemoteViews getRemoteViews(PendingIntent pendingIntent, String buttonText, Intent serviceIntent) {
        Intent clickIntent = new Intent(this, AppWidgetProvider.class);
        clickIntent.setAction(ACTION_TOAST);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(this,
                0, clickIntent, 0);
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.widget);
        views.setOnClickPendingIntent(R.id.example_widget_button, pendingIntent);
        views.setCharSequence(R.id.example_widget_button, "setText", buttonText);
        views.setRemoteAdapter(R.id.example_widget_stack_view, serviceIntent);
        views.setEmptyView(R.id.example_widget_stack_view, R.id.example_widget_empty_view);
        views.setPendingIntentTemplate(R.id.example_widget_stack_view, clickPendingIntent);
        return views;
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, SplashActivity.class);
        return PendingIntent.getActivity(this, 0, intent, 0);
    }

    @NotNull
    private Intent getServiceIntent() {
        Intent serviceIntent = new Intent(this, WidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        return serviceIntent;
    }

    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.question))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences.Editor editor = getSharedPreferences(SHAREDPREFERENCES_EDITOR, MODE_PRIVATE).edit();
        editor.remove(SHOPPINGLIST_TAG);
        editor.putBoolean(SHOPPINGLIST_TAG, true);
        editor.apply();
    }


}
