package fi.jamk.sumcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sumCalc(View view) {
        EditText et1 = findViewById(R.id.editText);
        String str1 = et1.getText().toString();
        double num1 = Double.parseDouble(str1);

        EditText et2 = findViewById(R.id.editText2);
        String str2 = et2.getText().toString();
        double num2 = Double.parseDouble(str2);

        double sum = num1 + num2;
        String display = "Sum is " + sum;

        Toast.makeText(getApplicationContext(), display, Toast.LENGTH_LONG).show();
    }
}
