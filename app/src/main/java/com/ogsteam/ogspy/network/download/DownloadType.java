package com.ogsteam.ogspy.network.download;

/**
 * Created by jp.tessier on 18/12/13.
 */
public enum DownloadType {
    SERVER(0, "Download des données d'espionage"),
    ALLIANCE(1, "Download des données d'espionage"),
    SPY(2, "Download des données d'espionage"),
    HOSTILES(3, "Download des données Hostiles"),
    RENTABILITES(4, "Download des données de Rentabilité"),
    SERVER_CONNECTION(5, "Download des données de connexion au server");

    private final int type;
    private final String libelle;

    DownloadType(int type, String libelle) {
        this.type = type;
        this.libelle = libelle;
    }

    public int getType() {
        return type;
    }

    public String getLibelle() {
        return libelle;
    }
}
