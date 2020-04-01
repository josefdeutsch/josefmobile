package com.josef.mobile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import com.josef.josefmobile.R;

public class MainActivity extends AppCompatActivity {

    private final Echo echo = new Echo();
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textview);
        new EndPointsAsync().execute();
    }

    private class EndPointsAsync extends AsyncTask<Void, Void, Message> {
        @Override
        protected Message doInBackground(Void... voids) {
            Message message = new Message();
            message.setMessage("echo me back..");
            return echo.echo(message, null);
        }

        @Override
        protected void onPostExecute(@Nullable Message result) {
            textView.setText(result.getMessage());
        }
    }
}
