package com.tallerwebi.dominio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pais {

    @JsonProperty("name")
    private Nombre nombre;

    @JsonProperty("capital")
    private List<String> capitales;

    @JsonProperty("population")
    private long poblacion;

    @JsonProperty("flags")
    private Banderas banderas;

    @JsonProperty("region")
    private String region;

    @JsonProperty("subregion")
    private String subregion;

    @JsonProperty("languages")
    private Map<String, String> idiomas;

    @JsonProperty("currencies")
    private Map<String, Moneda> monedas;

    public Nombre getNombre() { return nombre; }
    public void setNombre(Nombre nombre) { this.nombre = nombre; }

    public List<String> getCapitales() { return capitales; }
    public void setCapitales(List<String> capitales) { this.capitales = capitales; }

    public long getPoblacion() { return poblacion; }
    public void setPoblacion(long poblacion) { this.poblacion = poblacion; }

    public Banderas getBanderas() { return banderas; }
    public void setBanderas(Banderas banderas) { this.banderas = banderas; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getSubregion() { return subregion; }
    public void setSubregion(String subregion) { this.subregion = subregion; }

    public Map<String, String> getIdiomas() { return idiomas; }
    public void setIdiomas(Map<String, String> idiomas) { this.idiomas = idiomas; }

    public Map<String, Moneda> getMonedas() { return monedas; }
    public void setMonedas(Map<String, Moneda> monedas) { this.monedas = monedas; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Nombre {
        @JsonProperty("common")
        private String comun;

        @JsonProperty("official")
        private String oficial;

        public String getComun() { return comun; }
        public void setComun(String comun) { this.comun = comun; }

        public String getOficial() { return oficial; }
        public void setOficial(String oficial) { this.oficial = oficial; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Banderas {
        @JsonProperty("png")
        private String png;

        @JsonProperty("svg")
        private String svg;

        public String getPng() { return png; }
        public void setPng(String png) { this.png = png; }

        public String getSvg() { return svg; }
        public void setSvg(String svg) { this.svg = svg; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Moneda {
        @JsonProperty("name")
        private String nombre;

        @JsonProperty("symbol")
        private String simbolo;

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getSimbolo() { return simbolo; }
        public void setSimbolo(String simbolo) { this.simbolo = simbolo; }
    }
}
