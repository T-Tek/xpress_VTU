package com.xpressvtu.xpressvtu.repository;

import com.xpressvtu.xpressvtu.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Optional<Merchant> findByEmail(String email);

}
