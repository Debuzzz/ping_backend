package fr.epita.assistants.ping.presentation.rest;

import org.eclipse.microprofile.jwt.JsonWebToken;

import fr.epita.assistants.ping.service.ProjectService;
import fr.epita.assistants.ping.skeleton.response.project.ProjectResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/api/projects")
public class ProjectResource {
    @Inject 
    JsonWebToken jwt;

    @Inject 
    ProjectService projectService;
    
    @GET
    @RolesAllowed({"user", "admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjects(@QueryParam("onlyOwned") @DefaultValue("false") boolean onlyOwned, @Context SecurityContext ctx) {
        String username = ctx.getUserPrincipal().getName();
        
        List<ProjectResponse> projects = projectService.getProjects(username, onlyOwned);

        return Response.ok(projects).build();
    }

    // public Response createProject(NewProjectRequest newProjectRequest) {
    //     ProjectResponse resp = projectService.createProject(newProjectRequest);
    //     return Response.ok(200).entity(resp).build();

    // }
}
