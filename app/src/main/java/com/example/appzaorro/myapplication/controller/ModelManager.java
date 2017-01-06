package  com.example.appzaorro.myapplication.controller;




public class ModelManager {


    private static ModelManager modelManager;

    public ModelManager() {

    }

    public static ModelManager getInstance() {
        if (modelManager == null)
            return modelManager = new ModelManager();
        else
            return modelManager;
    }


}
