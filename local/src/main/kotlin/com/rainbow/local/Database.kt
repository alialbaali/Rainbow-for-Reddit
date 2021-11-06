package com.rainbow.local

import com.rainbow.sql.RainbowDatabase
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

private const val DatabasePath =
    "jdbc:sqlite:/home/alialbaali/Projects/Rainbow/local/src/main/kotlin/com/rainbow/local/database.sql"

val RainbowDatabase: RainbowDatabase by lazy {
    val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    RainbowDatabase.Schema.create(driver)
    RainbowDatabase(driver)
}