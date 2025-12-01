package com.prueba.gestion_restaurante_back.service;

import com.prueba.gestion_restaurante_back.dto.TableDTO;
import com.prueba.gestion_restaurante_back.model.table.RestaurantTable;
import com.prueba.gestion_restaurante_back.model.table.TableStatus;
import com.prueba.gestion_restaurante_back.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TableService {

    private final RestaurantTableRepository tableRepository;

    @Transactional
    public TableDTO createTable(TableDTO tableDTO) {
        if (tableRepository.existsByTableNumber(tableDTO.getTableNumber())) {
            throw new RuntimeException("Ya la mesa se encuentra registrada");
        }

        RestaurantTable table = convertToEntity(tableDTO);
        RestaurantTable saved = tableRepository.save(table);
        return convertToDTO(saved);

    }

    @Transactional
    public TableDTO updateTable(Long id, TableDTO tableDTO) {
        RestaurantTable table = tableRepository.findById(id).orElseThrow(() -> new RuntimeException());

        table.setTableNumber(tableDTO.getTableNumber());
        table.setCapacity(tableDTO.getCapacity());
        table.setStatus(tableDTO.getStatus());
        table.setIsTableVip(tableDTO.getIsTableVip());

        RestaurantTable updatedTable = tableRepository.save(table);
        return convertToDTO(table);
    }

    @Transactional
    public void deleteTable(Long id) {
        if (!tableRepository.existsById(id)) {
            throw new RuntimeException();
        }
        tableRepository.deleteById(id);
    }

    public List<TableDTO> getAllTables() {
        return tableRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public TableDTO getTableById(Long id) {
        RestaurantTable table = tableRepository.findById(id).orElseThrow(() -> new RuntimeException("No existe una mesa con ese id"));
        return convertToDTO(table);
    }

    public List<TableDTO> getAvalibleTables(Integer capacity) {
        return tableRepository.findByCapacity(capacity, TableStatus.DISPONIBLE).stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public void updateTableStatus(Long tableId, TableStatus status) {
        RestaurantTable table = tableRepository.findById(tableId).orElseThrow(() -> new RuntimeException("No existe una mesa con ese id"));
        table.setStatus(status);
        tableRepository.save(table);
    }

    private RestaurantTable convertToEntity(TableDTO tableDTO) {
        RestaurantTable table = new RestaurantTable();
        table.setId(tableDTO.getId());
        table.setTableNumber(tableDTO.getTableNumber());
        table.setCapacity(tableDTO.getCapacity());
        table.setStatus(tableDTO.getStatus());
        table.setStatus(tableDTO.getStatus() != null ? tableDTO.getStatus() : TableStatus.DISPONIBLE);
        table.setIsTableVip(tableDTO.getIsTableVip() != null ? tableDTO.getIsTableVip() : false);

        return table;
    }

    private TableDTO convertToDTO(RestaurantTable table) {
        return new TableDTO(
                table.getId(),
                table.getTableNumber(),
                table.getCapacity(),
                table.getStatus(),
                table.getIsTableVip()
        );
    }
}
