package domainevent.command;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import business.customer.CustomerDTO;
import business.qualifier.GetCustomerByCreateHotelBookingEventQualifier;
import domainevent.command.handler.BaseHandler;
import domainevent.command.handler.CommandPublisher;
import msa.commons.commands.hotelbooking.CreateHotelBookingCommand;
import msa.commons.event.EventData;
import msa.commons.event.EventId;

@Stateless
@GetCustomerByCreateHotelBookingEventQualifier
@Local(CommandPublisher.class)
public class GetCustomerByCreateHotelBookingEvent extends BaseHandler {
        private static final Logger LOGGER = LogManager.getLogger(GetCustomerByCreateHotelBookingEvent.class);

    @Override
    public void publishCommand(String json) {

        LOGGER.info("JSON recibido: {}", json);
        
        EventData eventData = EventData.fromJson(json, CreateHotelBookingCommand.class);
        CreateHotelBookingCommand command = (CreateHotelBookingCommand) eventData.getData();

        CustomerDTO customerDTO = this.customerService.getCustomerByDNI(command.getCustomerInfo().getDni());

        final boolean isPreviouslyCreated = customerDTO != null;
        final long customerId = isPreviouslyCreated ? customerDTO.getId() : 0L;

        command.getCustomerInfo().setIdCustomer(customerId);
        command.getCustomerInfo().setPreviouslyCreated(isPreviouslyCreated);
        this.jmsEventPublisher.publish(EventId.VALIDATE_HOTEL_CUSTOMER_BY_CREATE_HOTEL_BOOKING, eventData);

    }

}
