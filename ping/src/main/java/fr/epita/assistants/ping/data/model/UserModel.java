package fr.epita.assistants.ping.data.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@JsonPropertyOrder({"id", "login", "password", "isAdmin", "displayName", "avatar"})
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "login", nullable = false, unique = true, length = 255)
    private String login;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin;

    @Column(name = "display_name", nullable = false, length = 255)
    private String displayName;

    @Column(name = "avatar", nullable = true, length = 255)
    private String avatar;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<ProjectModel> models_ = new ArrayList<>();
}
