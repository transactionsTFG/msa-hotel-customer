package domainevent.registry;

import java.util.EnumMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import domainevent.command.handler.EventHandler;

import msa.commons.event.EventId;
import msa.commons.microservices.hotelcustomer.qualifier.CreateCustomerByCreateBookingEventQualifier;
import msa.commons.microservices.hotelcustomer.qualifier.GetCustomerByCreateBookingEventQualifier;

@Singleton
@Startup
public class EventHandlerRegistry {
    private Map<EventId, EventHandler> handlers = new EnumMap<>(EventId.class);
    private EventHandler createCustomerByHotelBooking;
    private EventHandler getCustomerByHotelBooking;

    @PostConstruct
    public void init() {
        this.handlers.put(EventId.CREATE_CUSTOMER_BY_HOTEL_BOOKING, createCustomerByHotelBooking);
        this.handlers.put(EventId.GET_CUSTOMER_BY_HOTEL_BOOKING, getCustomerByHotelBooking);
    }

    public EventHandler getHandler(EventId eventId) {
        return this.handlers.get(eventId);
    }

    @Inject
    public void setCreateCustomerByHotelBooking(
            @CreateCustomerByCreateBookingEventQualifier EventHandler createCustomerByCreateHotelBooking) {
        this.createCustomerByHotelBooking = createCustomerByCreateHotelBooking;
    }

    @Inject
    public void setGetCustomerByHotelBooking(
            @GetCustomerByCreateBookingEventQualifier EventHandler getCustomerByCreateBooking) {
        this.getCustomerByHotelBooking = getCustomerByCreateBooking;
    }
}