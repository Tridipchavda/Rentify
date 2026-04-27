package com.project.rentify.service;

import com.project.rentify.models.Owner;
import com.project.rentify.repository.OwnerRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {

    @Mock
    private OwnerRepo ownerRepo;

    @InjectMocks
    private OwnerService ownerService;

    // ---------------- GET ALL ----------------
    @Test
    void testGetAllOwner() {
        Owner owner = new Owner();
        owner.setId(1);

        when(ownerRepo.findAll()).thenReturn(List.of(owner));

        List<Owner> result = ownerService.getAllOwner();

        assertEquals(1, result.size());
        verify(ownerRepo, times(1)).findAll();
    }

    // ---------------- SEARCH ----------------
    @Test
    void testSearchOwner() {
        String search = "john";
        Owner owner = new Owner();

        when(ownerRepo.searchByNameContainingOrEmailContainingOrPhoneContaining(
                search, search, search))
                .thenReturn(List.of(owner));

        List<Owner> result = ownerService.searchOwner(search);

        assertEquals(1, result.size());

        verify(ownerRepo, times(1))
                .searchByNameContainingOrEmailContainingOrPhoneContaining(
                        search, search, search);
    }

    // ---------------- GET BY ID ----------------
    @Test
    void testGetOwnerById_found() {
        Owner owner = new Owner();
        owner.setId(1);

        when(ownerRepo.findById(1)).thenReturn(Optional.of(owner));

        Optional<Owner> result = ownerService.getOwnerById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void testGetOwnerById_notFound() {
        when(ownerRepo.findById(1)).thenReturn(Optional.empty());

        Optional<Owner> result = ownerService.getOwnerById(1);

        assertFalse(result.isPresent());
    }

    // ---------------- CREATE ----------------
    @Test
    void testCreateOwner() {
        Owner owner = new Owner();
        owner.setName("John");

        when(ownerRepo.save(owner)).thenReturn(owner);

        Owner result = ownerService.createOwner(owner);

        assertNotNull(result);
        assertEquals("John", result.getName());

        verify(ownerRepo, times(1)).save(owner);
    }

    // ---------------- UPDATE ----------------
    @Test
    void testUpdateOwner() {
        Owner owner = new Owner();
        owner.setId(1);
        owner.setName("Updated");

        when(ownerRepo.save(owner)).thenReturn(owner);

        Owner result = ownerService.updateOwner(owner);

        assertNotNull(result);
        assertEquals("Updated", result.getName());

        verify(ownerRepo, times(1)).save(owner);
    }
}