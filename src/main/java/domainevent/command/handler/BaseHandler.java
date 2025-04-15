package domainevent.command.handler;

import javax.ejb.EJB;
import javax.inject.Inject;

import com.google.gson.Gson;

import business.services.CustomerService;
import domainevent.publisher.IJMSEventPublisher;

public abstract class BaseHandler implements CommandPublisher {
    protected CustomerService customerService;
    protected IJMSEventPublisher jmsEventPublisher;
    protected Gson gson;

    @EJB
    public void setTypeUserServices(CustomerService customerService) {
        this.customerService = customerService;
    }

    @EJB
    public void setJmsEventPublisher(IJMSEventPublisher jmsEventPublisher) {
        this.jmsEventPublisher = jmsEventPublisher;
    }

    @Inject
    public void setGson(Gson gson) {
        this.gson = gson;
    }
}