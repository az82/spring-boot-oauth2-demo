package de.az82.auth.oauth2.demo.resourceserver;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@RestController
public class ResourceController {

    private final List<String> resources = new ArrayList<>(asList("Lignite", "Coal", "Oil"));

    @GetMapping("/resources")
    public List<String> getResources() {
        return resources;
    }

    @PostMapping("/resources")
    public void addResources(String resource) {
        resources.add(resource);
    }

    @DeleteMapping("/resources/{resource}")
    public void deleteResources(@PathVariable String resource) {
        resources.remove(resource);
    }


}
