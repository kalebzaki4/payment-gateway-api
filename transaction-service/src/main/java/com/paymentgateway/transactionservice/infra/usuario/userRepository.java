package com.paymentgateway.transactionservice.infra.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface userRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
}
