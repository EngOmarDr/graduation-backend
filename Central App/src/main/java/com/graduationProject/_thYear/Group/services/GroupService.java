package com.graduationProject._thYear.Group.services;



import java.util.List;

import com.graduationProject._thYear.EventSyncronization.Records.ProductRecord.GroupRecord;
import com.graduationProject._thYear.Group.dtos.request.CreateGroupRequest;
import com.graduationProject._thYear.Group.dtos.request.UpdateGroupRequest;
import com.graduationProject._thYear.Group.dtos.response.GroupResponse;
import com.graduationProject._thYear.Group.dtos.response.GroupTreeResponse;
import com.graduationProject._thYear.Group.models.Group;

public interface GroupService {
    GroupResponse createGroup(CreateGroupRequest request);
    GroupResponse getGroupById(Integer id);
    List<GroupResponse> getAllGroups();
    List<GroupTreeResponse> getGroupTree();
    List<GroupResponse> getChildGroups(Integer parentId);
    GroupResponse updateGroup(Integer id, UpdateGroupRequest request);
    void deleteGroup(Integer id);

    List<GroupResponse> searchGroups(String searchTerm);
    Group saveOrUpdate(GroupRecord groupRecord);
}
