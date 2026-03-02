package com.rahul.clearwalls.core.di;

import com.rahul.clearwalls.data.local.ClearWallsDatabase;
import com.rahul.clearwalls.data.local.dao.AiGenerationDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class DatabaseModule_ProvideAiGenerationDaoFactory implements Factory<AiGenerationDao> {
  private final Provider<ClearWallsDatabase> databaseProvider;

  public DatabaseModule_ProvideAiGenerationDaoFactory(
      Provider<ClearWallsDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public AiGenerationDao get() {
    return provideAiGenerationDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideAiGenerationDaoFactory create(
      javax.inject.Provider<ClearWallsDatabase> databaseProvider) {
    return new DatabaseModule_ProvideAiGenerationDaoFactory(Providers.asDaggerProvider(databaseProvider));
  }

  public static DatabaseModule_ProvideAiGenerationDaoFactory create(
      Provider<ClearWallsDatabase> databaseProvider) {
    return new DatabaseModule_ProvideAiGenerationDaoFactory(databaseProvider);
  }

  public static AiGenerationDao provideAiGenerationDao(ClearWallsDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAiGenerationDao(database));
  }
}
