package io.nowave.api.model;


import org.springframework.data.annotation.Id;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

public class User {

    @Id
    private String id;

    private String salt;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String billingId;

    /**
     * Needed for Jackson JSON Mapper
     */
    private User() {

    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.salt = UUID.randomUUID().toString().replace("-", "");
        this.password = encode(password);
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = encode(password);
    }

    public String getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public Boolean hasSubscribed() {
        return !StringUtils.isEmpty(billingId);
    }

    public Boolean validate(String password) {
        return encode(password).equals(this.password);
    }

    public void setBillingId(String billingId) {
        this.billingId = billingId;
    }

    public String getBillingId() {
        return billingId;
    }

    private String encode(String password) {

        String saltedPassword = salt + password;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return new String(digest.digest(saltedPassword.getBytes(UTF_8)));

        } catch (NoSuchAlgorithmException e) {
            return "-1";
        }
    }
}