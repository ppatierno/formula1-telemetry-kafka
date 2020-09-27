/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.data.ParticipantData;
import io.ppatierno.formula1.packets.PacketEventData;

/**
 * Represents an event with the related details if any
 */
public class Event {

    private ParticipantData participantData;
    private PacketEventData eventData;

    private String hashtag;
    private String shortName;

    public Event(ParticipantData participantData, PacketEventData eventData) {
        this.participantData = participantData;
        if (participantData != null) {
            String driverId = participantData.getDriverId().name();
            // build an hashtag as #<first_char_firstname><first_char_lastname><race_number>
            this.hashtag = "#" + driverId.charAt(0) + driverId.charAt(driverId.indexOf("_") + 1) + participantData.getRaceNumber();
            // build a short name using first 3 chars of lastname
            this.shortName = driverId.substring(driverId.indexOf("_") + 1, driverId.indexOf("_") + 4);
        }
        this.eventData = eventData;
    }

    public ParticipantData getParticipantData() {
        return participantData;
    }

    public void setParticipantData(ParticipantData participantData) {
        this.participantData = participantData;
    }

    public PacketEventData getEventData() {
        return eventData;
    }

    public void setEventData(PacketEventData details) {
        this.eventData = details;
    }

    public String getHashtag() {
        return hashtag;
    }

    public String getShortName() {
        return shortName;
    }

    @Override
    public String toString() {
        return "Event[participantData=" + this.participantData +
                ",eventData=" + this.eventData +
                ",hashtag=" + this.hashtag +
                ",shortName=" + this.shortName +
                ']';
    }
}
