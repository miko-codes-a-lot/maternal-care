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
import org.maternalcare.modules.main.user.model.entity.Address
import org.maternalcare.modules.main.user.model.entity.User
import org.maternalcare.modules.main.user.model.entity.UserBirthRecord
import org.maternalcare.modules.main.user.model.entity.UserCheckup
import org.maternalcare.modules.main.user.model.entity.UserCondition
import org.maternalcare.modules.main.user.model.entity.UserImmunization
import org.maternalcare.modules.main.user.model.entity.UserTrimesterRecord
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

            val setOfEntities = setOf(
                User::class,
                UserCheckup::class,
                Address::class,
                UserCondition::class,
                UserImmunization::class,
                UserBirthRecord:: class,
                UserTrimesterRecord::class
            )

            val config = SyncConfiguration
                .Builder(
                    user,
                    setOfEntities
                )
                .initialSubscriptions { realm ->
                    add(
                        realm.query<User>("_id <> $0", null),
                        name = "Users"
                    )
                    add(
                        realm.query<UserCheckup>("_id <> $0", null),
                        name = "UserCheckups"
                    )
                    add(
                        realm.query<Address>("_id <> $0", null),
                        name = "Addresses"
                    )
                    add(
                        realm.query<UserCondition>("_id <> $0", null),
                        name = "UserConditions"
                    )
                    add(
                        realm.query<UserImmunization>("_id <> $0", null),
                        name = "UserImmunizations"
                    )
                    add(
                        realm.query<UserBirthRecord>("_id <> $0", null),
                        name = "UserBirthRecords"
                    )
                    add(
                        realm.query<UserTrimesterRecord>("_id <> $0", null),
                        name = "UserTrimesterRecords"
                    )
                }
                .build()

            Realm.open(config)
        }
    }
}