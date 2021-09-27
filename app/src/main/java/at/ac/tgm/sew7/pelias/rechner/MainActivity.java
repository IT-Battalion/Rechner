package at.ac.tgm.sew7.pelias.rechner;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(this::itemClickEvent);
        findViewById(R.id.result).setBackgroundColor(Color.BLUE);
        findViewById(R.id.result).setOnTouchListener(this::resetResult);
        findViewById(R.id.berechnen).setOnClickListener(this::berechneErgebnis);
        findViewById(R.id.wert1).setOnFocusChangeListener(this::selectTextInput);
        findViewById(R.id.wert2).setOnFocusChangeListener(this::selectTextInput);
        findViewById(R.id.ms).setOnClickListener(this::sharedPreferenceSave);
        findViewById(R.id.mr).setOnClickListener(this::sharedPreferenceLoad);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.berechnen).setBackgroundColor(Color.GREEN);
    }

    public boolean itemClickEvent(MenuItem item) {
        if (item.getItemId() == R.id.reset) {
            ((TextView) findViewById(R.id.result)).setText("0");
            ((EditText) findViewById(R.id.wert1)).setText(R.string.wert_1);
            ((EditText) findViewById(R.id.wert2)).setText(R.string.wert_2);
            Spinner spinner = (Spinner) findViewById(R.id.operationType);
            spinner.setSelection(0);
        } else if (item.getItemId() == R.id.info) {
            Toast.makeText(getApplicationContext(), BuildConfig.VERSION_NAME + ", " + BuildConfig.AUTHOR, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void sharedPreferenceLoad(View view) {
        Toast.makeText(getApplicationContext(), "Lade...", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        float wert1 = sharedPreferences.getFloat(String.valueOf(R.id.wert1), 0);
        float wert2 = sharedPreferences.getFloat(String.valueOf(R.id.wert2), 0);
        ((EditText) findViewById(R.id.wert1)).setText(String.valueOf(wert1));
        ((EditText) findViewById(R.id.wert2)).setText(String.valueOf(wert2));
    }

    public void sharedPreferenceSave(View view) {
        try {
            Toast.makeText(getApplicationContext(), "Speichere...", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor writer = sharedPreferences.edit();
            writer.putFloat(String.valueOf(R.id.wert1), Float.parseFloat(((EditText) findViewById(R.id.wert1)).getText().toString()));
            writer.putFloat(String.valueOf(R.id.wert2), Float.parseFloat(((EditText) findViewById(R.id.wert2)).getText().toString()));
            writer.apply();
            Toast.makeText(getApplicationContext(), "Gespeichert.", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException ex) {
            Toast.makeText(getApplicationContext(), "Ungültige Zahlen", Toast.LENGTH_SHORT).show();
        }
    }

    public void selectTextInput(View view, Boolean b) {
        //Pattern das überprüft ob im Input Strings stehen oder nur zahlen.
        if (!Pattern.compile("\\d+").matcher(((EditText) view).getText().toString()).matches()) {
            if (b) {
                ((EditText) view).setText("");
            } else {
                if (view.getId() == R.id.wert1) {
                    ((EditText) view).setText(R.string.wert_1);
                } else if (view.getId() == R.id.wert2) {
                    ((EditText) view).setText(R.string.wert_2);
                }
            }
        }
    }

    public boolean resetResult(View view, MotionEvent motionEvent) {
        ((TextView) view.findViewById(R.id.result)).setText("0");
        return false;
    }

    @SuppressLint("NonConstantResourceId")
    public void berechneErgebnis(View view) {
        Toast.makeText(getApplicationContext(), "Berechne...", Toast.LENGTH_SHORT).show();
        EditText wert1element = (EditText) findViewById(R.id.wert1);
        EditText wert2element = (EditText) findViewById(R.id.wert2);
        Spinner spinner = (Spinner) findViewById(R.id.operationType);
        int operationID = spinner.getSelectedItemPosition();
        double wert1 = 0.0;
        double wert2 = 0.0;
        try {
            wert1 = Double.parseDouble(wert1element.getText().toString());
            wert2 = Double.parseDouble(wert2element.getText().toString());
        } catch (NumberFormatException ex) {
            Toast.makeText(getApplicationContext(), "Ungültige Zahl", Toast.LENGTH_SHORT).show();
            return;
        }
        double result = 0.0;
        switch (operationID) {
            case 4:
                result = wert1 / wert2;
                break;
            case 1:
                result = wert1 - wert2;
                break;
            case 2:
                result = wert1 * wert2;
                break;
            case 0:
                result = wert1 + wert2;
                break;
            default:
                break;
        }
        TextView resultTextelement = (TextView) findViewById(R.id.result);
        resultTextelement.setText(String.valueOf(result));
        if (result < 0) resultTextelement.setTextColor(Color.RED);
        else resultTextelement.setTextColor(Color.BLACK);
    }
}