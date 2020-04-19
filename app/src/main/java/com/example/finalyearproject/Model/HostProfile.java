package com.example.finalyearproject.Model;

public class HostProfile {

    public String hostID;
    public String hostEmail;
    public String hostName;
    public String hostPhone;

    public HostProfile()
    {

    }


    public HostProfile(String hostID, String hostEmail, String hostName, String hostPhone) {
        this.hostID = hostID;
        this.hostEmail = hostEmail;
        this.hostName = hostName;
        this.hostPhone = hostPhone;
    }

    public String getHostID() {
        return hostID;
    }

    public void setHostID(String hostID) {
        this.hostID = hostID;
    }

    public String getHostEmail() {
        return hostEmail;
    }

    public void setHostEmail(String hostEmail) {
        this.hostEmail = hostEmail;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostPhone() {
        return hostPhone;
    }

    public void setHostPhone(String hostPhone) {
        this.hostPhone = hostPhone;
    }
}
