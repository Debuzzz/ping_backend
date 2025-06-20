package fr.epita.assistants.ping.skeleton.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequest {
    private String login;
    private String password;
    private Boolean isAdmin;
}