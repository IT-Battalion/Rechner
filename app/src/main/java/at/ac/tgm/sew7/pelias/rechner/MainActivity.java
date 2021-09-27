package at.ac.tgm.sew7.pelias.rechner;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.result).setBackgroundColor(Color.BLUE);
        findViewById(R.id.result).setOnTouchListener(this::resetResult);
        findViewById(R.id.berechnen).setOnClickListener(this::berechneErgebnis);
        findViewById(R.id.wert1).setOnFocusChangeListener(this::selectTextInput);
        findViewById(R.id.wert2).setOnFocusChangeListener(this::selectTextInput);
        findViewById(R.id.ms).setOnClickListener(this::sharedPreferenceSave);
        findViewById(R.id.mr).setOnClickListener(this::sharedPreferenceLoad);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.berechnen).setBackgroundColor(Color.GREEN);
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
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public void berechneErgebnis(View view) {
        Toast.makeText(getApplicationContext(), "Berechne...", Toast.LENGTH_SHORT).show();
        EditText wert1element = (EditText) findViewById(R.id.wert1);
        EditText wert2element = (EditText) findViewById(R.id.wert2);
        RadioGroup operationGroup = (RadioGroup) findViewById(R.id.operationType);
        int operationID = operationGroup.getCheckedRadioButtonId();
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
            case R.id.operationDivide:
                result = wert1 / wert2;
                break;
            case R.id.operationMinus:
                result = wert1 - wert2;
                break;
            case R.id.operationMultiple:
                result = wert1 * wert2;
                break;
            case R.id.operationPlus:
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