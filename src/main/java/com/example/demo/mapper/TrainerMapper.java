package com.example.demo.mapper;

import com.example.demo.entity.User;
import com.example.demo.request.ScheduleRequest;
import com.example.demo.response.ScheduleResponse;
import com.example.demo.response.TrainerDetailResponse;
import com.example.demo.response.UserListResponse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface TrainerMapper {

    @Select("""
            SELECT m.id, m.name, t.info, c.name as centerName, c.address as centerAddress
            FROM `MEMBER` m
            LEFT JOIN TRAINER t ON m.id = t.memberId
            LEFT JOIN CENTER c ON t.centerId = c.id
            WHERE m.id = #{id};
            """)
    List<TrainerDetailResponse> findByAuthTrainerTId(Long id);

    @Select("""
            SELECT m.id, m.name, t.info, c.name as centerName, c.address as centerAddress
            FROM `MEMBER` m
            LEFT JOIN TRAINER t ON m.id = t.memberId
            LEFT JOIN CENTER c ON t.centerId = c.id
            WHERE t.id = #{id};
            """)
    List<TrainerDetailResponse> findByAuthTrainerUId(Long id);

    @Select("""
            SELECT * FROM MEMBER WHERE id = #{id};
            """)
    User getTrainerById(Long id);

    @Select("""
            SELECT m.id, m.name, ms.remainingPT, m.authority
            FROM `MEMBER` m
            LEFT JOIN
            	TRAINER t ON m.trainerId = t.id
            LEFT JOIN
            	MEMBERSHIP ms ON m.id = ms.memberId
            WHERE t.memberId = #{id};
            """)
    List<UserListResponse> selectResponsibleUserList(Integer id);

    @Insert("""
            INSERT INTO SCHEDULE (memberId, ptTime)
            VALUES (#{memberId}, #{ptTime})
            """)
    Integer insertSchedule(ScheduleRequest scheduleRequest);

//    Optional<ScheduleResponse> findByMemberId(Integer memberId);

    @Select("""
            SELECT * FROM SCHEDULE s
            WHERE memberId = #{memberId} && ptTime = #{ptTime}
            """)
    Optional<ScheduleResponse> findByMemberId(Integer memberId, LocalDateTime ptTime);

    @Select("""
            SELECT m.name as memberName, s.ptTime, ms.remainingPT
            FROM SCHEDULE s
            LEFT JOIN MEMBER m ON m.id = s.memberId
            LEFT JOIN MEMBERSHIP ms ON m.id = ms.memberId
            WHERE DAY(s.ptTime) = #{dayOfMonth}
            ORDER BY s.ptTime ASC;
            """)
    List<ScheduleResponse> findByIdWithRes(int dayOfMonth);

    @Select("""
            SELECT ptTime
            FROM SCHEDULE;
            """)
    ScheduleResponse getPtTime();
}

