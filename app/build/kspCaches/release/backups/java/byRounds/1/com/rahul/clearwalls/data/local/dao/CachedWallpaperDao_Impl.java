package com.rahul.clearwalls.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.rahul.clearwalls.data.local.entity.CachedWallpaperEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CachedWallpaperDao_Impl implements CachedWallpaperDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CachedWallpaperEntity> __insertionAdapterOfCachedWallpaperEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOlderThan;

  public CachedWallpaperDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCachedWallpaperEntity = new EntityInsertionAdapter<CachedWallpaperEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `cached_wallpapers` (`id`,`source`,`title`,`thumbnailUrl`,`previewUrl`,`fullUrl`,`width`,`height`,`dominantColor`,`tags`,`category`,`isAmoled`,`lowUrl`,`hdUrl`,`twoKUrl`,`fourKUrl`,`photographer`,`photographerUrl`,`sourceUrl`,`cachedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CachedWallpaperEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getSource());
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getThumbnailUrl());
        statement.bindString(5, entity.getPreviewUrl());
        statement.bindString(6, entity.getFullUrl());
        statement.bindLong(7, entity.getWidth());
        statement.bindLong(8, entity.getHeight());
        if (entity.getDominantColor() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getDominantColor());
        }
        statement.bindString(10, entity.getTags());
        if (entity.getCategory() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getCategory());
        }
        final int _tmp = entity.isAmoled() ? 1 : 0;
        statement.bindLong(12, _tmp);
        if (entity.getLowUrl() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getLowUrl());
        }
        if (entity.getHdUrl() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getHdUrl());
        }
        if (entity.getTwoKUrl() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getTwoKUrl());
        }
        if (entity.getFourKUrl() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getFourKUrl());
        }
        if (entity.getPhotographer() == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, entity.getPhotographer());
        }
        if (entity.getPhotographerUrl() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getPhotographerUrl());
        }
        if (entity.getSourceUrl() == null) {
          statement.bindNull(19);
        } else {
          statement.bindString(19, entity.getSourceUrl());
        }
        statement.bindLong(20, entity.getCachedAt());
      }
    };
    this.__preparedStmtOfDeleteOlderThan = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM cached_wallpapers WHERE cachedAt < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<CachedWallpaperEntity> wallpapers,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCachedWallpaperEntity.insert(wallpapers);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOlderThan(final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOlderThan.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestamp);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteOlderThan.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllCached(final Continuation<? super List<CachedWallpaperEntity>> $completion) {
    final String _sql = "SELECT * FROM cached_wallpapers ORDER BY cachedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CachedWallpaperEntity>>() {
      @Override
      @NonNull
      public List<CachedWallpaperEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfThumbnailUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailUrl");
          final int _cursorIndexOfPreviewUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "previewUrl");
          final int _cursorIndexOfFullUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "fullUrl");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfDominantColor = CursorUtil.getColumnIndexOrThrow(_cursor, "dominantColor");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfIsAmoled = CursorUtil.getColumnIndexOrThrow(_cursor, "isAmoled");
          final int _cursorIndexOfLowUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "lowUrl");
          final int _cursorIndexOfHdUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "hdUrl");
          final int _cursorIndexOfTwoKUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "twoKUrl");
          final int _cursorIndexOfFourKUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "fourKUrl");
          final int _cursorIndexOfPhotographer = CursorUtil.getColumnIndexOrThrow(_cursor, "photographer");
          final int _cursorIndexOfPhotographerUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "photographerUrl");
          final int _cursorIndexOfSourceUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceUrl");
          final int _cursorIndexOfCachedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "cachedAt");
          final List<CachedWallpaperEntity> _result = new ArrayList<CachedWallpaperEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CachedWallpaperEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSource;
            _tmpSource = _cursor.getString(_cursorIndexOfSource);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpThumbnailUrl;
            _tmpThumbnailUrl = _cursor.getString(_cursorIndexOfThumbnailUrl);
            final String _tmpPreviewUrl;
            _tmpPreviewUrl = _cursor.getString(_cursorIndexOfPreviewUrl);
            final String _tmpFullUrl;
            _tmpFullUrl = _cursor.getString(_cursorIndexOfFullUrl);
            final int _tmpWidth;
            _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            final int _tmpHeight;
            _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            final String _tmpDominantColor;
            if (_cursor.isNull(_cursorIndexOfDominantColor)) {
              _tmpDominantColor = null;
            } else {
              _tmpDominantColor = _cursor.getString(_cursorIndexOfDominantColor);
            }
            final String _tmpTags;
            _tmpTags = _cursor.getString(_cursorIndexOfTags);
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final boolean _tmpIsAmoled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsAmoled);
            _tmpIsAmoled = _tmp != 0;
            final String _tmpLowUrl;
            if (_cursor.isNull(_cursorIndexOfLowUrl)) {
              _tmpLowUrl = null;
            } else {
              _tmpLowUrl = _cursor.getString(_cursorIndexOfLowUrl);
            }
            final String _tmpHdUrl;
            if (_cursor.isNull(_cursorIndexOfHdUrl)) {
              _tmpHdUrl = null;
            } else {
              _tmpHdUrl = _cursor.getString(_cursorIndexOfHdUrl);
            }
            final String _tmpTwoKUrl;
            if (_cursor.isNull(_cursorIndexOfTwoKUrl)) {
              _tmpTwoKUrl = null;
            } else {
              _tmpTwoKUrl = _cursor.getString(_cursorIndexOfTwoKUrl);
            }
            final String _tmpFourKUrl;
            if (_cursor.isNull(_cursorIndexOfFourKUrl)) {
              _tmpFourKUrl = null;
            } else {
              _tmpFourKUrl = _cursor.getString(_cursorIndexOfFourKUrl);
            }
            final String _tmpPhotographer;
            if (_cursor.isNull(_cursorIndexOfPhotographer)) {
              _tmpPhotographer = null;
            } else {
              _tmpPhotographer = _cursor.getString(_cursorIndexOfPhotographer);
            }
            final String _tmpPhotographerUrl;
            if (_cursor.isNull(_cursorIndexOfPhotographerUrl)) {
              _tmpPhotographerUrl = null;
            } else {
              _tmpPhotographerUrl = _cursor.getString(_cursorIndexOfPhotographerUrl);
            }
            final String _tmpSourceUrl;
            if (_cursor.isNull(_cursorIndexOfSourceUrl)) {
              _tmpSourceUrl = null;
            } else {
              _tmpSourceUrl = _cursor.getString(_cursorIndexOfSourceUrl);
            }
            final long _tmpCachedAt;
            _tmpCachedAt = _cursor.getLong(_cursorIndexOfCachedAt);
            _item = new CachedWallpaperEntity(_tmpId,_tmpSource,_tmpTitle,_tmpThumbnailUrl,_tmpPreviewUrl,_tmpFullUrl,_tmpWidth,_tmpHeight,_tmpDominantColor,_tmpTags,_tmpCategory,_tmpIsAmoled,_tmpLowUrl,_tmpHdUrl,_tmpTwoKUrl,_tmpFourKUrl,_tmpPhotographer,_tmpPhotographerUrl,_tmpSourceUrl,_tmpCachedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM cached_wallpapers";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
