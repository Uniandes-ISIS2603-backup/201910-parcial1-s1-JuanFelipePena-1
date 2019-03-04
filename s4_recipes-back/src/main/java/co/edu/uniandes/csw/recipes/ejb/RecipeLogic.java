/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.recipes.ejb;

import co.edu.uniandes.csw.recipes.entities.RecipeEntity;
import co.edu.uniandes.csw.recipes.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.recipes.persistence.RecipePersistence;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author CesarF
 */
@Stateless
public class RecipeLogic {
    @Inject
    private RecipePersistence persistence; // Variable para acceder a la persistencia de la aplicación. Es una inyección de dependencias.

    public RecipeEntity getRecipe(Long id) {
        return persistence.find(id);
    }

    //TODO crear el método createRecipe
    public RecipeEntity createRecipe (RecipeEntity recipeEntity) throws BusinessLogicException
    {
        String nombre = persistence.find(recipeEntity.getId()).getName();
        
        String descripcion = persistence.find(recipeEntity.getId()).getDescription();
        
        if(nombre != null)
        {
             throw new BusinessLogicException("Ya existe una Recipe con el nombre \"" + recipeEntity.getName());
        }
        
        if( nombre.equals("") || nombre.length() > 30)
        {
            throw new BusinessLogicException( 
                    "\" el nombre es vacío o supera el límite de carácteres");
        }
        
        if( descripcion.equals("") || descripcion.length() > 150)
        {
            throw new BusinessLogicException( 
                    "\" o la descripción es vacía o supera el límite de carácteres");
        }
        
        // falta algo if( )
        
        persistence.createRecipe(recipeEntity);

        return recipeEntity;
    }
}
