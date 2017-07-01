package com.contentprovider.neba.contentproviders.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Neba on 01-Jul-17.
 */

public class Data implements Parcelable {
    private String name;
    private String city;
    private String house;
    private String years;

    public  Data(String name, String city, String house, String years){
        this.name=name;
        this.city=city;
        this.house=house;
        this.years=years;
    }

    protected Data(Parcel in) {
        name = in.readString();
        city = in.readString();
        house = in.readString();
        years = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeStringArray(new String[] {this.name,
                this.city,this.house,
                this.years});

    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getHouse() {
        return house;
    }

    public String getYears() {
        return years;
    }
}
