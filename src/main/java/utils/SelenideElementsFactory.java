package utils;

import com.codeborne.selenide.SelenideElement;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;

public class SelenideElementsFactory {

    private Map<String, String> elements = new HashMap<>();;
    private Map<String, String>[] tElements;
    private ObjectMapper mapper = new ObjectMapper();


    public SelenideElementsFactory() {

        try {
            tElements = mapper.readValue(new File("src/test/resources/SelenideElements.json"),
                    new TypeReference<Map<String, String>[]>() {});
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        for (int i = 0; i < tElements.length; i++) {
            elements.put(tElements[i].get("key"),  tElements[i].get("value"));
        }
    }

    public SelenideElement createElement(String key) {
        return $(elements.entrySet().stream()
                .filter(s -> s.getKey().equals(key))
                .map(Map.Entry::getValue)
                .collect(Collectors.joining()));
    }

}

