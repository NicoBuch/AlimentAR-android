package com.android.proyectoalimentar.model;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public class Donation {

    String status;
    String description;
    DateTime pickUpTimeFrom;
    DateTime pickUpTimeTo;

    public Donation(DateTime pickUpTimeTo) {
        pickUpTimeFrom = DateTime.now();
        this.pickUpTimeTo = pickUpTimeTo;
    }

    public long getTimeLeft() {
        Interval interval = new Interval(pickUpTimeFrom, pickUpTimeTo);
        return interval.toDurationMillis();
    }

}
