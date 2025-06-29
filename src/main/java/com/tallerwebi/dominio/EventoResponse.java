package com.tallerwebi.dominio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public class EventoResponse {
    private Embedded _embedded;

    public Embedded get_embedded() {
        return _embedded;
    }

    public void set_embedded(Embedded _embedded) {
        this._embedded = _embedded;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Embedded {
        private List<Evento> events;

        public List<Evento> getEvents() {
            return events;
        }

        public void setEvents(List<Evento> events) {
            this.events = events;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Evento {
        private String name;
        private String url;
        private Fecha dates;
        private List<Imagen> images;
        private EmbeddedVenue _embedded;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Fecha getDates() {
            return dates;
        }

        public void setDates(Fecha dates) {
            this.dates = dates;
        }

        public List<Imagen> getImages() {
            return images;
        }

        public void setImages(List<Imagen> images) {
            this.images = images;
        }

        public EmbeddedVenue get_embedded() {
            return _embedded;
        }

        public void set_embedded(EmbeddedVenue _embedded) {
            this._embedded = _embedded;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Fecha {
        private Start start;

        public Start getStart() {
            return start;
        }

        public void setStart(Start start) {
            this.start = start;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Start {
        private String localDate;

        public String getLocalDate() {
            return localDate;
        }

        public void setLocalDate(String localDate) {
            this.localDate = localDate;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Imagen {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EmbeddedVenue {
        private List<Venue> venues;

        public List<Venue> getVenues() {
            return venues;
        }

        public void setVenues(List<Venue> venues) {
            this.venues = venues;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Venue {
        private String name;
        private City city;
        private Country country;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public City getCity() {
            return city;
        }

        public void setCity(City city) {
            this.city = city;
        }

        public Country getCountry() {
            return country;
        }

        public void setCountry(Country country) {
            this.country = country;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class City {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Country {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
