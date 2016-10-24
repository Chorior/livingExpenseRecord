package org.chorior.pengzhen.livingexpenserecord;

/**
 * Created by pengzhen on 24/10/16.
 */

public class CustomRecord extends Object {
    private String title;
    private String text;
    private int cost;

    public CustomRecord(){
        cost = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
