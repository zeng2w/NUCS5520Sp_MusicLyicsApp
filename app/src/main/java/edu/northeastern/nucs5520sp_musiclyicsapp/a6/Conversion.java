package edu.northeastern.nucs5520sp_musiclyicsapp.a6;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

// Class to scrape relevant information from the currency conversion site
// Credit: https://www.zenrows.com/blog/web-scraping-java#connect-to-your-target-website
public class Conversion implements Parcelable {

    private final String originalAmount;
    private String convertedAmount;
    private final String fromCurrency;
    private final String toCurrency;

    /**
     * Default constructor.
     */
    public Conversion(String originalAmount, String fromCurrency, String toCurrency) {
        this.originalAmount = originalAmount;
        this.convertedAmount = "0.00";
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
    }

    protected Conversion(Parcel in) {
        originalAmount = in.readString();
        convertedAmount = in.readString();
        fromCurrency = in.readString();
        toCurrency = in.readString();
    }

    public static final Creator<Conversion> CREATOR = new Creator<Conversion>() {
        @Override
        public Conversion createFromParcel(Parcel in) {
            return new Conversion(in);
        }

        @Override
        public Conversion[] newArray(int size) {
            return new Conversion[size];
        }
    };

    public String getOriginalAmount() {
        return originalAmount;
    }

    public String getConvertedAmount() {
        return convertedAmount;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(originalAmount);
        parcel.writeString(convertedAmount);
        parcel.writeString(fromCurrency);
        parcel.writeString(toCurrency);
    }

    public void setConvertedAmount(String convertedAmount) {
        this.convertedAmount = convertedAmount;
    }
}
