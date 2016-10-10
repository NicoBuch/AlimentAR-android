package com.android.proyectoalimentar.model;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public class Donation {

    String status;
    String description;
    DateTime pickupTimeFrom;
    DateTime pickupTimeTo;
    FoodLocation donator;

    public Donation(FoodLocation donator) {
        this.donator = donator;
    }

    public Donation(DateTime pickupTimeTo) {
        pickupTimeFrom = DateTime.now();
        this.pickupTimeTo = pickupTimeTo;
    }

    public Donation(String description) {
        this.description = description;
        pickupTimeFrom = DateTime.now();
        pickupTimeTo = pickupTimeFrom.plusHours(1);
    }

    public DateTime getPickupTimeFrom() {
        return pickupTimeFrom;
    }

    public DateTime getPickupTimeTo() {
        return pickupTimeTo;
    }

    public long getTimeLeft() {
        if (DateTime.now().isAfter(pickupTimeTo)) {
            return 0;
        }
        Interval interval = new Interval(DateTime.now(), pickupTimeTo);
        return interval.toDurationMillis();
    }

    public FoodLocation getDonator() {
        return donator;
    }
}
