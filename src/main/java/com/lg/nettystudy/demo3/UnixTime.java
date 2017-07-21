package com.lg.nettystudy.demo3;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liuguo on 2017/7/19.
 */
public class UnixTime {
    private final long value;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:MM:ss");

    public UnixTime() {
        this(System.currentTimeMillis() / 1000L + 2208988800L);
    }

    public UnixTime(long value) {
       this.value = value;
    }

    public long value() {
        return value;
    }

    @Override
    public String toString() {
        return dateFormat.format(new Date((value() - 2208988800L) * 1000L));
    }
}
