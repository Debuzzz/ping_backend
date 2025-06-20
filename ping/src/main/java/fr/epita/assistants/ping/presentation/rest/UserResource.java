package fr.epita.assistants.ping.presentation.rest;

import java.util.UUID;

import org.eclipse.microprofile.jwt.JsonWebToken;

import fr.epita.assistants.ping.data.model.UserModel;
import fr.epita.assistants.ping.service.UserService;
import fr.epita.assistants.ping.skeleton.request.LoginRequest;
import fr.epita.assistants.ping.skeleton.request.UserRequest;
import fr.epita.assistants.ping.utils.ErrorInfo;
import fr.epita.assistants.ping.utils.Logger;
import fr.epita.assistants.ping.utils.TokenGen;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api")
public class UserResource {
    @Inject 
    UserService userService;

    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/user/all")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        Logger.info("===== Début getUsers() =====");
        
        // Construction d'un tableau JSON avec contrôle total sur l'ordre
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        for (UserModel user : userService.findAll()) {
            // Pour chaque utilisateur, on crée un objet JSON avec l'ordre exact des champs
            JsonObjectBuilder objBuilder = Json.createObjectBuilder()
                .add("id", user.getId().toString())
                .add("login", user.getLogin())
                .add("displayName", user.getDisplayName())
                .add("isAdmin", user.getIsAdmin());
            
            if (user.getAvatar() != null) {
                objBuilder.add("avatar", user.getAvatar());
            } else {
                objBuilder.addNull("avatar");
            }
            
            arrayBuilder.add(objBuilder);
        }
        
        Logger.success("===== Fin getUsers() =====");
        return Response.ok(arrayBuilder.build()).build();
    }

    @GET
    @Path("/user/{id}")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") String id) {
        Logger.info("===== Début getUser(id: " + id + ") =====");
        // On essaie de récupérer le user via son id 
        try {
            UserModel user = userService.findById(UUID.fromString(id));
            
            // Construction JSON avec ordre précis des champs
            JsonObjectBuilder objBuilder = Json.createObjectBuilder()
                .add("id", user.getId().toString())
                .add("login", user.getLogin())
                .add("displayName", user.getDisplayName())
                .add("isAdmin", user.getIsAdmin());
            
            if (user.getAvatar() != null) {
                objBuilder.add("avatar", user.getAvatar());
            } else {
                objBuilder.addNull("avatar");
            }
            
            Logger.success("User found: " + id);
            return Response.ok(objBuilder.build()).build();
        } 
        catch (NotFoundException e) {
            Logger.error("User not found: " + id);
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorInfo(e.getMessage())).build();
        }
    }

    @POST
    @Path("/user")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(UserRequest userRequest) { 
        //On vérifie la structure de notre request
        if (userRequest.getLogin() == null || userRequest.getLogin().isEmpty()) {
            return Response.status(400).entity(new ErrorInfo("Bad structure request")).build();
        }

        String[] credentials;
        String firstName, lastName;
        
        if (userRequest.getLogin().contains("-")) {
            credentials = userRequest.getLogin().split("-");
            if (credentials.length != 2) {
                return Response
                .status(400)
                .entity(new ErrorInfo("Login must be in the 'firstname-surname' or 'firstname.surname' format"))
                .build();
            }
            firstName = credentials[0];
            lastName = credentials[1];
        } else if (userRequest.getLogin().contains(".")) {
            credentials = userRequest.getLogin().split("\\.");
            if (credentials.length != 2) {
                return Response
                .status(400)
                .entity(new ErrorInfo("Login must be in the 'firstname-surname' or 'firstname.surname' format"))
                .build();
            }
            firstName = credentials[0];
            lastName = credentials[1];
        } else {
            return Response
            .status(400)
            .entity(new ErrorInfo("Login must be in the 'firstname-surname' or 'firstname.surname' format"))
            .build();
        }

        // On vérifie que le login n'existe pas déjà
        if (userService.findByLogin(userRequest.getLogin()) != null) {
            return Response.status(409).entity(new ErrorInfo("Login already exists")).build();
        }

        // On configure notre Model
        UserModel userModel = new UserModel();
        userModel.setPassword(userRequest.getPassword());
        userModel.setIsAdmin(userRequest.getIsAdmin());
        userModel.setLogin(userRequest.getLogin());
        userModel.setDisplayName(toDisplayName(userRequest.getLogin()));
        // L'erreur moulinette devrait être gérée ici -> (On doit pouvoir set un avatar null)
        // D'aprés les tests, on a bien identifié le problème ici et normalement on est bon
        userModel.setAvatar(null);

        // On ajoute l'utilisateur à la DataBase
        UserModel savedUser = userService.addUser(userModel);

        // On crée la réponse avec ordre précis des champs
        JsonObjectBuilder objBuilder = Json.createObjectBuilder()
            .add("id", savedUser.getId().toString())
            .add("login", savedUser.getLogin())
            .add("displayName", savedUser.getDisplayName())
            .add("isAdmin", savedUser.getIsAdmin());
        
        if (savedUser.getAvatar() != null) {
            objBuilder.add("avatar", savedUser.getAvatar());
        } else {
            objBuilder.addNull("avatar");
        }
        
        return Response.ok(objBuilder.build()).build();
    }

    private String toDisplayName(String login) {
        String[] parts = login.split("[._-]");
        StringBuilder displayName = new StringBuilder();
        for (String part : parts) {
            displayName.append(Character.toUpperCase(part.charAt(0)))
                       .append(part.substring(1))
                       .append(" ");
        }
        return displayName.toString().trim();
    }

    @DELETE
    @Path("/user/{id}")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") String id) {
        // On tente de supprimer le user
        try {
            Logger.info("Deleting user: " + id);
            userService.deleteUser(UUID.fromString(id));
            Logger.success("User deleted: " + id);  

            // On retourne une reponse 204
            return Response.status(204).build();
        } catch (Exception e) {
            // On a pas trouvé le user donc on retourne une erreur 404
            Logger.error("User not found: " + id);
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorInfo(e.getMessage())).build();
        }
    }

    @POST
    @Path("/user/login")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest loginRequest) {
        Logger.info("Tentative de connexion pour: " + loginRequest.getLogin());
        
        // On vérifie la structure de notre request
        Boolean validated = userService.validateLogin(loginRequest.getLogin(), loginRequest.getPassword());
        
        // Si jamais les credentials sont invalides, c'est ciao (status 401)
        if (!validated) {
            Logger.error("Échec d'authentification pour: " + loginRequest.getLogin());
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorInfo("Invalid credentials")).build();
        }

        // On récupère l'utilisateur
        UserModel user = userService.findByLogin(loginRequest.getLogin());
        
        // On génère le token
        String token = TokenGen.generateToken(user);
        
        // On crée la réponse avec ordre précis
        JsonObjectBuilder objBuilder = Json.createObjectBuilder()
            .add("token", token);
        
        Logger.success("Connexion réussie pour: " + loginRequest.getLogin() + " (isAdmin: " + user.getIsAdmin() + ")");
        
        return Response.ok(objBuilder.build()).build();
    }
}
