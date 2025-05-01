package domainevent.registry;

import java.util.EnumMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import business.qualifier.CommitCreateCustomerByCreateHotelBookingEventQualifier;
import business.qualifier.GetCustomerByCreateHotelBookingEventQualifier;
import business.qualifier.ValidateHotelCustomerByCreateHotelBookingEventQualifier;
import business.qualifier.ValidateHotelCustomerByUpdateHotelBookingEventQualifier;
import domainevent.command.handler.CommandPublisher;

import msa.commons.event.EventId;

@Singleton
@Startup
public class EventHandlerRegistry {
    private Map<EventId, CommandPublisher> handlers = new EnumMap<>(EventId.class);
    private CommandPublisher createCustomerByHotelBooking;
    private CommandPublisher getCustomerByHotelBooking;
    private CommandPublisher validateHotelCustomerByCreateHotelBooking;
    private CommandPublisher validateHotelCustomerByUpdateHotelBooking;

    @PostConstruct
    public void init() {
        this.handlers.put(EventId.BEGIN_CREATE_CUSTOMER_BY_HOTEL_BOOKING, createCustomerByHotelBooking);
        this.handlers.put(EventId.GET_HOTEL_CUSTOMER, getCustomerByHotelBooking);
        this.handlers.put(EventId.VALIDATE_HOTEL_CUSTOMER_BY_CREATE_HOTEL_BOOKING,
                validateHotelCustomerByCreateHotelBooking);
        this.handlers.put(EventId.VALIDATE_HOTEL_CUSTOMER_BY_UPDATE_HOTEL_BOOKING,
                validateHotelCustomerByUpdateHotelBooking);
    }

    public CommandPublisher getHandler(EventId eventId) {
        return this.handlers.get(eventId);
    }

    @Inject
    public void setCreateCustomerByHotelBooking(
            @CommitCreateCustomerByCreateHotelBookingEventQualifier CommandPublisher createCustomerByCreateHotelBooking) {
        this.createCustomerByHotelBooking = createCustomerByCreateHotelBooking;
    }

    @Inject
    public void setGetCustomerByHotelBooking(
            @GetCustomerByCreateHotelBookingEventQualifier CommandPublisher getCustomerByCreateBooking) {
        this.getCustomerByHotelBooking = getCustomerByCreateBooking;
    }

    @Inject
    public void setValidateHotelCustomerByCreateHotelBooking(
            @ValidateHotelCustomerByCreateHotelBookingEventQualifier CommandPublisher validateHotelCustomer) {
        this.validateHotelCustomerByCreateHotelBooking = validateHotelCustomer;
    }

    @Inject
    public void setValidateHotelCustomerByUpdateHotelBooking(
            @ValidateHotelCustomerByUpdateHotelBookingEventQualifier CommandPublisher validateHotelCustomerByUpdateHotelBooking) {
        this.validateHotelCustomerByUpdateHotelBooking = validateHotelCustomerByUpdateHotelBooking;
    }
}