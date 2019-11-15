package Classes;

import android.os.Parcelable;

public class FriendsData {

    public boolean isPhone() {
        return phone;
    }

    public void setPhone(boolean phone) {
        this.phone = phone;
    }

    public boolean isFacebook() {
        return facebook;
    }

    public void setFacebook(boolean facebook) {
        this.facebook = facebook;
    }

    public boolean isInstagram() {
        return instagram;
    }

    public void setInstagram(boolean instagram) {
        this.instagram = instagram;
    }

    public boolean isTwitter() {
        return twitter;
    }

    public void setTwitter(boolean twitter) {
        this.twitter = twitter;
    }

    public boolean isLinkedin() {
        return linkedin;
    }

    public void setLinkedin(boolean linkedin) {
        this.linkedin = linkedin;
    }

    public boolean isSnapchat() {
        return snapchat;
    }

    public void setSnapchat(boolean snapchat) {
        this.snapchat = snapchat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    boolean phone;
    boolean facebook;
    boolean instagram;
    boolean twitter;
    boolean linkedin;
    boolean snapchat;


    public boolean isEmaill() {
        return emaill;
    }

    public void setEmaill(boolean emaill) {
        this.emaill = emaill;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    boolean emaill;

    String name;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String username;
    String email;
}
