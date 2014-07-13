package de.codefor.le.crawler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import de.codefor.le.crawler.model.Nominatim;

@Component
public class NominatimAsker {
    private static final Logger logger = LoggerFactory.getLogger(NominatimAsker.class);

    private RestTemplate restTemplate;

    public NominatimAsker() {
        restTemplate = new RestTemplate();
    }

    @Async
    public Future<List<Nominatim>> execute(String adress) {
        List<Nominatim> result = new ArrayList<>();
        try {
            result = getCoords(adress);
            Thread.sleep(5000);
            logger.info("finshed getting coords");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<List<Nominatim>>(result);
    }

    private List<Nominatim> getCoords(String adress) {
        List<Nominatim> result = new ArrayList<>();
        String url = "http://nominatim.openstreetmap.org/search?q=" + adress + "&format=json";
        logger.debug("url {}", url);

        result = Arrays.asList(restTemplate.getForObject(url, Nominatim[].class));
        logger.debug("p {}", result.toString());
        return result;
    }

}