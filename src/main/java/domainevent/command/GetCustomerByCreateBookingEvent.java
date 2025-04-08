package domainevent.command;

import javax.ejb.Local;
import javax.ejb.Stateless;

import business.customer.CustomerDTO;
import domainevent.command.handler.BaseHandler;
import domainevent.command.handler.EventHandler;
import msa.commons.event.EventId;
import msa.commons.microservices.hotelbooking.commandevent.CreateHotelBookingCommand;
import msa.commons.microservices.hotelcustomer.qualifier.GetCustomerByCreateBookingEventQualifier;

@Stateless
@GetCustomerByCreateBookingEventQualifier
@Local(EventHandler.class)
public class GetCustomerByCreateBookingEvent extends BaseHandler {

    @Override
    public void handleCommand(Object data) {
        final CreateHotelBookingCommand command = this.gson.fromJson(data.toString(), CreateHotelBookingCommand.class);
        CustomerDTO customerDTO = this.customerService.getCustomerByDNI(command.getCustomerInfo().getDni());
        command.getCustomerInfo().setPreviouslyCreated(customerDTO != null);
        this.jmsEventPublisher.publish(EventId.VALIDATE_HOTEL_CUSTOMER, command);
    }

}
