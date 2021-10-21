package com.project.scheduling.dao;

import com.project.scheduling.modal.DynamicTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :21/10/2021 - 1:22 PM
 */
@Repository
public interface DynamicTaskRepository extends JpaRepository<DynamicTask,Long> {
}
