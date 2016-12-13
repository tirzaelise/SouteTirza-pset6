/* Native App Studio: Assignment 6
 * Tirza Soute
 *
 * This file implements the Event object.
 */

package com.example.tirza.soutetirza_pset62;

import java.io.Serializable;

class Event implements Serializable {
    private String title;
    private String description;
    private String date;
    private String venue;
    private String location;

    /** Creates Event constructor */
    Event(String title, String description, String date, String venue, String location) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.venue = venue;
        this.location = location;
    }

    /** Default constructor required for calls to DataSnapshot.getValue(Event.class) */
    public Event() {
    }

    String getTitle() {
        return title;
    }

    String getDescription() {
        return description;
    }

    String getDate() {
        return date;
    }

    String getVenue() {
        return venue;
    }

    String getLocation() {
        return location;
    }
}