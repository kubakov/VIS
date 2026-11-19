package data.dao;

import data.model.AddressEntity;
import java.util.Optional;

public interface IAddressDao {
    AddressEntity create(AddressEntity address);
    Optional<AddressEntity> getById(Long id);
}