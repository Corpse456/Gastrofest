package by.gastrofest.dto;

public record GastroSetDto(
        Long id,
        String imageLink,
        String url,
        String theme,
        Integer weight,
        Boolean eatOutside,
        Boolean booking,
        Boolean delivery,
        String gastrofest,
        String participant,
        Boolean restaurant,
        java.util.List<String> mealsImages,
        java.util.List<String> mealsDescriptions
) {

}
