package com.mindex.challenge.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;

@RestController
@RequestMapping("/compensation")
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService compensationService;

    @PostMapping
    public Compensation create(@RequestBody Compensation compensation) {
        LOG.debug("Recieved compensation create request for [{}]", compensation);

        return compensationService.create(compensation);
    }

    @GetMapping("/{id}")
    public Compensation read(@PathVariable String id) {
        LOG.debug("Recieved compensation create request for id [{}]", id);

        return compensationService.read(id);
    }

    @GetMapping("/all/{id}")
    public List<Compensation> readAll(@PathVariable String id) {
        LOG.debug("Recieved compensation list create request for id [{}]", id);

        return compensationService.readAll(id);
    }
}
