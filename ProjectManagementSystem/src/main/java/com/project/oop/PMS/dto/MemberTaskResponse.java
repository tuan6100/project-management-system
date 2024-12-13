package com.project.oop.PMS.dto;

import com.project.oop.PMS.entity.MemberTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberTaskResponse {

    private Integer memberId;
    private String memberName;
    private Boolean isCompleted;
    private Date completedDate;


    public static MemberTaskResponse fromEntity(MemberTask memberTask) {
        MemberTaskResponse memberTaskResponse = new MemberTaskResponse();
        memberTaskResponse.setMemberId(memberTask.getMember().getUserId());
        memberTaskResponse.setMemberName(memberTask.getMember().getUsername());
        memberTaskResponse.setIsCompleted(memberTask.getIs_completed());
        memberTaskResponse.setCompletedDate(memberTask.getCompletedDate());
        return memberTaskResponse;
    }

}
