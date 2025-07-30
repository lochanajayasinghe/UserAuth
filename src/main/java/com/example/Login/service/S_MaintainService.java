package com.example.Login.service;

import com.example.Login.model.Maintain;
import com.example.Login.repository.S_MaintainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class S_MaintainService {

    @Autowired
    private S_MaintainRepository maintainRepository;

    // Get all maintenance records
    public List<Maintain> getAllMaintains() {
        return maintainRepository.findAll();
    }

    // Save a new or updated maintenance record
    public void saveMaintain(Maintain maintain) {
        maintainRepository.save(maintain);
    }

    // Get a single maintenance record by ID
    public Optional<Maintain> getMaintainById(String maintainId) {
        return maintainRepository.findById(maintainId);
    }

    // Delete a maintenance record
    public void deleteMaintain(String maintainId) {
        maintainRepository.deleteById(maintainId);
    }
}
