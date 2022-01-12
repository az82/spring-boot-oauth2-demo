package de.az82.auth.oauth2.demo.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@Controller
public class ClientController {

    Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final WebClient webClient;

    public ClientController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping(value = "/")
    public String getResources(
            Model model,
            @RegisteredOAuth2AuthorizedClient("client-auth-code") OAuth2AuthorizedClient authorizedClient
    ) {
        model.addAttribute("resources", this.webClient
                .get()
                .uri("http://oauth2-demo-resource-server:8082/resources")
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String[].class)
                .block());
        return "list-resources";
    }

    @PostMapping(value = "/add")
    public ModelAndView addResource(
            String resource,
            @RegisteredOAuth2AuthorizedClient("client-auth-code") OAuth2AuthorizedClient authorizedClient
    ) {
        logger.info("Adding resource {}", resource);
        this.webClient
                .post()
                .uri("http://oauth2-demo-resource-server:8082/resources")
                .body(fromFormData("resource", resource))
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return new ModelAndView("redirect:/");
    }
}
