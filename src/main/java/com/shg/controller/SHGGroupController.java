package com.shg.controller;

import com.shg.model.SHGGroup;
import com.shg.service.SHGGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/groups")
@CrossOrigin(origins = "*")
public class SHGGroupController {
    
    @Autowired
    private SHGGroupService shgGroupService;
    
    @PostMapping
    public ResponseEntity<SHGGroup> createSHGGroup(@RequestBody SHGGroup shgGroup) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shgGroupService.createSHGGroup(shgGroup));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SHGGroup> getSHGGroupById(@PathVariable Long id) {
        Optional<SHGGroup> group = shgGroupService.getSHGGroupById(id);
        return group.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<SHGGroup>> getAllSHGGroups() {
        return ResponseEntity.ok(shgGroupService.getAllSHGGroups());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SHGGroup> updateSHGGroup(@PathVariable Long id, @RequestBody SHGGroup updatedGroup) {
        return ResponseEntity.ok(shgGroupService.updateSHGGroup(id, updatedGroup));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSHGGroup(@PathVariable Long id) {
        shgGroupService.deleteSHGGroup(id);
        return ResponseEntity.noContent().build();
    }
}