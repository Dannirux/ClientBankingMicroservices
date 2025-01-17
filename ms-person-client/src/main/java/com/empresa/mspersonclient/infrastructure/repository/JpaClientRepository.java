package com.empresa.mspersonclient.infrastructure.repository;

import com.empresa.mspersonclient.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaClientRepository extends JpaRepository<Client, String> {
}
