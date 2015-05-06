package com.example.session.resources;

import com.example.session.dao.SessionDao;
import com.example.session.model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;

@Path("/sessions")
public class SessionResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionResource.class);

    private final SessionDao sessionDao;

    public SessionResource(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    @POST
    public String createSession(String callerId) {
        LOGGER.info("Create session for callerId: " + callerId);

        Session newSession = new Session(callerId);
        sessionDao.insert(newSession);

        return newSession.getAccessToken();
    }

    @GET
    @Path("/{accessToken}")
    public String getSession(@PathParam("accessToken") String accessToken) {
        LOGGER.info("Validate session for accessToken: " + accessToken);

        Session session = sessionDao.findByAccessToken(accessToken);

        if (session != null) {
            return session.getCallerId();
        }

        throw new WebApplicationException("could not find accessToken", 404);
    }

    @DELETE
    @Path("/{accessToken}")
    public void deleteSession(@PathParam("accessToken") String accessToken) {
        LOGGER.info("Delete session for accessToken: " + accessToken);

        sessionDao.deleteByAccessToken(accessToken);
    }
}