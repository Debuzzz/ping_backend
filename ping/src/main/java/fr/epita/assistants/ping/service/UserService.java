package fr.epita.assistants.ping.service;

import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.jwt.JsonWebToken;

import fr.epita.assistants.ping.data.model.UserModel;
import fr.epita.assistants.ping.data.repository.UserRepository;
import fr.epita.assistants.ping.errors.authentification.InvalidPasswordException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UserService {
    @Inject 
    UserRepository userRepository;

    @Inject
    JsonWebToken jwt;
    
    @Transactional
    public UserModel addUser(UserModel userModel) {
        userRepository.persist(userModel);
        return userModel;
    }

    public UserModel findByLogin(String login) {
        return userRepository.find("login", login).firstResult();
    }

    public UserModel findById(UUID id) throws NotFoundException {
        UserModel user = userRepository.find("id", id).firstResult();
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user;
    }

    @Transactional
    public UserModel deleteUser(UUID id) {
        UserModel user = findById(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        userRepository.delete("id", id);
        return user;
    }

    public List<UserModel> findAll() {
        return userRepository.listAll();
    }

    public Boolean validateLogin(String login, String password) {
        try {
            UserModel user = findByLogin(login);
            if (!user.getPassword().equals(password)) {
                throw new InvalidPasswordException(password);
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}


