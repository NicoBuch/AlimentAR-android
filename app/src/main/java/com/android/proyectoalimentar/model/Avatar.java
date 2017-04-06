package com.android.proyectoalimentar.model;


public class Avatar {

    private String original;
    private String thumb;

    public Avatar(String original, String thumb){
        this.original = original;
        this.thumb = thumb;
    }

    public String getOriginal() {
        return original;
    }

    public String getThumb() {
        return thumb;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
