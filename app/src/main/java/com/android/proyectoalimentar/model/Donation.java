package com.android.proyectoalimentar.model;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public class Donation {

    int id;
    String status;
    String description;
    DateTime pickupTimeFrom;
    DateTime pickupTimeTo;
    DateTime activatedAt;
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

    public String getDescription() {
        return description;
    }

    public long getTimeLeft() {
        if (DateTime.now().isAfter(pickupTimeTo)) {
            return 0;
        }
        DateTime timeLimit = activatedAt.plusHours(1);
        timeLimit = timeLimit.isAfter(pickupTimeTo) ? pickupTimeTo : timeLimit;
        if(DateTime.now().isAfter(timeLimit)){
            return 0;
        }
        Interval interval = new Interval(DateTime.now(), timeLimit);
        return interval.toDurationMillis();
    }

    /**
     * This methods calculate the possible time to retrieve the donation.
     * If pickupTimeTo is more than 1 hour from now, this will return 1 hour
     * if not will return the interval between now and pickupTimeTo.
     * @return Possible time left in minutes
     */
    public long getPossibleTimeLeft(){
        DateTime timeLimit = DateTime.now().plusHours(1).isAfter(pickupTimeTo) ? pickupTimeTo : DateTime.now().plusHours(1);
        Interval interval = new Interval(DateTime.now(),timeLimit);
        return interval.toDuration().getStandardMinutes();
    }

    public FoodLocation getDonator() {
        return donator;
    }

    public int getId() {
        return id;
    }
}
