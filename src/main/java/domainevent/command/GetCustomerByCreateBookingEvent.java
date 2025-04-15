package domainevent.command;

import javax.ejb.Local;
import javax.ejb.Stateless;

import business.customer.CustomerDTO;
import domainevent.command.handler.BaseHandler;
import domainevent.command.handler.CommandPublisher;
import msa.commons.event.EventData;
import msa.commons.event.EventId;
import msa.commons.microservices.hotelbooking.commandevent.CreateHotelBookingCommand;
import msa.commons.microservices.hotelcustomer.qualifier.GetCustomerByCreateBookingEventQualifier;

@Stateless
@GetCustomerByCreateBookingEventQualifier
@Local(CommandPublisher.class)
public class GetCustomerByCreateBookingEvent extends BaseHandler {

    @Override
    public void publishCommand(String json) {
        EventData eventData = EventData.fromJson(json, CreateHotelBookingCommand.class);
        CreateHotelBookingCommand command = (CreateHotelBookingCommand) eventData.getData();

        CustomerDTO customerDTO = this.customerService.getCustomerByDNI(command.getCustomerInfo().getDni());

        final boolean isPreviouslyCreated = customerDTO != null;
        final long customerId = isPreviouslyCreated ? customerDTO.getId() : 0L;

        command.getCustomerInfo().setIdCustomer(customerId);
        command.getCustomerInfo().setPreviouslyCreated(isPreviouslyCreated);
        this.jmsEventPublisher.publish(EventId.VALIDATE_HOTEL_CUSTOMER, eventData);

    }

}
