//package weteam.backend.domain.bulk.repository;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.jdbc.core.BatchPreparedStatementSetter;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//import weteam.backend.application.auth.SecurityUtil;
//
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.time.ZoneOffset;
//import java.util.Random;
//import java.util.concurrent.ThreadLocalRandom;
//
//@Repository
//@RequiredArgsConstructor
//public class MeetingBulk {
//    private final JdbcTemplate jdbcTemplate;
//    private final SecurityUtil securityUtil;
//
//    @Transactional
//    public void clear() {
//        final String sql = "DELETE FROM meetings";
//        jdbcTemplate.execute(sql);
//    }
//
//    @Transactional
//    public void bulkInsertWithMeetings(final Integer batchSize, final LocalDateTime start, final LocalDateTime end) {
//        for (int i = 1; i <= batchSize; i++) {
//            batchInsert(batchSize, start, end);
//        }
//    }
//
//    private void batchInsert(final Integer batchSize, final LocalDateTime start, final LocalDateTime end) {
//        final String sql = "INSERT INTO meetings (event, event_date, memo, status, user_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
//        LocalDateTime randomDateTime = generateRandomLocalDateTime(start, end);
//
//        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps, int i) throws SQLException {
//                ps.setString(1, generateRandomEvent());
//                ps.setObject(2, randomDateTime);
//                ps.setString(3, generateRandomMemo());
//                ps.setString(4, generateRandomStatus().getKey());
//                ps.setLong(5, securityUtil.getId());
//                ps.setTimestamp(6, Timestamp.valueOf(randomDateTime));
//                ps.setTimestamp(7, Timestamp.valueOf(randomDateTime));
//            }
//
//            @Override
//            public int getBatchSize() {
//                return batchSize;
//            }
//        });
//    }
//
//    private LocalDateTime generateRandomLocalDateTime(LocalDateTime start, LocalDateTime end) {
//        long startEpochSecond = start.toEpochSecond(ZoneOffset.UTC);
//        long endEpochSecond = end.toEpochSecond(ZoneOffset.UTC);
//        long randomEpochSecond = ThreadLocalRandom.current().nextLong(startEpochSecond, endEpochSecond);
//
//        return LocalDateTime.ofEpochSecond(randomEpochSecond, 0, ZoneOffset.UTC);
//    }
//
//    private String generateRandomMemo() {
//        return "랜덤 메모 " + new Random().nextInt(10000);
//    }
//
//    private String generateRandomEvent() {
//        return "랜덤 행사 " + new Random().nextInt(10000);
//    }
//}
