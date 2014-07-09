package gw.quotes.dao

import gw.jdbi.MapMapper

import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.SqlUpdate
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper

@RegisterMapper(MapMapper)
interface QuotesDao {

    @SqlQuery('select id, content from quotes where id = :id')
    Map get(@Bind('id') Long id)

    @SqlQuery('select id, content from quotes')
    List<Map> list()

    @SqlUpdate('insert into quotes (id, text) values (:id, :name)')
    int save(
        @Bind('id') Long id,
        @Bind('quote') String quote)

}
