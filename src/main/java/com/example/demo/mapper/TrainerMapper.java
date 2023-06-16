package com.example.demo.mapper;

import com.example.demo.entity.Schedule;
import com.example.demo.entity.User;
import com.example.demo.request.DateRequest;
import com.example.demo.request.ScheduleRequest;
import com.example.demo.request.ScheduleUpdateRequest;
import com.example.demo.response.ScheduleResponse;
import com.example.demo.response.TrainerDetailResponse;
import com.example.demo.response.UserListResponse;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface TrainerMapper {

    @Select("""
            SELECT 
                m.id
                , m.name
                , t.centerId
                , t.memberId
                , t.info as trainerInfo
                , c.info as centerInfo
                , c.name as centerName
                , c.address as centerAddress
                , m.authority
            FROM `MEMBER` m
            LEFT JOIN TRAINER t ON m.id = t.memberId
            LEFT JOIN CENTER c ON t.centerId = c.id
            WHERE m.id = #{id};
            """)
    List<TrainerDetailResponse> findByAuthTrainerTId(Long id);

    @Select("""
            SELECT 
                m.id
                , m.name
                , t.info as trainerInfo
                , c.info as centerInfo
                , c.name as centerName
                , c.address as centerAddress
                , m.authority
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
            SELECT 
                m.id
                , m.name
                , ms.remainingPT
                , m.authority
            FROM MEMBER m
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
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insertSchedule(ScheduleRequest scheduleRequest);

//    Optional<ScheduleResponse> findByMemberId(Integer memberId);

    @Select("""
            SELECT * FROM SCHEDULE
            WHERE memberId = #{memberId} && ptTime = #{pt}
            """)
    Optional<ScheduleResponse> findByMemberId(@Param("memberId") Integer memberId, @Param("pt") LocalDateTime pt);

    @Select("""
            SELECT 
                s.id
                , s.ptTime
                , ms.remainingPT
                , t.memberId            AS trainerId
                , m.id                  AS memberId
                , m.name                AS memberName
            FROM 
                SCHEDULE s
            LEFT JOIN 
                MEMBER m ON m.id = s.memberId
            LEFT JOIN 
                TRAINER t ON t.id = m.trainerId
            LEFT JOIN 
                MEMBERSHIP ms ON m.id = ms.memberId
            WHERE 
                DAY(s.ptTime) = #{day}
            AND
                MONTH(s.ptTime) = #{month}
            AND
                YEAR(s.ptTIme) = #{year}
            AND
                t.memberId = #{id}
            ORDER BY 
                s.ptTime ASC;
            """)
    List<Schedule> findByIdWithRes(@Param("day") int day, @Param("year") int year, @Param("month") int month, @Param("id") Long id);

    @Select("""
            SELECT ptTime
            FROM SCHEDULE;
            """)
    ScheduleResponse getPtTime();


    @Delete("""
            DELETE FROM SCHEDULE
            WHERE id = #{id}
            """)
    Integer deleteById(ScheduleRequest scheduleRequest);

    @Update("""
            UPDATE MEMBERSHIP
            SET remainingPT = remainingPT - 1
            WHERE memberId = #{memberId};
            """)
    Integer updateMembershipPT(ScheduleRequest scheduleRequest);

    @Update("""
            UPDATE MEMBERSHIP
            SET remainingPT = remainingPT + 1
            WHERE memberId = #{memberId};
            """)
    Integer deleteMembershipPT(ScheduleRequest scheduleRequest);

    @Select("""
            SELECT
                m.id
                , m.name
                , t.centerId
                , t.memberId
                , t.info 		AS trainerInfo
                , t.infoDetail
                , c.name 		AS centerName
                , c.info 		AS centerInfo
                , c.address 	AS centerAddress
                , m.authority
                , tf.id			AS trainerFileId
                , tf.trainerId
                , tf.fileName
            FROM
                `MEMBER` m
            LEFT JOIN
                TRAINER t ON m.id = t.memberId
            LEFT JOIN
                CENTER c ON t.centerId = c.id
            LEFT JOIN
            	TRAINERFILE tf ON tf.trainerId = t.id
            WHERE m.id = #{id};
            """)
    TrainerDetailResponse getTrainerDetail(Integer id);

    @Update("""
            UPDATE 
                SCHEDULE
            SET ptTime = #{ptHour}
            WHERE id = #{id} AND memberId = #{memberId}
            """)
    Integer scheduleUpdate(ScheduleUpdateRequest scheduleUpdateRequest);

}

