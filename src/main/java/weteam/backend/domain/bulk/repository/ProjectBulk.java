package weteam.backend.domain.bulk.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Repository
@RequiredArgsConstructor
public class ProjectBulk {
    private final JdbcTemplate jdbcTemplate;
    private final SecurityUtil securityUtil;

    @Transactional
    public void clear() {
        final String sql = "DELETE FROM projects";
        jdbcTemplate.execute(sql);
    }

    @Transactional
    public void bulkInsert(final Integer batchSize, final LocalDateTime start, final LocalDateTime end) {
        for (int i = 1; i <= batchSize; i++) {
            batchInsert(batchSize, start, end);
        }
    }

    private void batchInsert(final Integer batchSize, final LocalDateTime start, final LocalDateTime end) {
        final String sql = "INSERT INTO projects (host_id ,done, explanation, name, started_at, ended_at, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    final LocalDateTime randomStart = generateRandomLocalDateTime(start, end);
                    final LocalDateTime randomEnd = generateRandomLocalDateTime(randomStart, end);

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, securityUtil.getId());
                        ps.setBoolean(2, false);
                        ps.setString(3, generateRandomExplanation());
                        ps.setString(4, generateRandomName());
                        ps.setObject(5, Timestamp.valueOf(randomStart));
                        ps.setObject(6, Timestamp.valueOf(randomEnd));
                        ps.setTimestamp(7, Timestamp.valueOf(randomStart));
                        ps.setTimestamp(8, Timestamp.valueOf(randomStart));
                    }

                    @Override
                    public int getBatchSize() {
                        return batchSize;
                    }
                }
        );
    }

    private LocalDateTime generateRandomLocalDateTime(LocalDateTime start, LocalDateTime end) {
        long startEpochSecond = start.toEpochSecond(ZoneOffset.UTC);
        long endEpochSecond = end.toEpochSecond(ZoneOffset.UTC);
        long randomEpochSecond = ThreadLocalRandom.current().nextLong(startEpochSecond, endEpochSecond);

        return LocalDateTime.ofEpochSecond(randomEpochSecond, 0, ZoneOffset.UTC);
    }

    private String generateRandomExplanation() {
        return "랜덤 설명 " + new Random().nextInt(10000);
    }

    private String generateRandomName() {
        return "랜덤 이름 " + new Random().nextInt(10000);
    }
}
