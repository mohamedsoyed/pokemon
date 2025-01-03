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

    @PUT
    @Path("/{pokemonId}/entrainement")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response entrainerPokemon(
            @PathParam("pokemonId") Long pokemonId,
            @QueryParam("userId") Long userId,
            @QueryParam("pourcentage") double pourcentage) {

        try {
            // Appel de la méthode métier pour entraîner le Pokémon
            if (pourcentage > 10) {
                throw new IllegalArgumentException("Un Pokémon ne peut gagner plus de 10% par session.");
            }

             pokemonService.entrainerPokemon(pokemonId, userId, pourcentage);

            return Response.ok().entity(
                    String.format("Le Pokémon avec l'ID %d a été entraîné par l'utilisateur %d avec un gain de %.2f%%.",
                            pokemonId, userId, pourcentage)
            ).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Une erreur est survenue lors de l'entraînement du Pokémon.")
                    .build();
        }
    }
}

