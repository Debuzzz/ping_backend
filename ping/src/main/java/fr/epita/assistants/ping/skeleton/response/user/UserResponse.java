package fr.epita.assistants.ping.skeleton.response.user;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "login", "displayName", "isAdmin", "avatar"})
public class UserResponse {
    private UUID id;
    private String login;
    private String displayName;
    private Boolean isAdmin;
    private String avatar;
}
