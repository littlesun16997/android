package fi.jamk.phonemodels;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends Activity {

    public void backButtonPressed(View view) {
        // finish and close activity
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // get intent which has used to open this activity
        Intent intent = getIntent();

        // get data from intent
        Bundle bundle = intent.getExtras();

        // get phone name
        String phone = bundle.getString("phone");

        // update text and image views to show data
        TextView textView = (TextView) findViewById(R.id.phoneTextView);
        textView.setText(phone);
        ImageView imageView = (ImageView) findViewById(R.id.phoneImageView);
        TextView textView2 = (TextView) findViewById(R.id.phoneTextView2);
        textView2.setText("This is a model of " + phone + ". You can consider buying it.");

        // show phone image
        switch (phone) {
            case "Android":
                imageView.setImageResource(R.drawable.android);
                break;
            case "iPhone":
                imageView.setImageResource(R.drawable.ios);
                break;
            case "WindowsMobile":
                imageView.setImageResource(R.drawable.windows);
                break;
            case "Blackberry":
                imageView.setImageResource(R.drawable.blackberry);
                break;
            case "WebOS":
                imageView.setImageResource(R.drawable.webos);
                break;
            case "Ubuntu":
                imageView.setImageResource(R.drawable.ubuntu);
                break;
        }
    }
}