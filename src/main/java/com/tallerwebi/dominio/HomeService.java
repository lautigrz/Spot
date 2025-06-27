package com.tallerwebi.dominio;


import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsAlbumsRequest;

import java.io.IOException;

@Service
public class HomeService {

    private static final String clientId = "7f4bb7b2a55e496d9a417e07aa7042c3";
    private static final String clientSecret = "15a50d49adf542fc808a919eb6da61ea";

    private final SpotifyApi spotifyApi;

    public HomeService() {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();

        try {
            ClientCredentialsRequest credentialsRequest = spotifyApi.clientCredentials().build();
            ClientCredentials credentials = credentialsRequest.execute();
            spotifyApi.setAccessToken(credentials.getAccessToken());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
        }
    }

    public AlbumSimplified[] getAlbumsByArtist(String artistId) throws IOException, SpotifyWebApiException, ParseException {
        GetArtistsAlbumsRequest albumsRequest = spotifyApi.getArtistsAlbums(artistId)
                .limit(10)
                .build();

        Paging<AlbumSimplified> albumPaging = albumsRequest.execute();
        return albumPaging.getItems();
    }

}
