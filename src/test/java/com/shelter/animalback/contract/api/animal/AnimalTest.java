
package com.shelter.animalback.contract.api.animal;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

import com.shelter.animalback.controller.AnimalController;
import com.shelter.animalback.domain.Animal;
import com.shelter.animalback.service.interfaces.AnimalService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@PactBroker(url = "${PACT_BROKER_BASE_URL}", authentication = @PactBrokerAuth(token = "${PACT_BROKER_TOKEN}"))
@Provider("AnimalShelterBack")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AnimalTest {
    @Mock
    private AnimalService animalService;
    @InjectMocks
    private AnimalController animalController;

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    public void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    public void changeContext(PactVerificationContext context) {
        MockMvcTestTarget testTarget = new MockMvcTestTarget();
        testTarget.setControllers(animalController);
        context.setTarget(testTarget);
        System.setProperty("pact.verifier.publishResults", "true");
    }

    @State("has animals")
    public void listAnimals() {
        Animal animal = new Animal();
        animal.setName("Bigotes");
        animal.setBreed("Siames");
        animal.setGender("Male");
        animal.setVaccinated(false);
        List<Animal> animals = new ArrayList<Animal>();
        animals.add(animal);
        Mockito.when(animalService.getAll()).thenReturn(animals);
    }

    @State("has an animal")
    public void listAnimalByName() {
        Animal animal = new Animal();
        animal.setName("Joy");
        animal.setBreed("Criollo");
        animal.setGender("Male");
        animal.setVaccinated(true);
        Mockito.when(animalService.get("Joy")).thenReturn(animal);
    }

    @State("adds an animals")
    public void addAnimal() {
        Animal animal = new Animal();
        animal.setName("Joy");
        animal.setBreed("Bengali");
        animal.setGender("Male");
        animal.setVaccinated(true);
        Mockito.lenient().when(animalService.save(any(Animal.class))).thenReturn(animal);
    }

    @State("update animasl")
    public void updateAnimal() {
        Animal animal = new Animal();
        animal.setId(1);
        animal.setName("Joy");
        animal.setBreed("Criollo");
        animal.setGender("Male");
        animal.setVaccinated(true);
        String[] vaccines = { "rabia" };
        animal.setVaccines(vaccines);
        Mockito.when(animalService.replace("Joy", animal)).thenReturn(animal);
    }

    @State("deletes animals")
    public void deleteAnimalByName() {
        Mockito.doNothing().when(animalService).delete("Joy");
    }

}
