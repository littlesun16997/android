package fi.jamk.launchmap;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showMap(View view) {
        // get lat and lng values
        EditText editText1 = (EditText) findViewById(R.id.editText);
        EditText editText2 = (EditText) findViewById(R.id.editText2);

        String numberOne = editText1.getText().toString();
        String numberTwo = editText2.getText().toString();

        double lat = Double.parseDouble(numberOne);
        double lng = Double.parseDouble(numberTwo);

        // show map
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:" + lat + "," + lng + "?q=" + lat + "," + lng));
        startActivity(intent);
    }
}
