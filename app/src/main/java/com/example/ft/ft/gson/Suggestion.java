package com.example.ft.ft.gson;

import com.google.gson.annotations.SerializedName;

public class Suggestion {
    @SerializedName("comf")
    public ComFort comFort;

    public class ComFort {
        @SerializedName("txt")
        public String info;
    }
    @SerializedName("cw")
    public CarWash carWash;

    public class CarWash {
        @SerializedName("txt")
        public String info;
    }

    public Sport sport;

    public class Sport {
        @SerializedName("txt")
        public String info;
    }
}
