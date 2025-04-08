package domainevent.command;

import javax.ejb.Local;
import javax.ejb.Stateless;

import business.mapper.CustomerMapper;
import domainevent.command.handler.BaseHandler;
import domainevent.command.handler.EventHandler;
import msa.commons.event.EventId;
import msa.commons.microservices.hotelbooking.commandevent.CreateHotelBookingCommand;
import msa.commons.microservices.hotelcustomer.qualifier.CreateCustomerByCreateBookingEventQualifier;

@Stateless
@CreateCustomerByCreateBookingEventQualifier
@Local(EventHandler.class)
public class CreateCustomerByCreateBookingEvent extends BaseHandler {

    @Override
    public void handleCommand(Object data) {
        CreateHotelBookingCommand c = (CreateHotelBookingCommand) data;
        if (!c.getCustomerInfo().isPreviouslyCreated())
            this.customerService
                    .save(CustomerMapper.INSTANCE.customerInfoCommandCreateReserationToDto(c.getCustomerInfo()));
        this.jmsEventPublisher.publish(EventId.CREATE_CUSTOMER_BY_HOTEL_BOOKING, c);
    }

}