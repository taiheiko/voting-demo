package com.github.terigina.voting.dao;

import com.github.terigina.voting.service.Color;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.primaryKey;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.using;
import static org.jooq.impl.SQLDataType.VARCHAR;

public class H2VotingDAO implements IVotingDAO {

    private static final Table<Record> COLOR_VOTES_TABLE = table("color_votes");
    private static final Field<UUID> USER_ID_FIELD = field("user_id", SQLDataType.UUID.nullable(false));
    private static final Field<String> COLOR_FIELD = field("color", VARCHAR.nullable(false));

    private Connection connection;
    private DSLContext context;

    public H2VotingDAO() {
        System.getProperties().setProperty("org.jooq.no-logo", "true");
        try {
            this.connection = DriverManager.getConnection("jdbc:h2:mem:votes");
            context = DSL.using(this.connection, SQLDialect.H2);
            context.createTable(table("if not exists " + COLOR_VOTES_TABLE.getName()))
                    .column(USER_ID_FIELD)
                    .column(COLOR_FIELD)
                    .constraints(
                            primaryKey(USER_ID_FIELD)
                    )
                    .execute();
        } catch (SQLException e) {
            throw new VotingDAOException(e);
        }
    }

    @Override
    public boolean voteFor(UUID userId, Color color) {
        return context.transactionResult(configuration -> {
            DSLContext ctx = using(configuration);
            ctx.deleteFrom(COLOR_VOTES_TABLE)
                    .where(USER_ID_FIELD.eq(userId))
                    .execute();
            int result = ctx.insertInto(COLOR_VOTES_TABLE, USER_ID_FIELD, COLOR_FIELD)
                    .values(userId, color.name())
                    .execute();
            return result > 0;
        });
    }

    @Override
    public int getVotes(Color color) {
        return context.select(count())
                .from(COLOR_VOTES_TABLE)
                .where(COLOR_FIELD.eq(color.name()))
                .fetchOne()
                .value1();
    }

    @Override
    public Optional<Color> getVote(UUID userId) {
        Record1<String> resultSet = context.select(COLOR_FIELD)
                .from(COLOR_VOTES_TABLE)
                .where(USER_ID_FIELD.eq(userId))
                .fetchOne();
        if (resultSet != null) {
            return Color.of(resultSet.value1());
        }
        return Optional.empty();
    }

    @Override
    public Map<Color, Integer> getVotes() {
        return context.select(COLOR_FIELD, count())
                .from(COLOR_VOTES_TABLE)
                .where(COLOR_FIELD.in(Arrays.asList(Color.values())))
                .groupBy(COLOR_FIELD)
                .fetch()
                .stream()
                .collect(Collectors.toMap(r -> Color.of(r.value1()).get(), r -> r.value2()));
    }

    @Override
    public void close() throws Exception {
        System.err.println("Closing DB Connection...");
        connection.close();
    }
}
