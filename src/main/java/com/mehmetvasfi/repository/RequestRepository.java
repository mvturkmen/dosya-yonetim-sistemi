package com.mehmetvasfi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mehmetvasfi.model.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findByApproved(boolean approved);
}
