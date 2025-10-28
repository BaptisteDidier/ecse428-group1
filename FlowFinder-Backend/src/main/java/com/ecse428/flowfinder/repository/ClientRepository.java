package com.ecse428.flowfinder.repository;

import org.springframework.data.repository.CrudRepository;

import com.ecse428.flowfinder.model.Client;

public interface ClientRepository extends CrudRepository<Client, Integer> {
    public Client findClientById(int id);
    public boolean existsById(int id);
    public Client findClientByEmail(String email);
    public boolean existsByEmail(String email);
}
