/* Native App Studio: Assignment 6
 * Tirza Soute
 *
 * This file uses the data that was received from HttpRequest to create an ArrayList of Event
 * objects.
 */

package com.example.tirza.soutetirza_pset62;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

class EventAsyncTask extends AsyncTask<String, String, String> {
    private SearchActivity activity;
    private Context context;

    /** Creates an EventAsyncTask constructor */
    EventAsyncTask(SearchActivity activity) {
        this.activity = activity;
        this.context = this.activity.getApplicationContext();
    }

    /** Returns the data that was obtained using the HttpRequest file */
    protected String doInBackground(String... params) {
        return HttpRequest.downloadFromAPI(params);
    }

    /** Creates an ArrayList of Event objects and passes the data to SearchActivity */
    protected void onPostExecute(String result) {
        ArrayList<Event> events = new ArrayList<>();

        if (!(result.contains("<total_items>0</total_items>"))) {
            NodeList nodes = createNodes(result);
            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    Element element = (Element) nodes.item(i);
                    Event event = createEvent(element);
                    events.add(event);
                }
            }
        } else {
            Toast.makeText(context, "No data was found", Toast.LENGTH_LONG).show();
        }
        this.activity.setData(events);
    }

    /**
     * Creates a NodeList of the events that were obtained using HttpRequest
     * Source: http://www.java2s.com/Code/Java/XML/ParseanXMLstringUsingDOMandaStringReader.htm
     */
    private NodeList createNodes(String result) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(result));

            Document doc = db.parse(is);
            return doc.getElementsByTagName("event");
        } catch (SAXException | ParserConfigurationException | IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates an Event object from an element
     * Source: http://www.java2s.com/Code/Java/XML/ParseanXMLstringUsingDOMandaStringReader.htm
     */
    private Event createEvent(Element element) {
        NodeList titleNode = element.getElementsByTagName("title");
        Element titleValue = (Element) titleNode.item(0);
        String title = getCharacterDataFromElement(titleValue);

        NodeList descriptionNode = element.getElementsByTagName("description");
        Element descriptionValue = (Element) descriptionNode.item(0);
        String description = getCharacterDataFromElement(descriptionValue);
        description = prettifyDescription(description);

        NodeList dateNode = element.getElementsByTagName("start_time");
        Element dateValue = (Element) dateNode.item(0);
        String date = getCharacterDataFromElement(dateValue);
        date = prettifyDate(date);

        NodeList venueNode = element.getElementsByTagName("venue_name");
        Element venueValue = (Element) venueNode.item(0);
        String venue = getCharacterDataFromElement(venueValue);

        NodeList venueAddressNode = element.getElementsByTagName("venue_address");
        Element addressValue = (Element) venueAddressNode.item(0);
        String address = getCharacterDataFromElement(addressValue);

        NodeList cityNode = element.getElementsByTagName("city_name");
        Element cityValue = (Element) cityNode.item(0);
        String city = getCharacterDataFromElement(cityValue);

        String location = city;
        if (!address.equals("")) location = address + ", " + city;

        return new Event(title, description, date, venue, location);
    }

    /**
     * Gets character data from an element
     * Source: http://www.java2s.com/Code/Java/XML/ParseanXMLstringUsingDOMandaStringReader.htm
     */
    private static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

    /** Converts the date to a prettier format (text instead of digits) */
    private String prettifyDate(String date) {
        String[] splitDate = date.split("-");
        String year = splitDate[0];
        String month = splitDate[1];
        String[] dayAndTime = splitDate[2].split(" ");
        String day = dayAndTime[0];
        String time = dayAndTime[1];

        String prettyMonth = dateDigitsToText(month);
        // Removes the seconds from the start time
        time = time.substring(0, time.length() - 3);
        // Removes start time if no start time was given by the event creator
        if (time.equals("00:00")) time = "";

        return day + " " + prettyMonth + " " + year + " " + time;
    }

    /** Converts the month from digit to text form*/
    private String dateDigitsToText(String digitMonth) {
        String textMonth = "-500";

        if (digitMonth.endsWith("01")) {
            textMonth = "January";
        } else if (digitMonth.endsWith("02")) {
            textMonth = "February";
        } else if (digitMonth.endsWith("02")) {
            textMonth = "February";
        } else if (digitMonth.endsWith("03")) {
            textMonth = "March";
        } else if (digitMonth.endsWith("04")) {
            textMonth = "April";
        } else if (digitMonth.endsWith("05")) {
            textMonth = "May";
        } else if (digitMonth.endsWith("06")) {
            textMonth = "June";
        } else if (digitMonth.endsWith("07")) {
            textMonth = "July";
        } else if (digitMonth.endsWith("08")) {
            textMonth = "August";
        } else if (digitMonth.endsWith("09")) {
            textMonth = "September";
        } else if (digitMonth.endsWith("10")) {
            textMonth = "October";
        } else if (digitMonth.endsWith("11")) {
            textMonth = "November";
        } else if (digitMonth.endsWith("12")) {
            textMonth = "December";
        }
        return textMonth;
    }

    /**
     * Converts the description to a prettier format (removes the first character if it's a
     * whitespace and replaces the HTML character for an apostrophe with an apostrophe)
     */
    private String prettifyDescription(String description) {
        if (!description.equals("")) {
            // Removes the first character from the description if it is a whitespace
            if (description.startsWith(" ")) description = description.substring(1);
            // Replaces the HTML character
            description = description.replace("&#39;", "'");
        }
        return description;
    }
}
