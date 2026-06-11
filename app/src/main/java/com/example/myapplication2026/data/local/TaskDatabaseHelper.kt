package com.example.myapplication2026.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_TASKS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT NOT NULL,
                $COLUMN_CATEGORY TEXT NOT NULL,
                $COLUMN_IS_DONE INTEGER NOT NULL,
                $COLUMN_CREATED_AT INTEGER NOT NULL
            )
        """.trimIndent()

        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }

    fun insertTask(task: TaskEntity): Long {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_CATEGORY, task.category)
            put(COLUMN_IS_DONE, if (task.isDone) 1 else 0)
            put(COLUMN_CREATED_AT, task.createdAt)
        }

        return db.insert(TABLE_TASKS, null, values)
    }

    fun getAllTasks(): List<TaskEntity> {
        val tasks = mutableListOf<TaskEntity>()
        val db = readableDatabase

        val cursor = db.query(
            TABLE_TASKS,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_ID DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                val task = TaskEntity(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE)),
                    description = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    category = it.getString(it.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                    isDone = it.getInt(it.getColumnIndexOrThrow(COLUMN_IS_DONE)) == 1,
                    createdAt = it.getLong(it.getColumnIndexOrThrow(COLUMN_CREATED_AT))
                )

                tasks.add(task)
            }
        }

        return tasks
    }

    fun updateTask(task: TaskEntity): Int {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_CATEGORY, task.category)
            put(COLUMN_IS_DONE, if (task.isDone) 1 else 0)
            put(COLUMN_CREATED_AT, task.createdAt)
        }

        return db.update(
            TABLE_TASKS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(task.id.toString())
        )
    }

    fun deleteTask(taskId: Int): Int {
        val db = writableDatabase

        return db.delete(
            TABLE_TASKS,
            "$COLUMN_ID = ?",
            arrayOf(taskId.toString())
        )
    }

    fun getTaskById(taskId: Int): TaskEntity? {
        val db = readableDatabase

        val cursor = db.query(
            TABLE_TASKS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(taskId.toString()),
            null,
            null,
            null
        )

        cursor.use {
            if (it.moveToFirst()) {
                return TaskEntity(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE)),
                    description = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    category = it.getString(it.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                    isDone = it.getInt(it.getColumnIndexOrThrow(COLUMN_IS_DONE)) == 1,
                    createdAt = it.getLong(it.getColumnIndexOrThrow(COLUMN_CREATED_AT))
                )
            }
        }

        return null
    }

    companion object {
        private const val DATABASE_NAME = "tasks_database.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_TASKS = "tasks"

        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_IS_DONE = "is_done"
        private const val COLUMN_CREATED_AT = "created_at"
    }
}