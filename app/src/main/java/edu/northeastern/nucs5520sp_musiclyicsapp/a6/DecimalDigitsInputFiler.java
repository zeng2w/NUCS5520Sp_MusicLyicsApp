package edu.northeastern.nucs5520sp_musiclyicsapp.a6;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to limit input currency to 2 digits after the decimal point.
 * Credit: https://stackoverflow.com/questions/5357455/limit-decimal-places-in-android-edittext
 */
public class DecimalDigitsInputFiler implements InputFilter {

    Pattern pattern;

    public DecimalDigitsInputFiler() {
        pattern = Pattern.compile("[0-9]*((\\.[0-9]?)?)");
    }

    @Override
    public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
        Matcher matcher = pattern.matcher(spanned);
        if (!matcher.matches()) {
            return "";
        }
        return null;
    }
}
