package com.josef.mobile.vfree.ui.auth;

import com.josef.mobile.vfree.ui.auth.email.help.InputFieldHelper;
import com.josef.mobile.vfree.ui.auth.email.help.QuartetLiveData;
import com.josef.mobile.vfree.ui.base.BaseViewModel;
import org.jetbrains.annotations.NotNull;
import javax.inject.Inject;

public class AuthInputViewModel extends BaseViewModel {

    private final QuartetLiveData<CharSequence, CharSequence,
            CharSequence, CharSequence> combiner = new QuartetLiveData<>();

    private final InputFieldHelper firstNameHelper = new InputFieldHelper();
    private final InputFieldHelper lastNameHelper = new InputFieldHelper();
    private final InputFieldHelper emailHelper = new InputFieldHelper();
    private final InputFieldHelper passwordHelper = new InputFieldHelper();


    @Inject
    public AuthInputViewModel() {
        combiner.combinedLiveData(firstNameHelper.getLiveData(),
                                  lastNameHelper.getLiveData(),
                                  emailHelper.getLiveData(),
                                  passwordHelper.getLiveData());
    }

    @NotNull
    public void verifyUsersInput() {

        combiner.removeLiveData(firstNameHelper.getLiveData(),
                lastNameHelper.getLiveData(),
                emailHelper.getLiveData(),
                passwordHelper.getLiveData());

        combiner.combinedLiveData(firstNameHelper.getLiveData(),
                lastNameHelper.getLiveData(),
                emailHelper.getLiveData(),
                passwordHelper.getLiveData());

    }


    @NotNull
    public void verifyEmailInputs(CharSequence input) {
        emailHelper.verifyInputs(input);
    }

    @NotNull
    public void verifyPasswordInputs(CharSequence input) {
        passwordHelper.verifyInputs(input);
    }
    @NotNull
    public void verifyFirstInputs(CharSequence input) {
        firstNameHelper.verifyInputs(input);
    }

    @NotNull
    public void verifyLastInputs(CharSequence input) {
        lastNameHelper.verifyInputs(input);
    }


    @NotNull
    public QuartetLiveData<CharSequence, CharSequence, CharSequence, CharSequence> getCombiner() {
        return combiner;
    }
    @NotNull
    public InputFieldHelper getFirstNameHelper() {
        return firstNameHelper;
    }
    @NotNull
    public InputFieldHelper getLastNameHelper() {
        return lastNameHelper;
    }
    @NotNull
    public InputFieldHelper getEmailHelper() {
        return emailHelper;
    }
    @NotNull
    public InputFieldHelper getPasswordHelper() {
        return passwordHelper;
    }

}
