package com.chemista15.repository;

import com.chemista15.domain.Media;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Media entity.
 */
@Repository
public interface MediaRepository extends JpaRepository<Media, Long>, JpaSpecificationExecutor<Media> {
    default Optional<Media> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Media> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Media> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select media from Media media left join fetch media.collection", countQuery = "select count(media) from Media media")
    Page<Media> findAllWithToOneRelationships(Pageable pageable);

    @Query("select media from Media media left join fetch media.collection")
    List<Media> findAllWithToOneRelationships();

    @Query("select media from Media media left join fetch media.collection where media.id =:id")
    Optional<Media> findOneWithToOneRelationships(@Param("id") Long id);
}
