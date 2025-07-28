/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel.management.system;

public class RecordData {
    private String period;
    private Integer bookings;
    private Double income;

    public RecordData(String period, Integer bookings, Double income) {
        this.period = period;
        this.bookings = bookings;
        this.income = income;
    }

    public String getPeriod() {
        return period;
    }

    public Integer getBookings() {
        return bookings;
    }

    public Double getIncome() {
        return income;
    }
}