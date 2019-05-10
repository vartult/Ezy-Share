package com.example.ezyshare;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class datatype2 {
    String name;
    String date;
    String file;

    public datatype2() {
    }

    datatype2(String name, String file, String date){
        this.name=name;
        this.file=file;
        this.date=date;
    }

    public String getDate() {
        return date;
    }

    public String getFile() {
        return file;
    }

    public String getName() {
        return name;
    }
}

