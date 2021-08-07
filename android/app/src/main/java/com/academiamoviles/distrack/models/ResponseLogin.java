package com.academiamoviles.distrack.models;

import com.google.gson.annotations.Expose;

public class ResponseLogin {
    @Expose
    private Boolean success;
    @Expose
    private User user;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ResponseLogin{" +
                "success=" + success +
                ", user=" + user +
                '}';
    }
}
