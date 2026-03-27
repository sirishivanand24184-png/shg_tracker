package com.shg.controller;

import com.shg.model.SHGMember;
import com.shg.service.SHGMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/members")
@CrossOrigin(origins = "*")
public class SHGMemberController {
    
    @Autowired
    private SHGMemberService memberService;
    
    @PostMapping
    public ResponseEntity<SHGMember> createMember(@RequestBody SHGMember member) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.createMember(member));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SHGMember> getMemberById(@PathVariable Long id) {
        Optional<SHGMember> member = memberService.getMemberById(id);
        return member.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<SHGMember>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<SHGMember> getMemberByUsername(@PathVariable String username) {
        Optional<SHGMember> member = memberService.getMemberByUsername(username);
        return member.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping("/shg-group/{shgGroupId}")
    public ResponseEntity<List<SHGMember>> getMembersByShgGroupId(@PathVariable Long shgGroupId) {
        return ResponseEntity.ok(memberService.getMembersByShgGroupId(shgGroupId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SHGMember> updateMember(@PathVariable Long id, @RequestBody SHGMember updatedMember) {
        return ResponseEntity.ok(memberService.updateMember(id, updatedMember));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}