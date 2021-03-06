package edu.coloradomesa.mytutor;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static edu.coloradomesa.mytutor.Util.*;

/**
 * Created by wmacevoy on 9/12/17.
 */

public class User {

    FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

    public static class Lazy extends edu.coloradomesa.mytutor.Lazy < User > {
        Lazy(Prefs.Lazy prefs) {
            super(User.class, prefs);
        }
    }

    String name() { return fbUser.getDisplayName(); }

    Prefs.Lazy mPrefs;
    User(Prefs.Lazy prefs) {
        mPrefs=prefs;
    }

    Prefs prefs() { return mPrefs.self(); }

    boolean exists(String user) {
// valid messages must be readable as an unathenticated user for this to work (firebase->db console->database [rules tab]
        DatabaseReference mDatabase;
// Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
        return false;
    }

    boolean authenticate(String user, String password) {
        switch(user) {
            case "foo@example.com": return eq("hello",password);
            case "bar@example.com": return eq("world",password);
            case "admin@localhost": return eq("secret", password);
        }
        return false;
    }

    boolean login(String user) {
        switch(user) {
            case "foo@example.com":
            case "bar@example.com":
                prefs().authenticated(true);
                prefs().user(user);
                prefs().groups("messages");
                prefs().save();
                break;
            case "admin@localhost":
                prefs().authenticated(true);
                prefs().user("admin@localhost");
                prefs().groups("admins","messages");
                prefs().save();
                break;
            default:
                logout();
        }
        return prefs().authenticated();
    }

    void logout() {
        prefs().authenticated(false);
        prefs().user(null);
        prefs().groups(new String[] {});
        prefs().save();
    }

    boolean authenticated() { return prefs().authenticated(); }
    String user() { return prefs().user(); }
    boolean isUser() { return prefs().groups().contains("messages"); }
    boolean isAdmin() { return prefs().groups().contains("admins"); }
}

