package com.puppet.frontendpracticeservice.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "requisites")
public class Requisites {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Pattern(regexp = "\\d{20}", message = "Расчетный счет состоит из 20 цифр")
    @Column(name = "current_account", nullable = false)
    private String currentAccount;

    @Pattern(regexp = "301\\d{17}", message = "Корр.счет не соответствует формату")
    @Column(name = "corr_account", nullable = false)
    private String corrAccount;

    @Pattern(regexp = "\\d{9}", message = "БИК состоит из 9 цифр")
    @Column(nullable = false)
    private String bic;

    @Pattern(regexp = "\\d{10}", message = "ИНН юр.лица состоит из 10 цифр")
    @Column(nullable = false)
    private String inn;

    @Pattern(regexp = "\\d{20}", message = "КБК состоит из 20 цифр")
    @Column(nullable = false)
    private String kbk;

    @Pattern(regexp = "\\d{9}", message = "КПП состоит из 9 цифр")
    @Column(nullable = false)
    private String kpp;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}