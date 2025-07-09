package com.example.wanderfunmobile.core.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.wanderfunmobile.domain.model.places.Place;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoritePlaceManager {

    private static final String PREF_NAME = "FavoritePlaces";
    private static final String PLACE_IDS_KEY = "placeIds";

    private static FavoritePlaceManager instance;
    private final SharedPreferences sharedPreferences;

    private FavoritePlaceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized FavoritePlaceManager getInstance(Context context) {
        if (instance == null) {
            instance = new FavoritePlaceManager(context);
        }
        return instance;
    }

    public void init(List<Place> placeList) {
        Set<String> placeIds = new HashSet<>();
        for (Place place : placeList) {
            placeIds.add(String.valueOf(place.getId()));
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(PLACE_IDS_KEY, placeIds);
        editor.apply();
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(PLACE_IDS_KEY);
        editor.apply();
    }

    public void add(Place place) {
        Set<String> placeIds = getPlaceIds();
        placeIds.add(String.valueOf(place.getId()));
        sharedPreferences.edit().putStringSet(PLACE_IDS_KEY, placeIds).apply();
    }

    public void remove(Place place) {
        Set<String> placeIds = getPlaceIds();
        placeIds.remove(String.valueOf(place.getId()));
        sharedPreferences.edit().putStringSet(PLACE_IDS_KEY, placeIds).apply();
    }

    public boolean isFavorite(Place place) {
        Set<String> placeIds = getPlaceIds();
        return placeIds.contains(String.valueOf(place.getId()));
    }

    private Set<String> getPlaceIds() {
        return new HashSet<>(sharedPreferences.getStringSet(PLACE_IDS_KEY, new HashSet<>()));
    }
}

