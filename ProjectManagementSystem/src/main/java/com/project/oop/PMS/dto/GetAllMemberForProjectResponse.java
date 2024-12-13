package com.project.oop.PMS.dto;

import com.project.oop.PMS.entity.MemberProject;
import com.project.oop.PMS.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Member;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllMemberForProjectResponse {

    private Integer userId;
    private String username;
    private String role;
    private String message;
    private Date joinDate;
    public static GetAllMemberForProjectResponse fromEntity (MemberProject member) {
        GetAllMemberForProjectResponse MemberResponse = new GetAllMemberForProjectResponse();
        MemberResponse.setUserId(member.getUser().getUserId());
        MemberResponse.setUsername(member.getUser().getUsername());
        MemberResponse.setRole(member.getRole());
        MemberResponse.setJoinDate(member.getJoinDate());
        return MemberResponse;
    }
}
