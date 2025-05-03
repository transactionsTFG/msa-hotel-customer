package domainevent.command;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import business.customer.CustomerDTO;
import business.qualifier.ValidateHotelCustomerByUpdateHotelBookingEventQualifier;
import domainevent.command.handler.BaseHandler;
import domainevent.command.handler.CommandPublisher;
import msa.commons.commands.hotelbooking.UpdateHotelBookingCommand;
import msa.commons.event.EventData;
import msa.commons.event.EventId;

@Stateless
@ValidateHotelCustomerByUpdateHotelBookingEventQualifier
@Local(CommandPublisher.class)
public class ValidateHotelCustomerByUpdateHotelBookingEvent extends BaseHandler {

    private static final Logger LOGGER = LogManager.getLogger(ValidateHotelCustomerByUpdateHotelBookingEvent.class);

    @Override
    public void publishCommand(String json) {

        LOGGER.info("JSON recibido: {}", json);

        EventData eventData = EventData.fromJson(json, UpdateHotelBookingCommand.class);
        UpdateHotelBookingCommand command = (UpdateHotelBookingCommand) eventData.getData();

        CustomerDTO customerDTO = this.customerService.getCustomerByDNI(command.getCustomerDNI());

        if (customerDTO == null || !customerDTO.isActive()) {
            this.jmsEventPublisher.publish(EventId.CANCEL_VALIDATE_HOTEL_CUSTOMER_BY_UPDATE_HOTEL_BOOKING, eventData);
        } else {
            this.jmsEventPublisher.publish(EventId.CONFIRM_VALIDATE_HOTEL_CUSTOMER_BY_UPDATE_HOTEL_BOOKING, eventData);
        }

    }

}
