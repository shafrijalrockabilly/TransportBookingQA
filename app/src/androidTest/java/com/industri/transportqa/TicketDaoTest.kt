package com.industri.transportqa

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@Entity(tableName = "tickets")
data class TicketEntity(
    @PrimaryKey val ticketCode: String,
    val passengerName: String,
    val destination: String
)

@Dao
interface TicketDao {
    @Insert
    fun insertTicket(ticket: TicketEntity)

    @Query("SELECT * FROM tickets WHERE ticketCode = :code")
    fun getTicketByCode(code: String): TicketEntity?
}

@Database(entities = [TicketEntity::class], version = 1, exportSchema = false)
abstract class TestAppDatabase : RoomDatabase() {
    abstract fun ticketDao(): TicketDao
}

@RunWith(AndroidJUnit4::class)
class TicketDaoTest {

    private lateinit var db: TestAppDatabase
    private lateinit var dao: TicketDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TestAppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.ticketDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertDanGetTicketByCode_harusMengembalikanDataYangSesuai() {
        val ticket = TicketEntity(
            ticketCode = "TKT-8801",
            passengerName = "Budi Santoso",
            destination = "Surabaya Gubeng"
        )
        dao.insertTicket(ticket)

        val retrieved = dao.getTicketByCode("TKT-8801")

        assertThat(retrieved).isNotNull()
        assertThat(retrieved?.passengerName).isEqualTo("Budi Santoso")
        assertThat(retrieved?.destination).isEqualTo("Surabaya Gubeng")
    }
}
