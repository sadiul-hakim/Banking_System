package xyz.sadiulhakim.customer;

import java.time.LocalDate;
import java.util.Objects;

public class Customer {
    private int id;
    private String name;
    private String email;
    private String nid;
    private String phone;
    private LocalDate dateOfBirth;
    private int addressId;

    public Customer(int id, String name, String email, String nid, String phone, LocalDate dateOfBirth, int addressId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.nid = nid;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.addressId = addressId;
    }

    public Customer(String name, String email, String nid, String phone, LocalDate dateOfBirth, int addressId) {
        this.name = name;
        this.email = email;
        this.nid = nid;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.addressId = addressId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Customer customer)) return false;

        return id == customer.id && Objects.equals(email, customer.email) && Objects.equals(nid, customer.nid) &&
                Objects.equals(phone, customer.phone);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(nid);
        result = 31 * result + Objects.hashCode(phone);
        return result;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", nid='" + nid + '\'' +
                ", phone='" + phone + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", addressId=" + addressId +
                '}';
    }
}
