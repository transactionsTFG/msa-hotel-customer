package domainevent.command;

import javax.ejb.Local;
import javax.ejb.Stateless;

import domainevent.command.handler.BaseHandler;
import domainevent.command.handler.CommandPublisher;
import msa.commons.event.EventData;
import msa.commons.event.EventId;
import msa.commons.microservices.hotelbooking.commandevent.CreateHotelBookingCommand;
import msa.commons.microservices.hotelcustomer.qualifier.CreateCustomerByCreateBookingEventRollbackQualifier;

@Stateless
@CreateCustomerByCreateBookingEventRollbackQualifier
@Local(CommandPublisher.class)
public class CreateCustomerByCreateBookingEventRollback extends BaseHandler {

    @Override
    public void publishCommand(String json) {
        EventData eventData = EventData.fromJson(json, CreateHotelBookingCommand.class);
        CreateHotelBookingCommand command = (CreateHotelBookingCommand) eventData.getData();
        if (!command.getCustomerInfo().isPreviouslyCreated() && !this.customerService.validateCustomerSagaId(command.getCustomerInfo().getName(), eventData.getSagaId())) {
            this.jmsEventPublisher.publish(EventId.ROLLBACK_CREATE_HOTEL_BOOKING, eventData);
        }
    }
    
}
