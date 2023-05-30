package dev.gigadev.recipes.service;
import dev.gigadev.recipes.model.Ingredient;
import dev.gigadev.recipes.model.Recipe;
import dev.gigadev.recipes.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public ResponseEntity<Resource> exportRecipeToPdf(ObjectId recipeId) throws Exception {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new Exception("Recipe not found"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);

        document.open();
        document.add(new Paragraph(recipe.getName()));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        PdfPCell cell;

        cell = new PdfPCell(new Paragraph("Ingredients"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Preparation Time"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        for (Ingredient ingredient : recipe.getIngredients()) {
            cell = new PdfPCell(new Paragraph(ingredient.getName()));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ingredient.getQuantity()));
            table.addCell(cell);
        }

        cell = new PdfPCell(new Paragraph("Total Preparation Time"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(1);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph(recipe.getPreparationTime().toString() + " minutes"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        document.add(table);
        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", recipe.getName() + ".pdf");
        ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(baos.size())
                .body(resource);
    }

    public List<Recipe> allRecipes() {
        return recipeRepository.findAll();
    }

    public List<Recipe> fetchRecipesByProperties(List<String> types, List<String> categories,
                                                 Integer preparationTime) {
        return recipeRepository.findRecipesByProperties(types, categories, preparationTime);
    }

    public List<Recipe> findRecipesByText(String searchPhrase) {
        return recipeRepository.findRecipesByText(searchPhrase);
    }

    public Optional<Recipe> singleRecipe(ObjectId id) {
        return recipeRepository.findById(id);
    }

    public void deleteById(ObjectId id) {
        recipeRepository.deleteById(id);
    }

    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }
}
