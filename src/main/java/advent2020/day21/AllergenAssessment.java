package advent2020.day21;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class AllergenAssessment {


    List<String> ingredients = new ArrayList<>();
    // Map of <Allergen - List< Variants<Ingredients>>>
    Map<String, List<String>> mapping = new HashMap<>();
    private static final Predicate<Map.Entry<String, List<String>>> SINGLE_LIST = entry -> entry.getValue().size() == 1;

    public void parseRawInput(List<String> recipes){
        for (String recipe : recipes) {
            List<String> ingredients = Stream.of(recipe.substring(0, recipe.indexOf("(")).split(" ")).collect(Collectors.toList());
            Stream.of(recipe.substring(recipe.indexOf("(contains ") + 10, recipe.length() - 1).split(","))
                  .map(String::trim)
                  .peek(allergen -> mapping.computeIfAbsent(allergen, (s1) -> new ArrayList<>(ingredients)))
                  .forEach(allergen -> mapping.get(allergen).retainAll(ingredients));
            this.ingredients.addAll(ingredients);
        }
    }

    public Map<String, String> deduceIngredientAllergens(){
        int numberOfAllergens = mapping.size();
        // allergen - ingredient
        Map<String, String> matches = new HashMap<>();
        while(numberOfAllergens > matches.size()){
            mapping.entrySet().stream().filter(SINGLE_LIST).forEach(combo -> matches.put(combo.getKey(), combo.getValue().get(0)));
            mapping.values().stream().forEach(ingredients -> ingredients.removeAll(matches.values()));
        }
        return matches;
    }

    public Long calculateNumberIngredientsWithNoAllergens(){
        Map<String, String> allergenToIngredient = deduceIngredientAllergens();
        return this.ingredients.stream().filter(ingredient -> !allergenToIngredient.values().contains(ingredient)).count();
    }
}
