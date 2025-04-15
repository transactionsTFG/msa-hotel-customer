package domainevent.command;

import javax.ejb.Local;
import javax.ejb.Stateless;

import business.customer.CustomerDTO;
import business.mapper.CustomerMapper;
import domainevent.command.handler.BaseHandler;
import domainevent.command.handler.CommandPublisher;
import msa.commons.event.EventData;
import msa.commons.event.EventId;
import msa.commons.microservices.hotelbooking.commandevent.CreateHotelBookingCommand;
import msa.commons.microservices.hotelcustomer.qualifier.CreateCustomerByCreateBookingEventCommitQualifier;

@Stateless
@CreateCustomerByCreateBookingEventCommitQualifier
@Local(CommandPublisher.class)
public class CreateCustomerByCreateBookingEventCommit extends BaseHandler {

    @Override
    public void publishCommand(String json) {
        EventData eventData = EventData.fromJson(json, CreateHotelBookingCommand.class);
        CreateHotelBookingCommand command = (CreateHotelBookingCommand) eventData.getData();
        CustomerDTO customerDTO = CustomerMapper.INSTANCE.customerInfoToDto(command.getCustomerInfo());

        customerDTO.setSagaId(eventData.getSagaId());

        if (!command.getCustomerInfo().isPreviouslyCreated()) {
            customerDTO = this.customerService.save(customerDTO);
        }

        command.getCustomerInfo().setIdCustomer(customerDTO.getId());
        this.jmsEventPublisher.publish(EventId.COMMIT_CREATE_HOTEL_BOOKING, eventData);
        
    }

}