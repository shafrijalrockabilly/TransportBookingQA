package com.industri.transportqa

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class FareCalculatorTest {

    private lateinit var calculator: FareCalculator

    @Before
    fun setUp() {
        calculator = FareCalculator()
    }

    @Test
    fun `isValidNik dengan 16 digit angka harus bernilai true`() {
        assertThat(calculator.isValidNik("3209123456780001")).isTrue()
    }

    @Test
    fun `isValidNik dengan kurang dari 16 digit harus bernilai false`() {
        assertThat(calculator.isValidNik("32091234")).isFalse()
    }

    @Test
    fun `isValidNik mengandung huruf harus bernilai false`() {
        assertThat(calculator.isValidNik("320912345678ABCD")).isFalse()
    }

    @Test
    fun `calculateTotalFare untuk penumpang lansia umur 65 tahun mendapat diskon 20 persen`() {
        val total = calculator.calculateTotalFare(500000.0, passengerAge = 65, baggageWeightKg = 15)
        assertThat(total).isEqualTo(400000.0)
    }

    @Test
    fun `calculateTotalFare dengan kelebihan bagasi 25 kg dikenakan denda bagasi`() {
        val total = calculator.calculateTotalFare(500000.0, passengerAge = 30, baggageWeightKg = 25)
        assertThat(total).isEqualTo(625000.0)
    }

    // ===== TUGAS 1: EDGE CASE =====

    @Test
    fun `calculateTotalFare dengan tarif dasar negatif harus melempar IllegalArgumentException`() {
        assertThrows(IllegalArgumentException::class.java) {
            calculator.calculateTotalFare(-10000.0, passengerAge = 30, baggageWeightKg = 10)
        }
    }

    @Test
    fun `calculateTotalFare untuk balita usia 2 tahun harus bernilai nol`() {
        val total = calculator.calculateTotalFare(500000.0, passengerAge = 2, baggageWeightKg = 0)
        assertThat(total).isEqualTo(0.0)
    }
}
