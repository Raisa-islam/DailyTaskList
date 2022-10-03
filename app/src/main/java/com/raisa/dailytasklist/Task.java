package com.raisa.dailytasklist;

public class Task {
    String id, title, description, hour, min;
    public Task(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public Task(String title, String description, String hour, String min) {
       // this.id = id;
        this.title = title;
        this.description = description;
        this.hour = hour;
        this.min = min;
    }
}
