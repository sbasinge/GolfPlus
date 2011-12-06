package com.basinc.golfminus.exceptioncontrol;

import java.io.IOException;

import javax.enterprise.context.NonexistentConversationException;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.seam.international.status.Messages;
import org.jboss.solder.exception.control.CaughtException;
import org.jboss.solder.exception.control.Handles;
import org.jboss.solder.logging.Logger;

import com.basinc.golfminus.i18n.DefaultBundleKey;

/**
 * handler those exceptions generated by conversations management
 *
 * @author <a href="http://community.jboss.org/people/spinner">Jose Freitas</a>
 */

@org.jboss.solder.exception.control.HandlesExceptions
public class ConversationExceptionHandler {
    @Inject private FacesContext facesContext;
    @Inject private Messages messages;


    /**
     * Handles the exception thrown at the end of a conversation redirecting
     * the flow to a pretty page instead of printing a stacktrace on the screen.
     *
     * @param event
     * @param log
     */
    public void conversationEndedExceptionHandler(@Handles CaughtException<NonexistentConversationException> event, Logger log) {
        log.info("Conversation ended: " + event.getException().getMessage());
        try {
            messages.info(new DefaultBundleKey("conversation_ended")).defaults("Your transaction has timed out.  Please start again.");
            facesContext.getExternalContext().redirect("home.jsf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
