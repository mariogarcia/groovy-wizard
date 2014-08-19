package gw.jdbi

import java.sql.SQLException
import java.sql.ResultSet

import org.skife.jdbi.v2.StatementContext
import org.skife.jdbi.v2.tweak.ResultSetMapper

import com.google.common.collect.ImmutableMap

class MapMapper implements ResultSetMapper<Map> {

    Closure<ImmutableMap> collector = { ResultSet rs ->
        return { ImmutableMap.Builder builder, int index ->
            builder.put(
                rs.metaData.getColumnName(index),
                rs.getObject(index)
            )
            builder
        }
    }

    Closure<Range<Integer>> columns = { ResultSet rs ->
        return (1..(rs.metaData.columnCount))
    }

    public ImmutableMap map(int index, ResultSet rs, StatementContext ctx) throws SQLException {
        return columns(rs)
            .inject(
                ImmutableMap.builder(),
                collector(rs)
            ).build()
    }

}
