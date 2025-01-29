package com.puppet.frontendpracticeservice.domain.entity;

import com.puppet.frontendpracticeservice.security.Role;
import com.puppet.frontendpracticeservice.validation.annotation.ValidLogin;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    private UUID id;

    @NotBlank(message = "Имя пользователя не может быть пустым")
    @NotNull
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Фамилия пользователя не может быть пустым")
    @NotNull
    @Column(nullable = false)
    private String surname;

    @Past(message = "Дата рождения должна быть в прошлом")
    @Column(nullable = false)
    private LocalDate birthday;

    @Pattern(regexp = "\\d{12}", message = "В ИНН должно быть 12 цифр")
    private String inn;

    @Pattern(regexp = "\\d{11}", message = "В СНИЛС должно быть 12 цифр")
    @Column(nullable = false)
    private String snils;

    @Pattern(regexp = "\\d{10}", message = "В номере паспорта должно быть 12 цифр")
    @Column(nullable = false)
    private String passport;

    @ValidLogin(min = 6, max = 30, message = "Логин должен состоять из 6-30 символов")
    @Column(unique = true, nullable = false)
    private String login;

    @Size(min = 8, message = "В пароле должно быть не менее 8 символов")
    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "roles",
            joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Set<Role> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Requisites requisites;
}