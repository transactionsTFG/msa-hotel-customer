package domainevent.command;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import business.customer.CustomerDTO;
import business.qualifier.ValidateHotelCustomerByCreateHotelBookingEventQualifier;
import domainevent.command.handler.BaseHandler;
import domainevent.command.handler.CommandPublisher;
import msa.commons.commands.hotelbooking.CreateHotelBookingCommand;
import msa.commons.event.EventData;
import msa.commons.event.EventId;

@Stateless
@ValidateHotelCustomerByCreateHotelBookingEventQualifier
@Local(CommandPublisher.class)
public class ValidateHotelCustomerByCreateHotelBookingEvent extends BaseHandler {

    private static final Logger LOGGER = LogManager.getLogger(ValidateHotelCustomerByCreateHotelBookingEvent.class);

    @Override
    public void publishCommand(String json) {

        LOGGER.info("JSON recibido: {}", json);

        EventData eventData = EventData.fromJson(json, CreateHotelBookingCommand.class);
        CreateHotelBookingCommand command = (CreateHotelBookingCommand) eventData.getData();

        CustomerDTO customerDTO = this.customerService.validateCustomer(CustomerDTO.builder()
                .dni(command.getCustomerInfo().getDni())
                .name(command.getCustomerInfo().getName())
                .email(command.getCustomerInfo().getEmail())
                .phone(command.getCustomerInfo().getPhone())
                .sagaId(eventData.getSagaId())
                .build());

        if (customerDTO == null) {
            this.jmsEventPublisher.publish(EventId.CANCEL_VALIDATE_HOTEL_CUSTOMER_BY_CREATE_HOTEL_BOOKING, eventData);
        } else {
            this.jmsEventPublisher.publish(EventId.CONFIRM_VALIDATE_HOTEL_CUSTOMER_BY_CREATE_HOTEL_BOOKING, eventData);
        }

    }

}
