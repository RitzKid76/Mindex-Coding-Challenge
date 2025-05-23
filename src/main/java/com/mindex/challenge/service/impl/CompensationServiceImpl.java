package com.mindex.challenge.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public Compensation read(String id) {
        LOG.debug("Creating compensation with id [{}]", id);

        Compensation compensation = compensationRepository.findTopByEmployeeIdOrderByEffectiveDateDesc(id);

        if (compensation == null)
            throw new RuntimeException("Invalid employeeId: " + id);

        return compensation;
    }

    @Override
    public List<Compensation> readAll(String id) {
        LOG.debug("Creating compensation list with id [{}]", id);

        List<Compensation> compensations = compensationRepository.findAllByEmployeeId(id);

        if (compensations == null)
            throw new RuntimeException("Invalid employeeId: " + id);

        return compensations;
    }
}
