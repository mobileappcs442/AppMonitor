package com.cs442project.appmonitor.Preferences;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.ListPreference;
import android.util.AttributeSet;

/**
 * Created by Snehal on 5/4/2015.
 */

public class VibrationPreference extends ListPreference {
    private int clickedIndex;


    // This example will cause the phone to vibrate "SOS" in Morse Code
    // In Morse Code, "s" = "dot-dot-dot", "o" = "dash-dash-dash"
    // There are pauses to separate dots/dashes, letters, and words
    // The following numbers represent millisecond lengths
    private static final int dot = 150;      // Length of a Morse Code "dot" in milliseconds
    private static final int dash = 375;     // Length of a Morse Code "dash" in milliseconds
    private static final int short_gap = 150;    // Length of Gap Between dots/dashes
    private static final int medium_gap = 375;   // Length of Gap Between Letters
    private static final int long_gap = 750;    // Length of Gap Between Words



    private static final long[] sos_pattern = {
            0,  // Start immediately
            dot, short_gap, dot, short_gap, dot,    // s
            medium_gap,
            dash, short_gap, dash, short_gap, dash, // o
            medium_gap,
            dot, short_gap, dot, short_gap, dot    // s
    };

    private static final int beat = 250;
    private static final int interbeat = 100;
    private static final int between_beat_pairs = 700;
    private static final long[] heartbeat_pattern = {
            0,  // Start immediately
            beat, interbeat, beat, // o
            between_beat_pairs,
            beat, interbeat, beat, // o
    };

    private static final long[] jackhammer_pattern = {
            0,  // Start immediately
            100, 100,
            100, 100,
            100, 100,
            100, 100,
            100, 100,

            100, 100,
            100, 100,
            100, 100,
            100, 100,
            100, 100,

            100, 100,
            100, 100,
            100

    };


    public static final long[][] vibration_patterns = { null, sos_pattern, heartbeat_pattern, jackhammer_pattern};



    Vibrator vibrator;

    public VibrationPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public VibrationPreference(Context context) {
        super(context);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    protected void onPrepareDialogBuilder(Builder builder) {
        super.onPrepareDialogBuilder(builder);

        if (getEntries() == null || getEntryValues() == null) {
            throw new IllegalStateException(
                    "ListPreference requires an entries array and an entryValues array.");
        }

        clickedIndex = findIndexOfValue(getValue());
        builder.setSingleChoiceItems(getEntries(), clickedIndex,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clickedIndex = which;
                        vibrator.cancel();
                        if (clickedIndex > 0) vibrator.vibrate(vibration_patterns[clickedIndex], -1);
                    }
                });

        builder.setPositiveButton("OK", this).setNegativeButton("Cancel", this);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        vibrator.cancel();
        super.onDialogClosed(positiveResult);

        if (positiveResult && clickedIndex >= 0 && getEntryValues() != null) {
            String value = getEntryValues()[clickedIndex].toString();
            if (callChangeListener(value)) {
                setValue(value);
            }
        }
    }
}