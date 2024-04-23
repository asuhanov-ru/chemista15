package com.chemista15.repository;

import com.chemista15.domain.OcrTask;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OcrTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OcrTaskRepository extends JpaRepository<OcrTask, Long>, JpaSpecificationExecutor<OcrTask> {}
