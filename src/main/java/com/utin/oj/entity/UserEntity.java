package com.utin.oj.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static jakarta.persistence.FetchType.EAGER;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@JsonInclude(NON_DEFAULT)
public class UserEntity extends Auditable{
    @Column(updatable = false, unique = true, nullable = false)
    private String userId;
    private String firstname;
    private String lastname;
    @Column(unique = true, nullable = false)
    private String email;
    private Integer loginAttempts;
    private LocalDateTime lastLogin;
    private String phone;
    private String bio;
    private String imageUrl;
    private Boolean accountNonExpired;
    private Boolean AccountNonLocked;
    private Boolean enabled;
    private Boolean mfa;
    @JsonIgnore
    private String qrCodeSecret;
    @Column(columnDefinition = "text")
    private String qrCodeImageUri;
    @ManyToOne(fetch = EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
                    inverseJoinColumns = @JoinColumn(
                            name = "role_id", referencedColumnName = "id"))
    private RoleEntity role;

    public String getFirstName() {
        return  firstname;
    }

    public String getLastName() { return lastname;}
}