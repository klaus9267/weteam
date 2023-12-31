package weteam.backend.domain.alarm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import weteam.backend.domain.alarm.dto.AlarmDto;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    @Query("""
           SELECT new weteam.backend.domain.alarm.dto.AlarmDto(a)
           FROM alarms a
                LEFT JOIN FETCH users u ON a.user.id = u.id
           WHERE a.user.id = :userId
           """)
    Page<AlarmDto> findAll(final Pageable pageable, final Long userId);
}
