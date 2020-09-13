package com.josef.mobile.util;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.google.common.util.concurrent.ListenableFuture;
import com.josef.mobile.Echo;
import com.josef.mobile.ui.ErrorActivity;
import com.josef.mobile.Message;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import static com.josef.mobile.util.Config.WORKREQUEST_KEYTASK_ERROR;
import static com.josef.mobile.util.Config.WORKREQUEST_KEYTAST_OUTPUT;
import static com.josef.mobile.util.Config.WORKREQUEST_AMOUNT;

/**http://joseph3d.com/wp-admin/
 Benutzer: joseph
 Passwort: %(d2cIVsYKJdVNTZDhd@BR8d**/

public class CallBackWorker extends ListenableWorker {

    private Context mContext;

    public CallBackWorker(Context context, WorkerParameters params) {
        super(context, params);
        mContext = context;
    }
//
    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        Data data = getInputData();
        final int index = data.getInt(WORKREQUEST_AMOUNT,0);

        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver<Result>() {
            @Nullable
            @Override
            public Object attachCompleter(@NonNull final CallbackToFutureAdapter.Completer<Result> completer) throws Exception {

                Callback callback = new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        final Data data = buildData(WORKREQUEST_KEYTASK_ERROR,e.toString());
                        completer.set(Result.failure(data));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code() >= 301) {
                            final Data data = buildData(WORKREQUEST_KEYTASK_ERROR,"invalid server response..");
                            Intent intent = new Intent(getApplicationContext(), ErrorActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);
                            completer.set(Result.failure(data));
                        }
                        Echo echo = new Echo();
                        Message message = echo.echo(new Message(),index);
                        String serialized = message.getMessage();
                        final Data data = buildData(WORKREQUEST_KEYTAST_OUTPUT,serialized);
                        completer.set(Result.success(data));
                    }
                };

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://joseph3d.com/")
                        .build();

                client.newCall(request).enqueue(callback);
                return callback;
            }
        });
    }
    private Data buildData(String key, String value) {
        return new Data.Builder()
                .putString(key,value)
                .build();
    }
}