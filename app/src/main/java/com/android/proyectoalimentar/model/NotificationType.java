package com.android.proyectoalimentar.model;


import com.android.proyectoalimentar.R;

public enum NotificationType {
    DONATIONS_CLOSE("donation_close", R.string.notification_donation_close);

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