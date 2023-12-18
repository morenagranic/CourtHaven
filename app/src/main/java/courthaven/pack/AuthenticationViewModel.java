package courthaven.pack;

import androidx.lifecycle.ViewModel;

public class AuthenticationViewModel extends ViewModel {

    private boolean isSignInSuccessful = false;
    private boolean isSignUpSuccessful = false;

    public boolean isSignInSuccessful() {
        return isSignInSuccessful;
    }

    public void setSignInSuccessful(boolean signInSuccessful) {
        isSignInSuccessful = signInSuccessful;
    }

    public boolean isSignUpSuccessful() {
        return isSignUpSuccessful;
    }

    public void setSignUpSuccessful(boolean signUpSuccessful) {
        isSignUpSuccessful = signUpSuccessful;
    }
}