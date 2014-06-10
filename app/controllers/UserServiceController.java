package controllers;

import securesocial.core.Identity;
import securesocial.core.IdentityId;
import securesocial.core.java.BaseUserService;
import securesocial.core.java.Token;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class UserServiceController extends BaseUserService {

    public UserServiceController(play.Application application) {
        super(application);
    }

    /**
     * Saves the user.  This method gets called when a user logs in.
     * This is your chance to save the user information in your backing store.
     * @param user
     */
    @Override
    public Identity doSave(Identity user) {
        // TODO IMPLEMENT
        throw new NotImplementedException();
    }

    /**
     * Finds an Identity in the backing store.	     
     * @return an Identity instance or null if no user matches the specified id
     */
    @Override
    public Identity doFind(IdentityId id) {
        // TODO IMPLEMENT
        throw new NotImplementedException();
    }

    /**
     * Finds an identity by email and provider id.
     *
     * Note: If you do not plan to use the UsernamePassword provider just provide en empty
     * implementation.
     *
     * @param email - the user email
     * @param providerId - the provider id
     * @return an Identity instance or null if no user matches the specified id
     */
    @Override
    public Identity doFindByEmailAndProvider(String email, String providerId) {
        // TODO IMPLEMENT
        throw new NotImplementedException();
    }

    /**
     * Saves a token
     */
    @Override
    public void doSave(Token token) {
        // TODO IMPLEMENT
        throw new NotImplementedException();
    }

    /**
     * Finds a token by id
     *
     * Note: If you do not plan to use the UsernamePassword provider just provide en empty
     * implementation
     *
     * @return a Token instance or null if no token matches the id
     */
    @Override
    public Token doFindToken(String tokenId) {
        // No implementation required.
        return null;
    }


    /**
     * Deletes a token
     *
     * Note: If you do not plan to use the UsernamePassword provider just provide en empty
     * implementation
     *
     * @param uuid the token id
     */
    @Override
    public void doDeleteToken(String uuid) {
        // No implementation required.
    }

    /**
     * Deletes all expired tokens
     *
     * Note: If you do not plan to use the UsernamePassword provider just provide en empty
     * implementation
     *
     */
    @Override
    public void doDeleteExpiredTokens() {
        // TODO IMPLEMENT
    }
}
