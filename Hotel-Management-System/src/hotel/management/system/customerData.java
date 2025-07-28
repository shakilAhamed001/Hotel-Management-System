/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel.management.system;

import java.util.Date;

public class customerData {
    private Integer customerNum;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Double total;
    private Date checkIn;
    private Date checkOut;

    public customerData(Integer customerNum, String firstName, String lastName, String phoneNumber, Double total, Date checkIn, Date checkOut) {
        this.customerNum = customerNum;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.total = total;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public Integer getCustomerNum() {
        return customerNum;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Double getTotal() {
        return total;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }
}