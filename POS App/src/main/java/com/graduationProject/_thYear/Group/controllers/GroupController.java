package com.graduationProject._thYear.Group.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.graduationProject._thYear.Group.dtos.request.CreateGroupRequest;
import com.graduationProject._thYear.Group.dtos.request.UpdateGroupRequest;
import com.graduationProject._thYear.Group.dtos.response.GroupResponse;
import com.graduationProject._thYear.Group.dtos.response.GroupTreeResponse;
import com.graduationProject._thYear.Group.services.GroupService;

import java.util.List;
@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(
            @Valid @RequestBody CreateGroupRequest request) {
        GroupResponse response = groupService.createGroup(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponse> getGroupById(@PathVariable Integer groupId) {
        GroupResponse response = groupService.getGroupById(groupId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<GroupResponse>> getAllGroups() {
        List<GroupResponse> responses = groupService.getAllGroups();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/tree")
    public ResponseEntity<List<GroupTreeResponse>> getGroupTree() {
        List<GroupTreeResponse> tree = groupService.getGroupTree();
        return ResponseEntity.ok(tree);
    }

    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<GroupResponse>> getChildGroups(
            @PathVariable Integer parentId) {
        List<GroupResponse> responses = groupService.getChildGroups(parentId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<GroupResponse> updateGroup(
            @PathVariable Integer groupId,
            @Valid @RequestBody UpdateGroupRequest request) {
        GroupResponse response = groupService.updateGroup(groupId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Integer groupId) {
        groupService.deleteGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<GroupResponse>> searchAccounts(@RequestParam String q) {
        var responses = groupService.searchGroups(q);
        return ResponseEntity.ok(responses);
    }

}
