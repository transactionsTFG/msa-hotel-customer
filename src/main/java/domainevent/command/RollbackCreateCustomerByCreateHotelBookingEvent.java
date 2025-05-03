package domainevent.command;

import javax.ejb.Local;
import javax.ejb.Stateless;

import business.qualifier.RollbackCreateCustomerByCreateHotelBookingEventQualifier;
import domainevent.command.handler.BaseHandler;
import domainevent.command.handler.CommandPublisher;
import msa.commons.commands.hotelbooking.CreateHotelBookingCommand;
import msa.commons.event.EventData;
import msa.commons.event.EventId;

@Stateless
@RollbackCreateCustomerByCreateHotelBookingEventQualifier
@Local(CommandPublisher.class)
public class RollbackCreateCustomerByCreateHotelBookingEvent extends BaseHandler {

    @Override
    public void publishCommand(String json) {
        EventData eventData = EventData.fromJson(json, CreateHotelBookingCommand.class);
        CreateHotelBookingCommand command = (CreateHotelBookingCommand) eventData.getData();
        if (!command.getCustomerInfo().isPreviouslyCreated() && !this.customerService.validateCustomerSagaId(command.getCustomerInfo().getName(), eventData.getSagaId())) {
            this.jmsEventPublisher.publish(EventId.ROLLBACK_CREATE_HOTEL_BOOKING, eventData);
        }
    }
    
}
