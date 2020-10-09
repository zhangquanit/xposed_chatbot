package com.ali.user.mobile.rpc.register.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RegisterCountryModel implements Parcelable {
    public static final Parcelable.Creator<RegisterCountryModel> CREATOR = new Parcelable.Creator<RegisterCountryModel>() {
        public RegisterCountryModel createFromParcel(Parcel parcel) {
            return new RegisterCountryModel(parcel);
        }

        public RegisterCountryModel[] newArray(int i) {
            return new RegisterCountryModel[i];
        }
    };
    public String areaCode;
    public String checkPattern;
    public String countryCode;
    public String countryFlagBaseUrl;
    public String countryName;
    public String sortLetters;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.areaCode);
        parcel.writeString(this.countryName);
        parcel.writeString(this.countryCode);
        parcel.writeString(this.checkPattern);
        parcel.writeString(this.sortLetters);
        parcel.writeString(this.countryFlagBaseUrl);
    }

    public RegisterCountryModel() {
    }

    protected RegisterCountryModel(Parcel parcel) {
        this.areaCode = parcel.readString();
        this.countryName = parcel.readString();
        this.countryCode = parcel.readString();
        this.checkPattern = parcel.readString();
        this.sortLetters = parcel.readString();
        this.countryFlagBaseUrl = parcel.readString();
    }
}
