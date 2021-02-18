package com.github.taya_y.voting.dao;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import static org.jooq.impl.DSL.*;
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
    public boolean voteFor(UUID userId, String color) {
        return context.transactionResult(configuration -> {
            DSLContext ctx = using(configuration);
            ctx.deleteFrom(COLOR_VOTES_TABLE)
                    .where(USER_ID_FIELD.eq(userId))
                    .execute();
            int result = ctx.insertInto(COLOR_VOTES_TABLE, USER_ID_FIELD, COLOR_FIELD)
                    .values(userId, color)
                    .execute();
            return result > 0;
        });
    }

    @Override
    public int getVotes(String color) {
        return context.select(count())
                .from(COLOR_VOTES_TABLE)
                .where(COLOR_FIELD.eq(color))
                .fetchOne()
                .value1();
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
