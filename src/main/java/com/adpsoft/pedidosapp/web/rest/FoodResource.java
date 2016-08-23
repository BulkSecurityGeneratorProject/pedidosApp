package com.adpsoft.pedidosapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.adpsoft.pedidosapp.domain.Food;

import com.adpsoft.pedidosapp.repository.FoodRepository;
import com.adpsoft.pedidosapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Food.
 */
@RestController
@RequestMapping("/api")
public class FoodResource {

    private final Logger log = LoggerFactory.getLogger(FoodResource.class);
        
    @Inject
    private FoodRepository foodRepository;

    /**
     * POST  /foods : Create a new food.
     *
     * @param food the food to create
     * @return the ResponseEntity with status 201 (Created) and with body the new food, or with status 400 (Bad Request) if the food has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/foods",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Food> createFood(@Valid @RequestBody Food food) throws URISyntaxException {
        log.debug("REST request to save Food : {}", food);
        if (food.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("food", "idexists", "A new food cannot already have an ID")).body(null);
        }
        Food result = foodRepository.save(food);
        return ResponseEntity.created(new URI("/api/foods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("food", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /foods : Updates an existing food.
     *
     * @param food the food to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated food,
     * or with status 400 (Bad Request) if the food is not valid,
     * or with status 500 (Internal Server Error) if the food couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/foods",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Food> updateFood(@Valid @RequestBody Food food) throws URISyntaxException {
        log.debug("REST request to update Food : {}", food);
        if (food.getId() == null) {
            return createFood(food);
        }
        Food result = foodRepository.save(food);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("food", food.getId().toString()))
            .body(result);
    }

    /**
     * GET  /foods : get all the foods.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of foods in body
     */
    @RequestMapping(value = "/foods",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Food> getAllFoods() {
        log.debug("REST request to get all Foods");
        List<Food> foods = foodRepository.findAllWithEagerRelationships();
        return foods;
    }

    /**
     * GET  /foods/:id : get the "id" food.
     *
     * @param id the id of the food to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the food, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/foods/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Food> getFood(@PathVariable Long id) {
        log.debug("REST request to get Food : {}", id);
        Food food = foodRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(food)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /foods/:id : delete the "id" food.
     *
     * @param id the id of the food to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/foods/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        log.debug("REST request to delete Food : {}", id);
        foodRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("food", id.toString())).build();
    }

}
