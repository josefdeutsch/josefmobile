package com.josef.mobile.util;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.josef.josefmobile.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.josef.mobile.util.AppWidgetProvider.EXTRA_ITEM_POSITION;

public class WidgetService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        final ExampleWidgetItemFactory itemFactory = new ExampleWidgetItemFactory(getApplicationContext(), intent);
        itemFactory.
                context = getApplicationContext();
        return itemFactory;
    }

    static class ExampleWidgetItemFactory implements RemoteViewsFactory {

        private Context context;

        private int recipeIndex;
        private int appWidgetId;

        ExampleWidgetItemFactory(Context context, Intent intent) {
            this.context = context;
            this.recipeIndex = intent.getIntExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            this.appWidgetId = this.recipeIndex = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return 3;
        }

        RemoteViews views;
        @Override
        public RemoteViews getViewAt(int position) {

            //query design to display new events on my server if applicable..

            views = new RemoteViews(context.getPackageName(), R.layout.widget_item);
            URL url = null;
            try {
                url = new URL("http://joseph3d.com/wp-content/uploads/2019/06/0002.png");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap bmp = null;
            try {
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            views.setImageViewBitmap(R.id.imageButtonViewWidget,bmp);

            Intent fillIntent = new Intent();
            fillIntent.putExtra(EXTRA_ITEM_POSITION, position);
            views.setOnClickFillInIntent(R.id.imageButtonViewWidget, fillIntent);

            return views;
        }


        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }


        public boolean hasInternet(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
                return true;
            } else {
                return false;
            }
        }
    }

}
