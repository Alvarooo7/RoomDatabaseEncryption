package com.example.roomdbencryption.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

import java.io.IOException;

import static com.example.roomdbencryption.BuildConfig.ROOM_ENCRYPTION_PASSPHRASE;

@Database(entities = {UserEntity.class},version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract UserDao userDao();

    public static final String DATABASE_NAME = "myAppDb.db";
    private static AppDataBase INSTANCE;

    public static AppDataBase getDatabase(Context context) {
        final byte[] passphrase = SQLiteDatabase.getBytes(ROOM_ENCRYPTION_PASSPHRASE.toCharArray());
        final SupportFactory factory = new SupportFactory(passphrase);
        SQLCipherUtils.State state = SQLCipherUtils.getDatabaseState(context,DATABASE_NAME);

        if (state.equals(SQLCipherUtils.State.UNENCRYPTED)){
            try {
                SQLCipherUtils.encrypt(context,DATABASE_NAME,passphrase);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class,
                    DATABASE_NAME).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .openHelperFactory(factory)
                    .build();
        }

        return INSTANCE;
    }
}
