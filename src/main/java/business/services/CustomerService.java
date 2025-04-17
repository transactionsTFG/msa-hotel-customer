package business.services;

import business.customer.CustomerDTO;

public interface CustomerService {
    
    CustomerDTO save(CustomerDTO customerDTO);

    CustomerDTO getCustomerByDNI(String dni);

    CustomerDTO validateCustomer(CustomerDTO customerDTO);

    boolean validateCustomerSagaId(String dni, String sagaId);

    boolean remove(long customerId);

    void commitCreateCustomerByHotelBooking(long userId);

    void rollbackCreateCustomerByHotelBooking(long userId);
}