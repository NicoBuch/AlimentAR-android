package com.android.proyectoalimentar.model;


import com.android.proyectoalimentar.R;

public enum NotificationType {

    QUALIFICATION_REQUEST("qualification_request", R.string.qualification_request),
    DONATIONS_NEARBY("donation_nearby", R.string.notification_donation_nearby);

    private String name;
    private int titleResource;

    NotificationType(String name, int titleResource){
        this.name = name;
        this.titleResource = titleResource;
    }
    public String getName() {
        return name;
    }

    public int getTitleResource(){
        return titleResource;
    }

    public static NotificationType fromString(String name) {
        if (name != null) {
            for (NotificationType n : NotificationType.values()) {
                if (name.equalsIgnoreCase(n.getName())) {
                    return n;
                }
            }
        }
        throw new IllegalArgumentException("No Notification type found with name " + name);
    }
}