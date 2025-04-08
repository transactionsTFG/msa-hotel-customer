package business.services;

import business.customer.CustomerDTO;

public interface CustomerService {
    CustomerDTO getCustomerByDNI(String dni);
    CustomerDTO save(CustomerDTO customerDTO);
}