package fr.epita.assistants.ping.data.repository;

import fr.epita.assistants.ping.data.model.ProjectModel;
import fr.epita.assistants.ping.data.model.UserModel;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ProjectRepository implements PanacheRepositoryBase<ProjectModel, UUID> {
    public ProjectModel getProject(String name)
    {
        return find("name", name).firstResult();
    }
    @Transactional
    public ProjectModel createProject(String name, UserModel owner) {
        ProjectModel projectModel = new ProjectModel();
        projectModel.setName(name);
        projectModel.setMembers(new ArrayList<UserModel>() {
        });
        projectModel.setPath("/home/martin/ping-projects" + "/" + projectModel.getId().toString());
        projectModel.setOwner(owner);
        projectModel.getMembers().add(owner);
        persist(projectModel);
        return projectModel;
    }

    public List<ProjectModel> findOwnedBy(UserModel owner) {
        return list("owner_", owner);
    }

    public List<ProjectModel> findMemberOf(UserModel member) {
        return list("?1 member of members", member);
    }
}
