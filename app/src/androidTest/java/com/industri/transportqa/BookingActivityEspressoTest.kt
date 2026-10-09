package com.industri.transportqa

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookingActivityEspressoTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun bookingTiketSukses_harusMenampilkanPesanVerifikasi() {
        // 1. Ketik NIK valid
        onView(withId(R.id.etNik))
            .perform(typeText("3209123456780001"), closeSoftKeyboard())

        // 2. Ketik nama penumpang
        onView(withId(R.id.etName))
            .perform(typeText("Ahmad Dahlan"), closeSoftKeyboard())

        // 3. Klik tombol submit
        onView(withId(R.id.btnSubmitBooking))
            .perform(click())

        // 4. Verifikasi status pemesanan
        onView(withId(R.id.tvBookingStatus))
            .check(matches(withText("Pemesanan Tiket Berhasil Diverifikasi")))
    }

    @Test
    fun nikKosong_laluSubmit_harusMenampilkanErrorDanTidakDiproses() {
        // 1. Biarkan NIK kosong, langsung tekan submit
        onView(withId(R.id.btnSubmitBooking))
            .perform(click())

        // 2. Verifikasi pesan error muncul pada kolom NIK
        onView(withId(R.id.etNik))
            .check(matches(hasErrorText("NIK tidak boleh kosong")))

        // 3. Verifikasi transaksi tidak diproses (status sukses tidak tampil)
        onView(withId(R.id.tvBookingStatus))
            .check(matches(not(withText("Pemesanan Tiket Berhasil Diverifikasi"))))
    }
}
