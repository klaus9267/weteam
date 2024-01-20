package weteam.backend.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import weteam.backend.domain.project.entity.ProjectUser;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectUser, Long> {
    Optional<ProjectUser> findByProjectIdAndUserId(Long projectId, Long userId);

    List<ProjectUser> findByProjectId(Long projectId);


    @Query("""
           SELECT count(h.id) > 0
           FROM project_users pu
                LEFT JOIN FETCH pu.project p
                LEFT JOIN FETCH p.host h
           WHERE h.id = :userId
           """)
    boolean checkHost(final Long userId);

    void deleteByUserId(final Long userId);
}
