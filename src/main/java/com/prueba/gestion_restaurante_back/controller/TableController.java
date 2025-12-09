package com.prueba.gestion_restaurante_back.controller;

import com.prueba.gestion_restaurante_back.dto.TableDTO;
import com.prueba.gestion_restaurante_back.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://locahost:5173")
public class TableController {

    private final TableService tableService;

    @GetMapping
    public ResponseEntity<List<TableDTO>> getAllTables() {
        return ResponseEntity.ok(tableService.getAllTables());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableDTO> getTable(@PathVariable Long id) {
        return ResponseEntity.ok(tableService.getTableById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<List<TableDTO>> getAvailableTables(@RequestParam(required = false) Integer capacity) {
        if (capacity != null) {
            return ResponseEntity.ok(tableService.getAvalibleTables(capacity));
        }
        return ResponseEntity.ok(tableService.getAllTables());
    }

    @PostMapping
    public ResponseEntity<TableDTO> createTable(@RequestBody TableDTO tableDTO) {
        return ResponseEntity.ok(tableService.createTable(tableDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TableDTO> updateTable(@PathVariable Long id, @RequestBody TableDTO tableDTO) {
        return ResponseEntity.ok(tableService.updateTable(id, tableDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TableDTO> deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }
}