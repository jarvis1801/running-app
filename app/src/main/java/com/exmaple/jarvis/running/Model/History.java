package com.exmaple.jarvis.running.Model;

public class History extends Object {
    private String _id;
    private String duration;
    private String created_at;
    private String original, destination;

    public History(String id, String duration, String created_at, String original, String destination) {
        this._id = id;
        this.duration = duration;
        this.created_at = created_at;
        this.original = original;
        this.destination = destination;
    }

    public String getId() {
        return this._id;
    }

    public String getDuration() {
        return this.duration;
    }

    public String getCreatedAt() {
        return this.created_at;
    }

    public String getOriginal() {
        return this.original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String toString() {
        return this.getDestination() + " " + this.getOriginal();
    }
}
