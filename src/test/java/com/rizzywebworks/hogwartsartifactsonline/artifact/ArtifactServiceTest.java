package com.rizzywebworks.hogwartsartifactsonline.artifact;

import com.rizzywebworks.hogwartsartifactsonline.artifact.utils.IdWorker;
import com.rizzywebworks.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import com.rizzywebworks.hogwartsartifactsonline.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

// for JUnit 5, we need to use @ExtendWith
@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {

    @Mock // @Mock defines a Mockito mock object
    ArtifactRepository artifactRepository;
// using a mock simulation of the repository behavior (we are mocking the dependencies)
    @Mock
    IdWorker idWorker;
    @InjectMocks // The Mockito mock objects for ArtifactService
    ArtifactService artifactService;

    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");
        this.artifacts = new ArrayList<>();
        this.artifacts.add(a1);
        this.artifacts.add(a2);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        // given (arrange inputs and targets. define the behavior of mock object artifactRepository)
        /*
        "id": "1250808601744904192",
        "name": "Invisibility Cloak",
        "description": "An invisibility cloak is used to make the wearer invisible.",
        "imageUrl": "ImageUrl",
         */
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry Potter");

        a.setOwner(w);

                        //setup mock behavior and define it
        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a));

        // when (act on the target behavior(artifact service). When steps should cover the method ot be tested)

        Artifact returnedArtifact = this.artifactService.findById("1250808601744904192");

        // then (assert expected outcomes.)

        assertThat(returnedArtifact.getId()).isEqualTo(a.getId());
        assertThat(returnedArtifact.getName()).isEqualTo(a.getName());
        assertThat(returnedArtifact.getDescription()).isEqualTo(a.getDescription());
        assertThat(returnedArtifact.getImageUrl()).isEqualTo(a.getImageUrl());

        verify(this.artifactRepository, times(1)).findById("1250808601744904192");
    }

    @Test
    void testFindByIdNotFound(){
        //given
            given(this.artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());
        //when
            Throwable thrown = catchThrowable(()-> {
                Artifact returnedArtifact = this.artifactService.findById("1250808601744904192");
            });

        //then
            assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
                    .hasMessage("Could not find artifact with Id 1250808601744904192 :(");

        verify(this.artifactRepository, times(1)).findById("1250808601744904192");

    }

    @Test
    void testFindAllSuccess() {
        //given
        given(this.artifactRepository.findAll()).willReturn(this.artifacts);

        //when
        List<Artifact> actualArtifacts = this.artifactService.findAll();

        //then
        assertThat(actualArtifacts.size()).isEqualTo(this.artifacts.size());
        verify(this.artifactRepository, times(1)).findAll();
    }

    @Test
    void testSaveSuccess() {
        //given
        Artifact newArtifact = new Artifact();
        newArtifact.setName("Artifact 3");
        newArtifact.setDescription("Description");
        newArtifact.setImageUrl("ImageUrl...");

        given(this.idWorker.nextId()).willReturn(123456L);
        given(this.artifactRepository.save(newArtifact)).willReturn(newArtifact);

        //when
        Artifact savedArtifact = this.artifactService.save(newArtifact);

        //then
        assertThat(savedArtifact.getId()).isEqualTo("123456");
        assertThat(savedArtifact.getName()).isEqualTo("Artifact 3");
        assertThat(savedArtifact.getDescription()).isEqualTo("Description");
        assertThat(savedArtifact.getImageUrl()).isEqualTo("ImageUrl...");
        verify(this.artifactRepository, times(1)).save(newArtifact);

    }

    @Test
    void testUpdateSuccess() {
        //given simulate saved artifact in db and frontend request with new updated data
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("1250808601744904192");
        oldArtifact.setName("Invisibility Cloak");
        oldArtifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        oldArtifact.setImageUrl("ImageUrl");

        Artifact update = new Artifact();
//        update.setId("1250808601744904192");
        update.setName("Invisibility Cloak");
        update.setDescription("A new description.");
        update.setImageUrl("ImageUrl");

        //define behavior that should happen (first find then update)
        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(oldArtifact));
        given(this.artifactRepository.save(oldArtifact)).willReturn(oldArtifact);

        //when
        Artifact updatedArtifact = this.artifactService.update("1250808601744904192", update);

        //then
        assertThat(updatedArtifact.getId()).isEqualTo("1250808601744904192");
        assertThat(updatedArtifact.getDescription()).isEqualTo(update.getDescription());

        verify(this.artifactRepository, times(1)).findById("1250808601744904192");
        verify(this.artifactRepository, times(1)).save(oldArtifact);

    }

    @Test
    void testUpdateNotFound() {
        //given
        Artifact update = new Artifact();
        update.setName("Invisibility Cloak");
        update.setDescription("A new description.");
        update.setImageUrl("ImageUrl");

        //define behavior that should happen (find and return empty optional)
        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        //when
        assertThrows(ObjectNotFoundException.class, () -> {
            this.artifactService.update("1250808601744904192", update);
        });


        //then
        verify(this.artifactRepository, times(1)).findById("1250808601744904192");
    }

    @Test
    void testDeleteSuccess() {
        //given
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904192");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        artifact.setImageUrl("ImageUrl");

        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(artifact));
        doNothing().when(this.artifactRepository).deleteById("1250808601744904192");

        //when
        this.artifactService.delete("1250808601744904192");

        //then
        verify(this.artifactRepository, times(1)).deleteById("1250808601744904192");
    }

    @Test
    void testDeleteNotFound() {
        //given
        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        //when
        assertThrows(ObjectNotFoundException.class, () -> {
            this.artifactService.delete("1250808601744904192");
        });

        //then
        verify(this.artifactRepository, times(1)).findById("1250808601744904192");
    }
}