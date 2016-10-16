package com.ogsteam.ogspy.network.responses;

/**
 * Created by jpspa on 15/10/2016.
 */

public class AllianceResponse implements OgspyResponse {
    private String ownAlliance;
    private String nbMembers;

    public String getOwnAlliance() {
        return ownAlliance;
    }

    public void setOwnAlliance(String ownAlliance) {
        this.ownAlliance = ownAlliance;
    }

    public String getNbMembers() {
        return nbMembers;
    }

    public void setNbMembers(String nbMembers) {
        this.nbMembers = nbMembers;
    }
}
