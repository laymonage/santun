package id.ac.ui.cs.mobileprogramming.sage.santun.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import id.ac.ui.cs.mobileprogramming.sage.santun.model.migrations.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Message::class], version = 1)
abstract class MessageRoomDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao

    private class MessageDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let {
                    database -> scope.launch {
                populateDatabase(database.messageDao())
            }
            }
        }
        suspend fun populateDatabase(messageDao: MessageDao) {
            messageDao.deleteAll()
            var message = Message(null, "aku", "kamu", "halo")
            messageDao.insert(message)
            message = Message(null, "kamu", "aku", "world")
            messageDao.insert(message)
            message = Message(
                null, "seseorang yang namanya panjang sekali banget banget pokoknya lah " +
                "gitu pokoknya gan",
                "ini juga namanya panjang banget pokoknya mungkin dia mau ngerusak app ini " +
                "gitu pokoknya gan",
                "pesan\nini terdiri\ndari lebih dari\ntiga baris kata-kata\nkita lihat saja"
            )
            messageDao.insert(message)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: MessageRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): MessageRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MessageRoomDatabase::class.java,
                    "message_database"
                )
                    .addCallback(MessageDatabaseCallback(scope))
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
