package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user", nullable = false)
    private Integer idUser;

    @Column(name = "flg_status", length = 1)
    private Character flgStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "nama", length = 50)
    private String nama;

    @Column(name = "password", length = 50)
    private String password;

    @Column(name = "ip_login", length = 20)
    private String ipLogin;

    @Column(name = "tgl_status")
    private LocalDateTime tglStatus;

    @Column(name = "id_session", length = 100)
    private String idSession;

    @Column(name = "id_group", length = 20)
    private String idGroup;
}
