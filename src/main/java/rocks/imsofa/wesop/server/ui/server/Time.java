/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;

import java.util.Calendar;
import java.util.Date;

/**
 * the object can be used for two purposes:
 * 1. a 24-hour format style time object
 * 2.representing an interval
 * @author lendle
 */
public class Time implements Comparable<Time>{
    private int hour=-1;
    private int minute=-1;
    private int second=-1;

    public Time(int hour, int minute, int second) {
        this.hour=hour;
        this.minute=minute;
        this.second=second;
    }

    public Time() {
    }
    
    
    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.hour;
        hash = 23 * hash + this.minute;
        hash = 23 * hash + this.second;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Time other = (Time) obj;
        if (this.hour != other.hour) {
            return false;
        }
        if (this.minute != other.minute) {
            return false;
        }
        if (this.second != other.second) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return hour+":"+minute+":"+second;
    }

    @Override
    public int compareTo(Time t) {
        int seconds1=this.toSeconds();
        int seconds2=t.toSeconds();
        if(seconds1<seconds2){
            return -1;
        }else if(seconds1>seconds2){
            return 1;
        }else{
            return 0;
        }
    }
    
    public int toSeconds(){
        return this.second+this.minute*60+this.hour*3600;
    }
    
    public static Time getTime(Date date){
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        Time time=new Time(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
        return time;
    }
}
