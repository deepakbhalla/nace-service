package com.nace.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nace.entities.NaceDetailsEntity;

/**
 * Repository interface for Nace Details.
 * 
 * @author Deepak Bhalla
 *
 */
@Repository
public interface NaceDetailsRepository extends JpaRepository<NaceDetailsEntity, Long> {

    public List<NaceDetailsEntity> findByOrder(Long order);

    public void deleteByOrder(Long order);
}
