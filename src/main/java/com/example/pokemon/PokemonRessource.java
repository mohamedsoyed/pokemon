package com.example.pokemon;


import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/pokemons")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class PokemonRessource {

    @Inject
    PokemonService pokemonService;


    @GET
    public List<Pokemon> listerPokemons() {
        return pokemonService.listerPokemons();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/random")
    public Pokemon pokemonAleatoire() {
        return pokemonService.getRandomPokemon();
    }





    @POST
    public Pokemon creerPokemon(Pokemon pokemon){
        return pokemonService.creerPokemon(pokemon.getNom(), pokemon.getDescription(), pokemon.getValeurReelle());
    }

    @GET
    @Path("/{id}")
    public Pokemon trouverPokemon(@PathParam("id") Long id) {
        return pokemonService.trouverPokemon(id);
    }

    @DELETE
    @Path("/{id}")
    public void supprimerPokemon(@PathParam("id") Long id) {
        pokemonService.supprimerPokemon(id);
    }

    @POST
    @Path("/{pokemonId}/addAuction")
    @Transactional
    public Response addAuctionHistory(
            @PathParam("pokemonId") Long pokemonId,
            Enchere enchere
    ) {
        try {
            pokemonService.addAuctionHistory(pokemonId, enchere);
            return Response.ok("Enchère ajoutée avec succès.").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}

