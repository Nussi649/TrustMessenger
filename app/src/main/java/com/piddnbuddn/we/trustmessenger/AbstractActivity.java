package com.piddnbuddn.we.trustmessenger;

import android.app.Activity;
import backend.Model;
import backend.Controller;

/**
 * Created by ich on 07.10.2017.
 */

public abstract class AbstractActivity extends Activity {
    Model model;
    Controller controller;

    public Model getModel() {
        return model;
    }

    public Controller getController() {
        return controller;
    }
}
