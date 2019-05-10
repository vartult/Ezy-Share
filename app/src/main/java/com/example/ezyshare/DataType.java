package com.example.ezyshare;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataType {
    String name;
    String date;
    String file;

    DataType(String name, String file){
        this.name=name;
        this.file=file;
        this.date=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }

    public String getDate() {
        return date;
    }

    public DataType() {

    }

    public String getFile() {
        return file;
    }

    public String getName() {
        return name;
    }
}

