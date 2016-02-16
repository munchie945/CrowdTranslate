package edu.rosehulman.manc.crowdtranslate;

/**
 * Created by manc on 2/15/2016.
 */
public interface OnLoginListener {

    public void onSuccessfulLogin(String userUid);

    /**
     * New account was created (and logged into)
     * @param userUid UID of current user
     */
    public void onAccountCreated(String userUid);

}
