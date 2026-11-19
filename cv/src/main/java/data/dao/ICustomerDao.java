package data.dao;

import data.model.CustomerEntity;
import java.util.Optional;

public interface ICustomerDao {
    CustomerEntity create(CustomerEntity customer);
    Optional<CustomerEntity> getById(Long id);
}