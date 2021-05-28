/* SPDX-License-Identifier: Apache-2.0 */
package main.java.no.uib.eckochain.datasetcontract.model;

/**
 * Class for representing an event
 */
public class Event {

    private EventType type;
    private String invokedAt;

    /**
     * Constructor for Event object
     */
    public Event() {
    }

    /**
     * Constructor for Event object
     *
     * @param type      Event type
     * @param invokedAt Invoked at timestamp
     */
    public Event(EventType type, String invokedAt) {
        this.type = type;
        this.invokedAt = invokedAt;
    }

    /**
     * Get event type
     *
     * @return Event type
     */
    public EventType getType() {
        return type;
    }

    /**
     * Set event type
     *
     * @param type Event type
     */
    public void setType(EventType type) {
        this.type = type;
    }

    /**
     * Get invoked at timestamp
     *
     * @return Invoked at timestamp
     */
    public String getInvokedAt() {
        return invokedAt;
    }

    /**
     * Set invoked at timestamp
     *
     * @param invokedAt Invoked at timestamp
     */
    public void setInvokedAt(String invokedAt) {
        this.invokedAt = invokedAt;
    }
}
