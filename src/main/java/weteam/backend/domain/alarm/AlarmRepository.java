package weteam.backend.domain.alarm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.alarm.entity.Alarm;

import java.util.List;
import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    @EntityGraph(attributePaths = {"user", "targetUser"})
    Page<Alarm> findAllByUserId(final Pageable pageable, final Long userId);

    Optional<Alarm> findByIdAndUserId(final Long alarmId, final Long userId);

    List<Alarm> findAllByUserId(final Long userId);
}
