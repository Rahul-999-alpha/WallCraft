package com.rahul.clearwalls.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.rahul.clearwalls.data.local.entity.AiGenerationEntity;
import com.rahul.clearwalls.data.local.entity.AiQuotaEntity;
import java.lang.Class;
import java.lang.Exception;
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
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AiGenerationDao_Impl implements AiGenerationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AiGenerationEntity> __insertionAdapterOfAiGenerationEntity;

  private final EntityInsertionAdapter<AiQuotaEntity> __insertionAdapterOfAiQuotaEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteGeneration;

  public AiGenerationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAiGenerationEntity = new EntityInsertionAdapter<AiGenerationEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `ai_generations` (`id`,`prompt`,`enhancedPrompt`,`imageUrl`,`localPath`,`style`,`isAmoled`,`createdAt`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AiGenerationEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getPrompt());
        statement.bindString(3, entity.getEnhancedPrompt());
        statement.bindString(4, entity.getImageUrl());
        if (entity.getLocalPath() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getLocalPath());
        }
        statement.bindString(6, entity.getStyle());
        final int _tmp = entity.isAmoled() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfAiQuotaEntity = new EntityInsertionAdapter<AiQuotaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `ai_quota` (`date`,`freeUsed`,`bonusEarned`,`bonusUsed`,`adsWatched`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AiQuotaEntity entity) {
        statement.bindString(1, entity.getDate());
        statement.bindLong(2, entity.getFreeUsed());
        statement.bindLong(3, entity.getBonusEarned());
        statement.bindLong(4, entity.getBonusUsed());
        statement.bindLong(5, entity.getAdsWatched());
      }
    };
    this.__preparedStmtOfDeleteGeneration = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM ai_generations WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertGeneration(final AiGenerationEntity generation,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAiGenerationEntity.insert(generation);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertQuota(final AiQuotaEntity quota,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAiQuotaEntity.insert(quota);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteGeneration(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteGeneration.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
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
          __preparedStmtOfDeleteGeneration.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AiGenerationEntity>> getAllGenerations() {
    final String _sql = "SELECT * FROM ai_generations ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"ai_generations"}, new Callable<List<AiGenerationEntity>>() {
      @Override
      @NonNull
      public List<AiGenerationEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPrompt = CursorUtil.getColumnIndexOrThrow(_cursor, "prompt");
          final int _cursorIndexOfEnhancedPrompt = CursorUtil.getColumnIndexOrThrow(_cursor, "enhancedPrompt");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "localPath");
          final int _cursorIndexOfStyle = CursorUtil.getColumnIndexOrThrow(_cursor, "style");
          final int _cursorIndexOfIsAmoled = CursorUtil.getColumnIndexOrThrow(_cursor, "isAmoled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<AiGenerationEntity> _result = new ArrayList<AiGenerationEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AiGenerationEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpPrompt;
            _tmpPrompt = _cursor.getString(_cursorIndexOfPrompt);
            final String _tmpEnhancedPrompt;
            _tmpEnhancedPrompt = _cursor.getString(_cursorIndexOfEnhancedPrompt);
            final String _tmpImageUrl;
            _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final String _tmpStyle;
            _tmpStyle = _cursor.getString(_cursorIndexOfStyle);
            final boolean _tmpIsAmoled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsAmoled);
            _tmpIsAmoled = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new AiGenerationEntity(_tmpId,_tmpPrompt,_tmpEnhancedPrompt,_tmpImageUrl,_tmpLocalPath,_tmpStyle,_tmpIsAmoled,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getQuota(final String date, final Continuation<? super AiQuotaEntity> $completion) {
    final String _sql = "SELECT * FROM ai_quota WHERE date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AiQuotaEntity>() {
      @Override
      @Nullable
      public AiQuotaEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfFreeUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "freeUsed");
          final int _cursorIndexOfBonusEarned = CursorUtil.getColumnIndexOrThrow(_cursor, "bonusEarned");
          final int _cursorIndexOfBonusUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "bonusUsed");
          final int _cursorIndexOfAdsWatched = CursorUtil.getColumnIndexOrThrow(_cursor, "adsWatched");
          final AiQuotaEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final int _tmpFreeUsed;
            _tmpFreeUsed = _cursor.getInt(_cursorIndexOfFreeUsed);
            final int _tmpBonusEarned;
            _tmpBonusEarned = _cursor.getInt(_cursorIndexOfBonusEarned);
            final int _tmpBonusUsed;
            _tmpBonusUsed = _cursor.getInt(_cursorIndexOfBonusUsed);
            final int _tmpAdsWatched;
            _tmpAdsWatched = _cursor.getInt(_cursorIndexOfAdsWatched);
            _result = new AiQuotaEntity(_tmpDate,_tmpFreeUsed,_tmpBonusEarned,_tmpBonusUsed,_tmpAdsWatched);
          } else {
            _result = null;
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
