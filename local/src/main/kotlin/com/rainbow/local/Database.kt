package com.rainbow.local

import com.rainbow.sql.LocalPost
import com.rainbow.sql.LocalSubreddit
import com.rainbow.sql.LocalUser
import com.rainbow.sql.RainbowDatabase
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.datetime.LocalDateTime

val RainbowDatabase: RainbowDatabase by lazy {
    val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    RainbowDatabase.Schema.create(driver)
    RainbowDatabase(
        driver,
        LocalPostAdapter = LocalPost.Adapter(LocalDateTimeAdapter),
        LocalSubredditAdapter = LocalSubreddit.Adapter(LocalDateTimeAdapter),
        LocalUserAdapter = LocalUser.Adapter(LocalDateTimeAdapter)
    )
}

private object LocalDateTimeAdapter : ColumnAdapter<LocalDateTime, String> {
    override fun decode(databaseValue: String): LocalDateTime = LocalDateTime.parse(databaseValue)
    override fun encode(value: LocalDateTime): String = value.toString()
}