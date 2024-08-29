package org.maternalcare.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.runBlocking
import org.maternalcare.BuildConfig
import org.maternalcare.modules.main.user.model.entity.UserEntity
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRealmDatabase(): Realm {
        return runBlocking {
            val app = App.create(BuildConfig.REALM_APP_ID)
            val credentials = Credentials.apiKey(BuildConfig.REALM_API_KEY)
            val user = app.login(credentials)

            val config = SyncConfiguration
                .Builder(
                    user,
                    setOf(UserEntity::class)
                )
                .initialSubscriptions { realm ->
                    add(
                        realm.query<UserEntity>("_id <> $0", null),
                        name = "Users"
                    )
                }
                .build()

            Realm.open(config)
        }
    }
}