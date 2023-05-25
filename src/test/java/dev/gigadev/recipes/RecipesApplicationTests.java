package dev.gigadev.recipes;
import static org.mockito.Mockito.*;

import dev.gigadev.recipes.model.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dev.gigadev.recipes.model.Recipe;
import dev.gigadev.recipes.repository.RecipeRepository;
import dev.gigadev.recipes.service.RecipeService;
import org.bson.types.ObjectId;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.*;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class RecipesApplicationTests {
	@Mock
	public RecipeRepository recipeRepository;
	@InjectMocks
	public RecipeService recipeService;


	public Recipe recipe1;
	public Recipe recipe2;
	public ObjectId id1;
	public ObjectId id2;

	@BeforeEach
	public void setUp() {

		recipe1 = new Recipe();
		recipe2 = new Recipe();
		id1 = new ObjectId("6177ec310bceee97a38a12c0");
		id2 = new ObjectId("6177ec310bceee97a38a13e1");
		recipe1.setName("Pasta");
		recipe1.setId(id1);
		recipe1.setCategories(Arrays.asList("Vegan","Gluten-free"));
		recipe1.setTypes(Arrays.asList("Main Course","Appetizer"));
		recipe1.setPreparationTime(60);
		List<Ingredient> ingredients1 = new ArrayList<>();
		ingredients1.add(new Ingredient("Spaghetti", "1 lb"));
		ingredients1.add(new Ingredient("Tomato Sauce", "24 oz"));
		ingredients1.add(new Ingredient("Garlic", "3 cloves"));
		ingredients1.add(new Ingredient("Olive Oil", "2 tbsp"));
		recipe1.setIngredients(ingredients1 );
		recipe2.setName("Pizza");
		recipe2.setId(id2);
		recipe2.setCategories(Arrays.asList("Meat","Cheese"));
		recipe2.setTypes(Arrays.asList("Main Course","Snack"));
		recipe2.setPreparationTime(30);
		List<Ingredient> ingredients2 = new ArrayList<>();
		ingredients2.add(new Ingredient("Pizza Dough", "200g"));
		ingredients2.add(new Ingredient("Tomato Sauce", "100g"));
		ingredients2.add(new Ingredient("Mozzarella Cheese", "100g"));
		recipe2.setIngredients(ingredients2);

	}
	@Test
	void contextLoads() {
	}
	@Test
	public void testAllRecipes() {

		when(recipeRepository.findAll()).thenReturn(Arrays.asList(recipe1, recipe2));
		List expRes = Arrays.asList(recipe1, recipe2);
		List<Recipe> result = recipeService.allRecipes();
		assertEquals(expRes,result);
//        verify(recipeRepository, times(1)).findAll();
//        verifyNoMoreInteractions(recipeRepository);
		System.out.println("TestAllRecipes() passed");
	}

	@Test
	public void testSingleRecipe(){
		// Set up the mock repository to return the mock Recipe object when findById() is called with the mock ObjectId
		when(recipeRepository.findById(id1)).thenReturn(Optional.of(recipe1));

		// Call the service method with the mock ObjectId
		Optional<Recipe> result = recipeService.singleRecipe(id1);
		System.out.println(Optional.of(recipe1));
		System.out.println(result);
		// Assert that the result is equal to the mock Recipe object
		assertEquals(Optional.of(recipe1), result);
		verify(recipeRepository, times(1)).findById(id1);
		verifyNoMoreInteractions(recipeRepository);
		System.out.println("TestSingleRecipe() passed");
	}
	@Test
	public void testFetchRecipesByProperties() {

		List<Recipe> expectedResult = Collections.singletonList(recipe1);

		when(recipeRepository.findRecipesByProperties(recipe1.getTypes(), recipe1.getCategories(), recipe1.getPreparationTime())).thenReturn(expectedResult);

		List<Recipe> result = recipeService.fetchRecipesByProperties(recipe1.getTypes(), recipe1.getCategories(), recipe1.getPreparationTime());

		assertEquals(expectedResult, result);
		verify(recipeRepository, times(1)).findRecipesByProperties(recipe1.getTypes(), recipe1.getCategories(), recipe1.getPreparationTime());
		verifyNoMoreInteractions(recipeRepository);
		System.out.println("testFetchRecipesByProperties passed");
	}
	@Test
	public void testFindRecipesByText() {
		// Define a search phrase
		String searchPhrase = "pasta";

		// Define the expected list of recipes
		List<Recipe> expectedRecipes = Arrays.asList(recipe1);

		// Set up the mock repository to return the expected list of recipes
		when(recipeRepository.findRecipesByText(searchPhrase)).thenReturn(expectedRecipes);

		// Call the service method with the search phrase
		List<Recipe> result = recipeService.findRecipesByText(searchPhrase);

		// Assert that the result is equal to the expected list of recipes
		assertEquals(expectedRecipes, result);
		verify(recipeRepository, times(1)).findRecipesByText(searchPhrase);
		verifyNoMoreInteractions(recipeRepository);
		System.out.println("testFindRecipesByText passed");
	}
	@Test
	public void testDeleteById() {
		// Set up the mock repository to return the mock Recipe object when findById() is called with the mock ObjectId
		when(recipeRepository.findAll()).thenReturn(Arrays.asList(recipe1, recipe2));
		// Assert that the recipe exists before deletion
//        assertEquals(Arrays.asList(recipe1, recipe2), resultBeforeDelete);
		// Delete the recipe by ID
		recipeService.deleteById(id1);
		// Verify that the findById and deleteById methods were called once with the correct ID
		verify(recipeRepository, times(1)).deleteById(id1);

	}

	@Test
	public void testSaveRecipe() {
		when(recipeRepository.save(recipe1)).thenReturn(recipe1);
		Recipe savedRecipe = recipeService.saveRecipe(recipe1);
		assertEquals(recipe1, savedRecipe);
		verify(recipeRepository, times(1)).save(recipe1);
		verifyNoMoreInteractions(recipeRepository);
	}
}
