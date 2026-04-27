package com.project.rentify.service;

import com.project.rentify.models.Property;
import com.project.rentify.repository.PropertyRepo;
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
class PropertyServiceTest {

    @Mock
    private PropertyRepo propertyRepo;

    @InjectMocks
    private PropertyService propertyService;

    // ---------------- GET ALL ----------------
    @Test
    void testGetAllProperty() {
        Property property = new Property();

        when(propertyRepo.findAll()).thenReturn(List.of(property));

        List<Property> result = propertyService.getAllProperty();

        assertEquals(1, result.size());
        verify(propertyRepo, times(1)).findAll();
    }

    // ---------------- SEARCH ----------------
    @Test
    void testSearchProperty() {
        String search = "Ahmedabad";
        Property property = new Property();

        when(propertyRepo.searchByNameContainingOrAddressContainingOrCityContaining(
                search, search, search))
                .thenReturn(List.of(property));

        List<Property> result = propertyService.searchProperty(search);

        assertEquals(1, result.size());

        verify(propertyRepo, times(1))
                .searchByNameContainingOrAddressContainingOrCityContaining(
                        search, search, search);
    }

    // ---------------- GET BY OWNER ----------------
    @Test
    void testGetPropertiesByOwner() {
        int ownerId = 10;
        Property property = new Property();

        when(propertyRepo.getPropertiesByOwnerId(ownerId))
                .thenReturn(List.of(property));

        List<Property> result = propertyService.getPropertiesByOwner(ownerId);

        assertEquals(1, result.size());
        verify(propertyRepo).getPropertiesByOwnerId(ownerId);
    }

    // ---------------- GET BY ID ----------------
    @Test
    void testGetPropertyById_found() {
        Property property = new Property();
        property.setId(1);

        when(propertyRepo.findById(1)).thenReturn(Optional.of(property));

        Optional<Property> result = propertyService.getPropertyById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void testGetPropertyById_notFound() {
        when(propertyRepo.findById(1)).thenReturn(Optional.empty());

        Optional<Property> result = propertyService.getPropertyById(1);

        assertFalse(result.isPresent());
    }

    // ---------------- CREATE ----------------
    @Test
    void testCreateProperty() {
        Property property = new Property();
        property.setName("Flat A");

        when(propertyRepo.save(property)).thenReturn(property);

        Property result = propertyService.createProperty(property);

        assertNotNull(result);
        assertEquals("Flat A", result.getName());

        verify(propertyRepo).save(property);
    }

    // ---------------- UPDATE ----------------
    @Test
    void testUpdateProperty() {
        Property property = new Property();
        property.setId(1);
        property.setName("Updated Flat");

        when(propertyRepo.save(property)).thenReturn(property);

        Property result = propertyService.updateProperty(property);

        assertNotNull(result);
        assertEquals("Updated Flat", result.getName());

        verify(propertyRepo).save(property);
    }
}