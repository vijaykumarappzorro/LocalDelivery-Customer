package  com.example.appzaorro.myapplication.controller;




public class ModelManager {
    NearDriverManager nearDriverManager;
    FeedbackManager feedbackManager;
    UpdateProfileManager updateProfileManager;
    PlaceParser placeParser;
    SearchAddressManager searchAddressManager;
    CompleteRequestManager completeRequestManager;
    RequesDriverManager requesDriverManager;
    AcceptDriverManager acceptDriverManager;
    ForgotPasswordmanager forgotPasswordmanager;
    UserDeatilManager userDeatilManager;
    LoginManager loginManager;
    PendingRequestManager pendingRequestManager;
    FacebookLoginManager facebookLoginManager;
    RegisterManager registerManager;

    private static ModelManager modelManager;


    public ModelManager() {
        nearDriverManager = new NearDriverManager();
        feedbackManager = new FeedbackManager();
        updateProfileManager = new UpdateProfileManager();
        placeParser = new PlaceParser();
        searchAddressManager= new SearchAddressManager();
        completeRequestManager = new CompleteRequestManager();
        requesDriverManager = new RequesDriverManager();
        acceptDriverManager = new AcceptDriverManager();
        forgotPasswordmanager = new ForgotPasswordmanager();
        userDeatilManager = new UserDeatilManager();
        loginManager = new LoginManager();
        pendingRequestManager = new PendingRequestManager();
        facebookLoginManager = new FacebookLoginManager();
        registerManager = new RegisterManager();

    }

    public CompleteRequestManager getCompleteRequestManager() {
        return completeRequestManager;
    }

    public static ModelManager getInstance() {
        if (modelManager == null)
            return modelManager = new ModelManager();
        else
            return modelManager;
    }

    public FacebookLoginManager getFacebookLoginManager() {
        return facebookLoginManager;
    }

    public RegisterManager getRegisterManager() {
        return registerManager;
    }

    public PendingRequestManager getPendingRequestManager() {
        return pendingRequestManager;
    }

    public ForgotPasswordmanager getForgotPasswordmanager() {
        return forgotPasswordmanager;
    }

    public UserDeatilManager getUserDeatilManager() {
        return userDeatilManager;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public AcceptDriverManager getAcceptDriverManager() {
        return acceptDriverManager;
    }

    public RequesDriverManager getRequesDriverManager() {
        return requesDriverManager;
    }

    public PlaceParser getPlaceParser() {
        return placeParser;
    }

    public SearchAddressManager getSearchAddressManager() {
        return searchAddressManager;
    }

    public NearDriverManager getNearDriverManager() {

        return nearDriverManager;
    }

    public FeedbackManager getFeedbackManager() {
        return feedbackManager;
    }

    public UpdateProfileManager getUpdateProfileManager() {
        return updateProfileManager;
    }
}
