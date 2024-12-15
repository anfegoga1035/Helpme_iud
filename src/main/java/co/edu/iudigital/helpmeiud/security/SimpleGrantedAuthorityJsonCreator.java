package co.edu.iudigital.helpmeiud.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthorityJsonCreator {

    public SimpleGrantedAuthorityJsonCreator(@JsonProperty("authority") String role){

    }
}
