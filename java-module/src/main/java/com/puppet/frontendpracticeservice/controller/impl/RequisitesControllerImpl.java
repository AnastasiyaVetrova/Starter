package com.puppet.frontendpracticeservice.controller.impl;

import com.puppet.frontendpracticeservice.controller.RequisitesController;
import com.puppet.frontendpracticeservice.domain.request.RequisitesDto;
import com.puppet.frontendpracticeservice.service.RequisitesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/frontend-practice/requisites")
@RequiredArgsConstructor
public class RequisitesControllerImpl implements RequisitesController {
    private final RequisitesService requisitesService;

    @GetMapping("/{user_id}")
    @Override
    public ResponseEntity<RequisitesDto> getRequisites(@PathVariable UUID user_id) {
        return ResponseEntity.ok(requisitesService.findRequisites(user_id));
    }
}