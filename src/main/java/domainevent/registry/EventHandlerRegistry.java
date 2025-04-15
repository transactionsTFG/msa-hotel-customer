package domainevent.registry;

import java.util.EnumMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import domainevent.command.handler.CommandPublisher;

import msa.commons.event.EventId;
import msa.commons.microservices.hotelcustomer.qualifier.CreateCustomerByCreateBookingEventCommitQualifier;
import msa.commons.microservices.hotelcustomer.qualifier.GetCustomerByCreateBookingEventQualifier;

@Singleton
@Startup
public class EventHandlerRegistry {
    private Map<EventId, CommandPublisher> handlers = new EnumMap<>(EventId.class);
    private CommandPublisher createCustomerByHotelBooking;
    private CommandPublisher getCustomerByHotelBooking;

    @PostConstruct
    public void init() {
        this.handlers.put(EventId.BEGIN_CREATE_CUSTOMER_BY_HOTEL_BOOKING, createCustomerByHotelBooking);
        this.handlers.put(EventId.GET_HOTEL_CUSTOMER, getCustomerByHotelBooking);
    }

    public CommandPublisher getHandler(EventId eventId) {
        return this.handlers.get(eventId);
    }

    @Inject
    public void setCreateCustomerByHotelBooking(
            @CreateCustomerByCreateBookingEventCommitQualifier CommandPublisher createCustomerByCreateHotelBooking) {
        this.createCustomerByHotelBooking = createCustomerByCreateHotelBooking;
    }

    @Inject
    public void setGetCustomerByHotelBooking(
            @GetCustomerByCreateBookingEventQualifier CommandPublisher getCustomerByCreateBooking) {
        this.getCustomerByHotelBooking = getCustomerByCreateBooking;
    }
}