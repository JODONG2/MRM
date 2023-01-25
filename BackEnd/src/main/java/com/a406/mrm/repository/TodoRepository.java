package com.a406.mrm.repository;

import com.a406.mrm.model.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> findByUserId(@Param("userId") String userId);
    List<Todo> findByRoomId(@Param("roomId") int roomId);

//    @Modifying(clearAutomatically = true)
//    @Query("UPDATE todo_time AS tt " +
//            "RIGHT JOIN todo as t ON (t.id = tt.todo_id)" +
//            "SET t.state =2, t.endtime=now(), tt.endtime=now(), tt.totaltime=now()-tt.starttime()" +
//            "WHERE tt.todo_id = :todo_id")
//    int updateTodoStateDone(@Param("todo_id") int id);
}