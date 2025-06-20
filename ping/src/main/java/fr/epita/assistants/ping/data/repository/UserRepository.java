package fr.epita.assistants.ping.data.repository;

import fr.epita.assistants.ping.data.model.UserModel;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<UserModel, UUID> {

    public UserModel findByLogin(String login) {
        return find("login", login).firstResult();
    }

    public List<UserModel> findByProjectId(UUID projectId) {
        return list("project_id", projectId);
    }
}
