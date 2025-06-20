package fr.epita.assistants.ping.service;

import fr.epita.assistants.ping.data.model.ProjectModel;
import fr.epita.assistants.ping.data.model.UserModel;
import fr.epita.assistants.ping.data.repository.ProjectRepository;
import fr.epita.assistants.ping.data.repository.UserRepository;
import fr.epita.assistants.ping.skeleton.request.ProjectRequest;
import fr.epita.assistants.ping.skeleton.response.project.ProjectResponse;
import fr.epita.assistants.ping.skeleton.response.user.UserResponse;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@ApplicationScoped
public class ProjectService {
    @Inject
    UserRepository userRepository;

    @Inject
    ProjectRepository projectRepository;

    @Inject
    SecurityIdentity securityIdentity;


    public List<ProjectResponse> getProjects(String username, boolean onlyOwned) {
        UserModel user = userRepository.findByLogin(username);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        List<ProjectModel> projectModels;
        if (onlyOwned) {
            projectModels = projectRepository.findOwnedBy(user);
        } else {
            projectModels = projectRepository.findMemberOf(user);
        }

        return projectModels.stream()
                            .map(this::mapToProjectResponse)
                            .collect(Collectors.toList());
    }

    private ProjectResponse mapToProjectResponse(ProjectModel projectModel) {
        UserResponse ownerResponse = new UserResponse();
        UserModel owner = projectModel.getOwner();
        ownerResponse.setId(owner.getId());
        ownerResponse.setDisplayName(owner.getDisplayName());
        ownerResponse.setAvatar(owner.getAvatar());

        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setId(projectModel.getId());
        projectResponse.setName(projectModel.getName());
        projectResponse.setOwner(ownerResponse);

        List<UserResponse> membersResponse = projectModel.getMembers().stream().map(member -> {
            UserResponse user = new UserResponse();
            user.setId(member.getId());
            user.setDisplayName(member.getDisplayName());
            user.setAvatar(member.getAvatar());
            return user;
        }).collect(Collectors.toList());
        projectResponse.setMembers(membersResponse);

        return projectResponse;
    }


    public ProjectResponse getProjectByName(String name) {
        ProjectModel pr = projectRepository.getProject(name);
        if (pr == null) {
            throw new NotFoundException("Project not found");
        }
        return mapToProjectResponse(pr);
    }

    public Response createProject(ProjectRequest newProjectRequest) {
        if (newProjectRequest.getName() == null || newProjectRequest.getName().isEmpty()) {
            throw new RuntimeException("Project name cannot be empty");
        }
        // String name = newProjectRequest.getName();

        return Response.noContent().build();
    }
}
