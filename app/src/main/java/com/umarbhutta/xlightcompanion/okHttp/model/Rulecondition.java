package com.umarbhutta.xlightcompanion.okHttp.model;

import java.util.List;

/**
 * Created by guangbinw on 2017/3/17.
 */

public class Rulecondition {
    public List<Condition> condition;
    public List<Schedule> schedule;

    @Override
    public String toString() {
        return "Rulecondition{" +
                "condition=" + condition +
                ", schedule=" + schedule +
                '}';
    }
}
