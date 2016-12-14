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

    /** Returns the title of an event */
    String getTitle() {
        return title;
    }

    /** Returns the description of an event */
    String getDescription() {
        return description;
    }

    /** Returns the date of an event */
    String getDate() {
        return date;
    }

    /** Returns the venue of an event */
    String getVenue() {
        return venue;
    }

    /** Returns the location of the venue */
    String getLocation() {
        return location;
    }
}