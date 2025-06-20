package fr.epita.assistants.ping.skeleton.response.user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"token"})
public class LoginResponse {
    private String token;
}
