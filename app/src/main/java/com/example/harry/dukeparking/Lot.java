package com.example.harry.dukeparking;

import java.util.logging.Filter;

/**
 * Created by Harry on period11/period10/period16.
 */
public class Lot {
    private String Id;
    private String Name;
    private Integer Capacity;
    private Integer Current;
    private String Addr;

    public Lot(){
        super();
    }

    public Lot(String Id, String Name, Integer Capacity){
        super();
        this.Id = Id;
        this.Name = Name;
        this.Capacity = Capacity;
        this.Current = 0;
        this.Addr = "Default Addr";
    }

    public String getId(){
        return Id;
    }

    public String getName(){
        return Name;
    }

    public Integer getCapacity(){
        return Capacity;
    }

    public Integer getCurrent(){
        return Current;
    }

    public String getAddr(){return Addr;}

    public void setCurrent(Integer Current){
        this.Current = Current;
    }

    public void setAddr(String Addr){this.Addr = Addr;}
}
