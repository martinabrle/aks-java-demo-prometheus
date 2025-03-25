/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.api.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.samples.petclinic.api.dto.Visits;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.ctc.wstx.shaded.msv_core.util.Uri;

import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * @author Maciej Szarlinski
 */
@Component
@RequiredArgsConstructor
public class VisitsServiceClient {

    // Could be changed for testing purpose
    private String hostname = "http://visits-service/";

    private final WebClient.Builder webClientBuilder;

    //TESTING pets retrieval
    public Mono<Visits> getVisitsForPets(final List<Integer> petIds) {
        
        var joinedPetIds = joinIds(petIds);

        var uri = String.format("{0}pets/visits?petId={1}", hostname, joinedPetIds);
        System.out.flush();
        
        System.out.println("Starting VisitsServiceClient.getVisitsForPets("+uri+")...");

        var retVal = webClientBuilder.build()
                    .get()
                    .uri(hostname + "pets/visits?petId={petId}", joinedPetIds)
                    .retrieve()
                    .bodyToMono(Visits.class);

        System.out.println("VisitsServiceClient.getVisitsForPets() - retVal: " + retVal);
        System.out.flush();
        
        return retVal;
    }

    private String joinIds(List<Integer> petIds) {
        return petIds.stream().map(Object::toString).collect(joining(","));
    }

    void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
