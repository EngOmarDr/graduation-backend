package com.graduationProject._thYear.Group.services;


import com.graduationProject._thYear.Group.dtos.request.CreateGroupRequest;
import com.graduationProject._thYear.Group.dtos.request.UpdateGroupRequest;
import com.graduationProject._thYear.Group.dtos.response.GroupResponse;
import com.graduationProject._thYear.Group.dtos.response.GroupTreeResponse;
import com.graduationProject._thYear.Group.models.Group;
import com.graduationProject._thYear.Group.repositories.GroupRepository;
import com.graduationProject._thYear.exceptionHandler.ResourceHasChildrenException;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;

    @Override
    public GroupResponse createGroup(CreateGroupRequest request) {

// Validate name and code uniqueness
        if (groupRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Group item with code '" + request.getCode() + "' already exists");
        }
        if (groupRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Group item with name '" + request.getName() + "' already exists");
        }

        Group group = new Group();
        group.setCode(request.getCode());
        group.setName(request.getName());
        group.setNotes(request.getNotes());

        if (request.getParentId() != null) {
            Group parent = groupRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent group not found"));
            group.setParent(parent);
        }else {
            group.setParent(null);
        }

        Group savedGroup = groupRepository.save(group);
        return convertToResponse(savedGroup);
    }



    @Override
    public GroupResponse getGroupById(Integer id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        return convertToResponse(group);
    }

    @Override
    public List<GroupResponse> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public List<GroupTreeResponse> getGroupTree() {
        List<Group> rootGroups = groupRepository.findByParentIsNull();
        return rootGroups.stream()
                .map(this::convertToTreeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupResponse> getChildGroups(Integer parentId) {
        return groupRepository.findByParentId(parentId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GroupResponse updateGroup(Integer id, UpdateGroupRequest request) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        // Check and update code if provided
        if (request.getCode() != null && !group.getCode().equals(request.getCode())) {
            if (groupRepository.existsByCode(request.getCode())) {
                throw new IllegalArgumentException("Group item with code '" + request.getCode() + "' already exists");
            }
            group.setCode(request.getCode());
        }

        // Check and update name if provided
        if (request.getName() != null && !group.getName().equals(request.getName())) {
            if (groupRepository.existsByName(request.getName())) {
                throw new IllegalArgumentException("Group item with name '" + request.getName() + "' already exists");
            }
            group.setName(request.getName());
        }

        // Update notes if present
        if (request.getNotes() != null) {
            group.setNotes(request.getNotes());
        }

        // Update parent if present
        if (request.getParentId() != null) {
            Group parent = groupRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent group not found"));
            group.setParent(parent);
        }

        Group updatedGroup = groupRepository.save(group);
        return convertToResponse(updatedGroup);
    }


    @Override
    public void deleteGroup(Integer id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        if (!group.getChildren().isEmpty()) {
            throw new ResourceHasChildrenException("Group can't be deleted because it's associated with child group resources, cosider deleting them manualy first");
        }
        group.getChildren().forEach(child -> {
            child.setParent(group.getParent());
            groupRepository.save(child);
        });


        groupRepository.delete(group);
    }

    public List<GroupResponse> searchGroups(String searchTerm) {
        return groupRepository.searchByNameOrCode(searchTerm).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private GroupResponse convertToResponse(Group group) {
        return GroupResponse.builder()
                .id(group.getId())
                .code(group.getCode())
                .name(group.getName())
                .notes(group.getNotes())
                .parentId(group.getParent() != null ? group.getParent().getId() : null)
                .parentName(group.getParent() != null ? group.getParent().getName() : null)
                .build();
    }

    private GroupTreeResponse convertToTreeResponse(Group group) {
        return GroupTreeResponse.builder()
                .id(group.getId())
                .code(group.getCode())
                .name(group.getName())
                .notes(group.getNotes())
                .children(group.getChildren().stream()
                        .map(this::convertToTreeResponse)
                        .collect(Collectors.toList()))
                .build();
    }
}
