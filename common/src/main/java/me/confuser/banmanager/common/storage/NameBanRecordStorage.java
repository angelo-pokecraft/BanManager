package me.confuser.banmanager.common.storage;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;
import me.confuser.banmanager.common.BanManagerPlugin;
import me.confuser.banmanager.common.data.NameBanData;
import me.confuser.banmanager.common.data.NameBanRecord;
import me.confuser.banmanager.common.data.PlayerData;
import me.confuser.banmanager.common.util.DateUtils;

import java.sql.SQLException;

public class NameBanRecordStorage extends BaseDaoImpl<NameBanRecord, Integer> {

  public NameBanRecordStorage(BanManagerPlugin plugin) throws SQLException {
    super(plugin.getLocalConn(), (DatabaseTableConfig<NameBanRecord>) plugin.getConfig()
        .getLocalDb().getTable("nameBanRecords"));

    if (!this.isTableExists()) {
      TableUtils.createTable(connectionSource, tableConfig);
      return;
    }
  }

  public NameBanRecordStorage(ConnectionSource connection, DatabaseTableConfig<?> table) throws SQLException {
    super(connection, (DatabaseTableConfig<NameBanRecord>) table);
  }

  public void addRecord(NameBanData ban, PlayerData actor, String reason) throws SQLException {
    create(new NameBanRecord(ban, actor, reason));
  }

  public CloseableIterator<NameBanRecord> findUnbans(long fromTime) throws SQLException {
    if (fromTime == 0) {
      return iterator();
    }

    long checkTime = fromTime + DateUtils.getTimeDiff();

    QueryBuilder<NameBanRecord, Integer> query = queryBuilder();
    Where<NameBanRecord, Integer> where = query.where();

    where.ge("created", checkTime);

    query.setWhere(where);


    return query.iterator();

  }
}
