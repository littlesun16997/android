package fi.jamk.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new DateCustomDialog(this, R.id.editText2);
    }

    public void addNotification(View view) {
        // The id of the channel.
        String CHANNEL_ID = "my_channel_01";

        // The user-visible name of the channel.
        CharSequence name = getString(R.string.channel_name);

        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

        EditText et1 = (EditText) findViewById(R.id.editText);
        String user = et1.getText().toString();
        EditText et2 = (EditText) findViewById(R.id.editText2);
        String birthday = et2.getText().toString();

        String display = user + "'s birthday is " + birthday;

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Notification")
                        .setContentText(display);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(mChannel);
        manager.notify(0, builder.build());

        Toast.makeText(getApplicationContext(), display, Toast.LENGTH_LONG).show();
    }
}
