package com.example.project_tracker;

import com.example.project_tracker.DTO.response.DeveloperResponseDTO;
import com.example.project_tracker.models.Developer;
import com.example.project_tracker.mapper.DeveloperMapper;
import com.example.project_tracker.models.Task;
import com.example.project_tracker.repository.DeveloperRepository;
import com.example.project_tracker.service.DeveloperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeveloperServiceTest {

    @Mock
    private DeveloperRepository developerRepository;

    @InjectMocks
    private DeveloperService developerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDevelopers() {
        int page = 0;
        int size = 2;

        Set<String> skills1 = new HashSet<>(Set.of("Java"));
        Set<Task> tasks1 = new HashSet<>(); // empty or add sample tasks

        Set<String> skills2 = new HashSet<>(Set.of("Python"));
        Set<Task> tasks2 = new HashSet<>();

        Developer dev1 = new Developer(1L, "Alice", "alice@example.com", skills1, tasks1);
        Developer dev2 = new Developer(2L, "Bob", "bob@example.com", skills2, tasks2);

        List<Developer> developerList = Arrays.asList(dev1, dev2);
        Page<Developer> developerPage = new PageImpl<>(developerList, PageRequest.of(page, size), developerList.size());

        when(developerRepository.findAll(PageRequest.of(page, size))).thenReturn(developerPage);

        try (MockedStatic<DeveloperMapper> mockedMapper = mockStatic(DeveloperMapper.class)) {
            mockedMapper.when(() -> DeveloperMapper.toDTO(dev1))
                    .thenReturn(new DeveloperResponseDTO(1L, "Alice", "alice@example.com", skills1));
            mockedMapper.when(() -> DeveloperMapper.toDTO(dev2))
                    .thenReturn(new DeveloperResponseDTO(2L, "Bob", "bob@example.com", skills2));

            Map<String, Object> response = developerService.getAllDevelopers(page, size);

            List<?> developers = (List<?>) response.get("developers");
            assertEquals(2, developers.size());
            assertEquals(0, response.get("currentPage"));
            assertEquals(2L, response.get("totalItems"));
            assertEquals(1, response.get("totalPages"));
        }

    }
}
