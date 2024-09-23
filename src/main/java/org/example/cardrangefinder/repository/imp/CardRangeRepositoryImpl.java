package org.example.cardrangefinder.repository.imp;

import org.example.cardrangefinder.dto.CardInfoDto;
import org.example.cardrangefinder.entity.CardRange;
import org.example.cardrangefinder.exceptions.DatabaseOperationException;
import org.example.cardrangefinder.repository.CardRangeRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CardRangeRepositoryImpl implements CardRangeRepository {

    private JdbcTemplate jdbcTemplate;

    private final static String mainTable = "card_range_main";
    private final static String cardInfoShadow = "card_range_shadow";

    public CardRangeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CardInfoDto> findCardInfoByMinAndMaxPossibleCardNumber(BigDecimal minCardNumber,
                                                                       BigDecimal maxCardNumber, boolean isMain) {
        String tableName = isMain ? mainTable : cardInfoShadow;
        String sql = String.format(
                "SELECT bin, alpha_code, bank_name FROM %s WHERE min_range <= ? AND max_range >= ?", tableName
        );

        try {
            return jdbcTemplate.query(sql, new Object[]{minCardNumber, minCardNumber}, (rs, rowNum) -> {
                return new CardInfoDto(
                        rs.getInt("bin"),
                        rs.getString("alpha_code"),
                        rs.getString("bank_name")
                );
            });
        } catch (EmptyResultDataAccessException ex) {
            return List.of();
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Database error occurred while fetching card info", ex);
        }
    }

    @Override
    @Transactional
    public boolean saveCardInfo(List<CardRange> infos, boolean isMain) {
        String tableName = isMain ? mainTable : cardInfoShadow;
        String sql = String.format(
                "INSERT INTO %s (bin, min_range, max_range, alpha_code, bank_name) VALUES (?, ?, ?, ?, ?)", tableName
        );

        try {
            int[] results = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    CardRange cardRange = infos.get(i);
                    ps.setInt(1, cardRange.getBin());
                    ps.setBigDecimal(2, cardRange.getMinRange());
                    ps.setBigDecimal(3, cardRange.getMaxRange());
                    ps.setString(4, cardRange.getAlphaCode());
                    ps.setString(5, cardRange.getBankName());
                }

                @Override
                public int getBatchSize() {
                    return infos.size();
                }
            });
            return results.length == infos.size();
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Database error occurred while saving card info", ex);
        }
    }

    @Transactional
    @Override
    public void deleteAllRecords(boolean isMain) {
        String tableName = isMain ? mainTable : cardInfoShadow;
        String cardInfoTableQuery = String.format("DELETE FROM %s", tableName);

        try {
            jdbcTemplate.update(cardInfoTableQuery);
            String sequenceName = isMain ? "card_range_main_id_seq" : "card_range_shadow_id_seq";
            String cardInfoSequenceQuery = String.format("ALTER SEQUENCE %s RESTART WITH 1", sequenceName);
            jdbcTemplate.update(cardInfoSequenceQuery);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Database error occurred while deleting records", ex);
        }
    }
}
