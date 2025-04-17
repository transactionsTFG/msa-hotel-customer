package business.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import business.customer.Customer;
import business.customer.CustomerDTO;
import business.mapper.CustomerMapper;

@Stateless
public class CustomerServiceImpl implements CustomerService {

    private EntityManager entityManager;

    @Override
    public CustomerDTO getCustomerByDNI(String dni) {
        List<Customer> listCustomer = this.entityManager.createNamedQuery("customer.findByDNI", Customer.class)
                .setParameter("dni", dni)
                .setLockMode(LockModeType.OPTIMISTIC)
                .getResultList();
        return listCustomer.isEmpty() ? null : CustomerMapper.INSTANCE.entityToDto(listCustomer.get(0));
    }

    @Override
    public CustomerDTO validateCustomer(CustomerDTO customerDTO) {
        List<Customer> listCustomer = this.entityManager.createNamedQuery("customer.findByDNI", Customer.class)
                .setParameter("dni", customerDTO.getDni())
                .setLockMode(LockModeType.OPTIMISTIC)
                .getResultList();

        Customer customer = listCustomer.isEmpty() ? null : listCustomer.get(0);

        if (customer == null) {
            customer = Customer.builder()
                    .dni(customerDTO.getDni())
                    .name(customerDTO.getName())
                    .email(customerDTO.getEmail())
                    .phone(customerDTO.getPhone())
                    .sagaId(customerDTO.getSagaId())
                    .active(true)
                    .build();
            this.entityManager.persist(customer);
            this.entityManager.flush();
        } else {
            if (!customer.isActive()) 
                return null;
        }

        return CustomerMapper.INSTANCE.entityToDto(customer);
    }

    @Override
    public CustomerDTO save(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setDni(customerDTO.getDni());
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        this.entityManager.persist(customer);
        this.entityManager.flush();
        return CustomerMapper.INSTANCE.entityToDto(customer);
    }

    @Inject
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean validateCustomerSagaId(String dni, String sagaId) {
        List<Customer> listCustomer = this.entityManager.createNamedQuery("customer.findByDNI", Customer.class)
                .setParameter("dni", dni)
                .setLockMode(LockModeType.OPTIMISTIC)
                .getResultList();
        return !listCustomer.isEmpty() && sagaId.equals(listCustomer.get(0).getSagaId());
    }

    @Override
    public boolean remove(long customerId) {
        Customer c = this.entityManager.find(Customer.class, customerId, LockModeType.OPTIMISTIC);
        if (c != null)
            this.entityManager.remove(c);
        return c != null;
    }

    @Override
    public void commitCreateCustomerByHotelBooking(long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'commitCreateCustomerByHotelBooking'");
    }

    @Override
    public void rollbackCreateCustomerByHotelBooking(long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rollbackCreateCustomerByHotelBooking'");
    }

}