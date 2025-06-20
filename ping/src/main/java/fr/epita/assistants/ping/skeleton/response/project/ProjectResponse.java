package fr.epita.assistants.ping.skeleton.response.project;

import fr.epita.assistants.ping.skeleton.response.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {
    private UUID id;
    private String name;
    private List<UserResponse> members;
    private UserResponse owner;
}
