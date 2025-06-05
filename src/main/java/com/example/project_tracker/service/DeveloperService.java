package com.example.project_tracker.service;

import com.example.project_tracker.DTO.request.DeveloperRequestDTO;
import com.example.project_tracker.DTO.response.DeveloperResponseDTO;
import com.example.project_tracker.exceptions.DeveloperNotFoundException;
import com.example.project_tracker.mapper.DeveloperMapper;
import com.example.project_tracker.models.Developer;
import com.example.project_tracker.repository.DeveloperRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class DeveloperService {

    private DeveloperRepository developerRepository;
    private final AuditLogService auditLogService;


    public DeveloperService(DeveloperRepository developerRepository, AuditLogService auditLogService){
        this.developerRepository = developerRepository;
        this.auditLogService = auditLogService;
    }

    public DeveloperResponseDTO createDeveloper(@Valid DeveloperRequestDTO requestDTO) {
        Developer developer = Developer.builder()
                .name(requestDTO.getName())
                .email(requestDTO.getEmail())
                .skills(requestDTO.getSkills())
                .build();

        Developer saved = developerRepository.save(developer);
        auditLogService.logAction("CREATE", "Developer", saved.getId().toString(), saved.getName().toString(), saved);
        return DeveloperMapper.toDTO(saved);
    }

    public Map<String, Object> getAllDevelopers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Developer> developerPage = developerRepository.findAll(pageable);

        List<DeveloperResponseDTO> developerDTOs = developerPage.getContent()
                .stream()
                .map(task -> DeveloperMapper.toDTO(task))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("developers", developerDTOs);
        response.put("currentPage", developerPage.getNumber());
        response.put("totalItems", developerPage.getTotalElements());
        response.put("totalPages", developerPage.getTotalPages());

        return response;

    }

    public DeveloperResponseDTO getDeveloperById(Long id) {
        Developer developer = developerRepository.findById(id)
                .orElseThrow(() -> new DeveloperNotFoundException("Developer with ID " + id + " not found"));
        return DeveloperMapper.toDTO(developer);
    }

    public DeveloperResponseDTO updateDeveloper(Long id, @Valid DeveloperRequestDTO requestDTO) {
        Developer existing = developerRepository.findById(id)
                .orElseThrow(() -> new DeveloperNotFoundException("Developer with ID " + id + " not found"));

        existing.setName(requestDTO.getName());
        existing.setEmail(requestDTO.getEmail());
        existing.setSkills(requestDTO.getSkills());

        Developer updated = developerRepository.save(existing);
        auditLogService.logAction("UPDATE", "Developer", updated.getId().toString(), updated.getName().toString(), updated);
        return DeveloperMapper.toDTO(updated);
    }

    public void deleteDeveloper(Long id) {
        Developer existing = developerRepository.findById(id)
                .orElseThrow(() -> new DeveloperNotFoundException("Developer with ID " + id + " not found"));
        auditLogService.logAction("DELETE", "Developer", existing.getId().toString(), existing.getName().toString(), existing);
        developerRepository.delete(existing);
    }

}
