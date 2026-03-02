package com.rahul.clearwalls.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.rahul.clearwalls.data.local.dao.AiGenerationDao;
import com.rahul.clearwalls.data.local.dao.AiGenerationDao_Impl;
import com.rahul.clearwalls.data.local.dao.CachedWallpaperDao;
import com.rahul.clearwalls.data.local.dao.CachedWallpaperDao_Impl;
import com.rahul.clearwalls.data.local.dao.FavoriteDao;
import com.rahul.clearwalls.data.local.dao.FavoriteDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ClearWallsDatabase_Impl extends ClearWallsDatabase {
  private volatile FavoriteDao _favoriteDao;

  private volatile AiGenerationDao _aiGenerationDao;

  private volatile CachedWallpaperDao _cachedWallpaperDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `favorites` (`wallpaperId` TEXT NOT NULL, `source` TEXT NOT NULL, `title` TEXT NOT NULL, `thumbnailUrl` TEXT NOT NULL, `previewUrl` TEXT NOT NULL, `fullUrl` TEXT NOT NULL, `width` INTEGER NOT NULL, `height` INTEGER NOT NULL, `dominantColor` TEXT, `tags` TEXT NOT NULL, `category` TEXT, `isAmoled` INTEGER NOT NULL, `addedAt` INTEGER NOT NULL, PRIMARY KEY(`wallpaperId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `ai_generations` (`id` TEXT NOT NULL, `prompt` TEXT NOT NULL, `enhancedPrompt` TEXT NOT NULL, `imageUrl` TEXT NOT NULL, `localPath` TEXT, `style` TEXT NOT NULL, `isAmoled` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `ai_quota` (`date` TEXT NOT NULL, `freeUsed` INTEGER NOT NULL, `bonusEarned` INTEGER NOT NULL, `bonusUsed` INTEGER NOT NULL, `adsWatched` INTEGER NOT NULL, PRIMARY KEY(`date`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `cached_wallpapers` (`id` TEXT NOT NULL, `source` TEXT NOT NULL, `title` TEXT NOT NULL, `thumbnailUrl` TEXT NOT NULL, `previewUrl` TEXT NOT NULL, `fullUrl` TEXT NOT NULL, `width` INTEGER NOT NULL, `height` INTEGER NOT NULL, `dominantColor` TEXT, `tags` TEXT NOT NULL, `category` TEXT, `isAmoled` INTEGER NOT NULL, `lowUrl` TEXT, `hdUrl` TEXT, `twoKUrl` TEXT, `fourKUrl` TEXT, `photographer` TEXT, `photographerUrl` TEXT, `sourceUrl` TEXT, `cachedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '4986f7ede219660930dcd886e255bca8')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `favorites`");
        db.execSQL("DROP TABLE IF EXISTS `ai_generations`");
        db.execSQL("DROP TABLE IF EXISTS `ai_quota`");
        db.execSQL("DROP TABLE IF EXISTS `cached_wallpapers`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsFavorites = new HashMap<String, TableInfo.Column>(13);
        _columnsFavorites.put("wallpaperId", new TableInfo.Column("wallpaperId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("source", new TableInfo.Column("source", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("thumbnailUrl", new TableInfo.Column("thumbnailUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("previewUrl", new TableInfo.Column("previewUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("fullUrl", new TableInfo.Column("fullUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("width", new TableInfo.Column("width", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("height", new TableInfo.Column("height", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("dominantColor", new TableInfo.Column("dominantColor", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("tags", new TableInfo.Column("tags", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("category", new TableInfo.Column("category", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("isAmoled", new TableInfo.Column("isAmoled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("addedAt", new TableInfo.Column("addedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFavorites = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFavorites = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFavorites = new TableInfo("favorites", _columnsFavorites, _foreignKeysFavorites, _indicesFavorites);
        final TableInfo _existingFavorites = TableInfo.read(db, "favorites");
        if (!_infoFavorites.equals(_existingFavorites)) {
          return new RoomOpenHelper.ValidationResult(false, "favorites(com.rahul.clearwalls.data.local.entity.FavoriteEntity).\n"
                  + " Expected:\n" + _infoFavorites + "\n"
                  + " Found:\n" + _existingFavorites);
        }
        final HashMap<String, TableInfo.Column> _columnsAiGenerations = new HashMap<String, TableInfo.Column>(8);
        _columnsAiGenerations.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiGenerations.put("prompt", new TableInfo.Column("prompt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiGenerations.put("enhancedPrompt", new TableInfo.Column("enhancedPrompt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiGenerations.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiGenerations.put("localPath", new TableInfo.Column("localPath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiGenerations.put("style", new TableInfo.Column("style", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiGenerations.put("isAmoled", new TableInfo.Column("isAmoled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiGenerations.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAiGenerations = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAiGenerations = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAiGenerations = new TableInfo("ai_generations", _columnsAiGenerations, _foreignKeysAiGenerations, _indicesAiGenerations);
        final TableInfo _existingAiGenerations = TableInfo.read(db, "ai_generations");
        if (!_infoAiGenerations.equals(_existingAiGenerations)) {
          return new RoomOpenHelper.ValidationResult(false, "ai_generations(com.rahul.clearwalls.data.local.entity.AiGenerationEntity).\n"
                  + " Expected:\n" + _infoAiGenerations + "\n"
                  + " Found:\n" + _existingAiGenerations);
        }
        final HashMap<String, TableInfo.Column> _columnsAiQuota = new HashMap<String, TableInfo.Column>(5);
        _columnsAiQuota.put("date", new TableInfo.Column("date", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiQuota.put("freeUsed", new TableInfo.Column("freeUsed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiQuota.put("bonusEarned", new TableInfo.Column("bonusEarned", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiQuota.put("bonusUsed", new TableInfo.Column("bonusUsed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiQuota.put("adsWatched", new TableInfo.Column("adsWatched", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAiQuota = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAiQuota = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAiQuota = new TableInfo("ai_quota", _columnsAiQuota, _foreignKeysAiQuota, _indicesAiQuota);
        final TableInfo _existingAiQuota = TableInfo.read(db, "ai_quota");
        if (!_infoAiQuota.equals(_existingAiQuota)) {
          return new RoomOpenHelper.ValidationResult(false, "ai_quota(com.rahul.clearwalls.data.local.entity.AiQuotaEntity).\n"
                  + " Expected:\n" + _infoAiQuota + "\n"
                  + " Found:\n" + _existingAiQuota);
        }
        final HashMap<String, TableInfo.Column> _columnsCachedWallpapers = new HashMap<String, TableInfo.Column>(20);
        _columnsCachedWallpapers.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedWallpapers.put("source", new TableInfo.Column("source", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedWallpapers.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedWallpapers.put("thumbnailUrl", new TableInfo.Column("thumbnailUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedWallpapers.put("previewUrl", new TableInfo.Column("previewUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedWallpapers.put("fullUrl", new TableInfo.Column("fullUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedWallpapers.put("width", new TableInfo.Column("width", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedWallpapers.put("height", new TableInfo.Column("height", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedWallpapers.put("dominantColor", new TableInfo.Column("dominantColor", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedWallpapers.put("tags", new TableInfo.Column("tags", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedWallpapers.put("category", new TableInfo.Column("category", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedWallpapers.put("isAmoled", new TableInfo.Column("isAmoled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedWallpapers.put("lowUrl", new TableInfo.Column("lowUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedWallpapers.put("hdUrl", new TableInfo.Column("hdUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedWallpapers.put("twoKUrl", new TableInfo.Column("twoKUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedWallpapers.put("fourKUrl", new TableInfo.Column("fourKUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedWallpapers.put("photographer", new TableInfo.Column("photographer", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedWallpapers.put("photographerUrl", new TableInfo.Column("photographerUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedWallpapers.put("sourceUrl", new TableInfo.Column("sourceUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedWallpapers.put("cachedAt", new TableInfo.Column("cachedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCachedWallpapers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCachedWallpapers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCachedWallpapers = new TableInfo("cached_wallpapers", _columnsCachedWallpapers, _foreignKeysCachedWallpapers, _indicesCachedWallpapers);
        final TableInfo _existingCachedWallpapers = TableInfo.read(db, "cached_wallpapers");
        if (!_infoCachedWallpapers.equals(_existingCachedWallpapers)) {
          return new RoomOpenHelper.ValidationResult(false, "cached_wallpapers(com.rahul.clearwalls.data.local.entity.CachedWallpaperEntity).\n"
                  + " Expected:\n" + _infoCachedWallpapers + "\n"
                  + " Found:\n" + _existingCachedWallpapers);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "4986f7ede219660930dcd886e255bca8", "4981029b0996ca6a0a9133253a5c8ef5");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "favorites","ai_generations","ai_quota","cached_wallpapers");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `favorites`");
      _db.execSQL("DELETE FROM `ai_generations`");
      _db.execSQL("DELETE FROM `ai_quota`");
      _db.execSQL("DELETE FROM `cached_wallpapers`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(FavoriteDao.class, FavoriteDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AiGenerationDao.class, AiGenerationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CachedWallpaperDao.class, CachedWallpaperDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public FavoriteDao favoriteDao() {
    if (_favoriteDao != null) {
      return _favoriteDao;
    } else {
      synchronized(this) {
        if(_favoriteDao == null) {
          _favoriteDao = new FavoriteDao_Impl(this);
        }
        return _favoriteDao;
      }
    }
  }

  @Override
  public AiGenerationDao aiGenerationDao() {
    if (_aiGenerationDao != null) {
      return _aiGenerationDao;
    } else {
      synchronized(this) {
        if(_aiGenerationDao == null) {
          _aiGenerationDao = new AiGenerationDao_Impl(this);
        }
        return _aiGenerationDao;
      }
    }
  }

  @Override
  public CachedWallpaperDao cachedWallpaperDao() {
    if (_cachedWallpaperDao != null) {
      return _cachedWallpaperDao;
    } else {
      synchronized(this) {
        if(_cachedWallpaperDao == null) {
          _cachedWallpaperDao = new CachedWallpaperDao_Impl(this);
        }
        return _cachedWallpaperDao;
      }
    }
  }
}
