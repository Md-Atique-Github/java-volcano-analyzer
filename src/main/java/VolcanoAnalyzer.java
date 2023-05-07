import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class VolcanoAnalyzer {
    private List<Volcano> volcanos;

    public void loadVolcanoes(Optional<String> pathOpt) throws IOException, URISyntaxException {
        try{
            String path = pathOpt.orElse("volcano.json");
            URL url = this.getClass().getClassLoader().getResource(path);
            String jsonString = new String(Files.readAllBytes(Paths.get(url.toURI())));
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            volcanos = objectMapper.readValue(jsonString, typeFactory.constructCollectionType(List.class, Volcano.class));
        } catch(Exception e){
            throw(e);
        }
    }

    public Integer numbVolcanoes(){
        return volcanos.size();
    }

    public List<Volcano> eruptedInEighties(){
        return volcanos.stream().filter(i-> i.getYear() >= 1980 && i.getYear()< 1990 ) .collect(Collectors.toList());
    }

    

    public String[] highVEI() {
        List<String> highVEIVolcanoes = new ArrayList<>();
        for (Volcano volcano : volcanos) {
            if (volcano.getVEI() >= 6) {
                highVEIVolcanoes.add(volcano.getName());
            }
        }
        String[] highVEIVolcanoArray = new String[highVEIVolcanoes.size()];
        highVEIVolcanoArray = highVEIVolcanoes.toArray(highVEIVolcanoArray);
        return highVEIVolcanoArray;
    }

    public Volcano mostDeadly() {
        return volcanos.stream()
                      .max(Comparator.comparingInt(v -> {
                          String deaths = v.getDEATHS();
                          return deaths.isEmpty() ? 0 : Integer.parseInt(deaths);
                      }))
                      .orElse(null);
    }

    public double causedTsunami() {
        long tsuVolcanoesCount = volcanos.stream()
                                         .filter(v -> "tsu".equals(v.getTsu()))
                                         .count();
        return (double) tsuVolcanoesCount * 100 / volcanos.size();
    }

    public String mostCommonType() {
        return volcanos.stream()
                      .collect(Collectors.groupingBy(Volcano::getType, Collectors.counting()))
                      .entrySet().stream()
                      .max(Map.Entry.comparingByValue())
                      .map(Map.Entry::getKey)
                      .orElse(null);
    }

	public int eruptionsByCountry(String country) {
        return (int) volcanos.stream()
                             .filter(v -> country.equals(v.getCountry()))
                             .count();
    }
	
    
    

    //add methods here to meet the requirements in README.md

}
