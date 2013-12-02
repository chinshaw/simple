package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Must extends DashBoardPlace so that it draws the dashboard activity.
 * 
 * @author chinshaw
 * 
 */
public class PlaygroundPlace extends ApplicationPlace {

    public PlaygroundPlace() {
    }

    static class Tokenizer implements PlaceTokenizer<PlaygroundPlace> {
        @Override
        public String getToken(PlaygroundPlace place) {
            return "";
        }

        @Override
        public PlaygroundPlace getPlace(String token) {
            return new PlaygroundPlace();
        }
    }

    @Override
    public String getApplicationTitle() {
        return "Playground";
    }
}