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

val MESSAGE_MIGRATION_3_4: Migration = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE message_table ADD COLUMN uuid TEXT NOT NULL")
        database.execSQL("CREATE UNIQUE INDEX idx_message_uuid ON message_table(uuid)")
    }
}
