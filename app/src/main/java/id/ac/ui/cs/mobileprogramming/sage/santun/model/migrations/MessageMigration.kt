package id.ac.ui.cs.mobileprogramming.sage.santun.model.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MESSAGE_MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE message_table ADD COLUMN image_uri TEXT")
    }
}

val MESSAGE_MIGRATION_2_3: Migration = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE message_table ADD COLUMN timestamp INTEGER NOT NULL")
    }
}
