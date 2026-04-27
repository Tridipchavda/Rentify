package com.project.rentify.service;

import com.project.rentify.models.RentalContracts;
import com.project.rentify.models.enums.ContractStatus;
import com.project.rentify.repository.PropertyRepo;
import com.project.rentify.repository.RentalContractRepo;
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
class RentalContractServiceTest {

    @Mock
    private RentalContractRepo rentalContractRepo;

    @InjectMocks
    private RentalContractService rentalContractService;

    // ---------------- CREATE ----------------
    @Test
    void testCreateRentalContract() {
        RentalContracts rc = new RentalContracts();

        when(rentalContractRepo.save(rc)).thenReturn(rc);

        RentalContracts result = rentalContractService.createRentalContract(rc);

        assertNotNull(result);
        verify(rentalContractRepo).save(rc);
    }

    // ---------------- GET ALL ----------------
    @Test
    void testGetAllRentalContracts() {
        when(rentalContractRepo.findAll())
                .thenReturn(List.of(new RentalContracts()));

        List<RentalContracts> result =
                rentalContractService.getAllRentalContracts();

        assertEquals(1, result.size());
        verify(rentalContractRepo).findAll();
    }

    // ---------------- UPDATE ----------------
    @Test
    void testUpdateRentalContract() {
        RentalContracts rc = new RentalContracts();

        when(rentalContractRepo.save(rc)).thenReturn(rc);

        RentalContracts result =
                rentalContractService.updateRentalContract(rc);

        assertNotNull(result);
        verify(rentalContractRepo).save(rc);
    }

    // ---------------- GET BY ID ----------------
    @Test
    void testGetRentalContractById_found() {
        RentalContracts rc = new RentalContracts();
        rc.setId(1);

        when(rentalContractRepo.findById(1))
                .thenReturn(Optional.of(rc));

        Optional<RentalContracts> result =
                rentalContractService.getRentalContractById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void testGetRentalContractById_notFound() {
        when(rentalContractRepo.findById(1))
                .thenReturn(Optional.empty());

        Optional<RentalContracts> result =
                rentalContractService.getRentalContractById(1);

        assertFalse(result.isPresent());
    }

    // ---------------- SEARCH SUCCESS ----------------
    @Test
    void testSearchContract_validStatus() {
        String search = "ACTIVE";

        when(rentalContractRepo.findAllByProperty_NameOrTenant_NameOrStatus(
                search, search, ContractStatus.ACTIVE))
                .thenReturn(List.of(new RentalContracts()));

        List<RentalContracts> result =
                rentalContractService.searchContract(search);

        assertEquals(1, result.size());

        verify(rentalContractRepo)
                .findAllByProperty_NameOrTenant_NameOrStatus(
                        search, search, ContractStatus.ACTIVE);
    }

    // ---------------- SEARCH INVALID STATUS ----------------
    @Test
    void testSearchContract_invalidStatus() {
        String search = "INVALID_STATUS";

        assertThrows(IllegalArgumentException.class, () -> {
            rentalContractService.searchContract(search);
        });

        verify(rentalContractRepo, never())
                .findAllByProperty_NameOrTenant_NameOrStatus(
                        any(), any(), any());
    }
}