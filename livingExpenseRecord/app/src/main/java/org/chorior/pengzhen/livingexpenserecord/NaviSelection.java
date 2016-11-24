package org.chorior.pengzhen.livingexpenserecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by pengzhen on 24/11/16.
 */

public class NaviSelection extends Object {
    private int theme;
    private int animation;

    public NaviSelection(){
        theme = 0;
        animation = 0;
    }

    private static final String JSON_THEME = "theme";
    private static final String JSON_ANIMATION = "animation";

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();

        json.put(JSON_THEME,theme);
        json.put(JSON_ANIMATION,animation);

        return json;
    }

    public NaviSelection(JSONObject json) throws JSONException, ParseException {
        theme = json.getInt(JSON_THEME);
        animation = json.getInt(JSON_ANIMATION);
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public int getAnimation() {
        return animation;
    }

    public void setAnimation(int animation) {
        this.animation = animation;
    }
}
