package com.rizzywebworks.hogwartsartifactsonline.wizard;

import com.rizzywebworks.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
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

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;

    @InjectMocks
    WizardService wizardService;

    List<Wizard> wizards;

    @BeforeEach
    void setUp() {
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbotom");

        this.wizards = new ArrayList<>();
        this.wizards.add(w1);
        this.wizards.add(w2);
        this.wizards.add(w3);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllSuccess() {
        //given
        given(this.wizardRepository.findAll()).willReturn(this.wizards);

        //when
        List<Wizard> actualWizards = this.wizardService.findAll();

        //then
        assertThat(actualWizards.size()).isEqualTo(this.wizards.size());
        verify(this.wizardRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        // given
        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry Potter");
        given(this.wizardRepository.findById(2)).willReturn(Optional.of(w));

        //when
        Wizard returnedWizard = this.wizardService.findById(2);

        //then
        assertThat(returnedWizard.getId()).isEqualTo(w.getId());
        assertThat(returnedWizard.getName()).isEqualTo(w.getName());

        verify(this.wizardRepository, times(1)).findById(2);
    }

    @Test
    void testFindByIdNotFound() {
        // given
        given(this.wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        //when
        Throwable thrown = catchThrowable(()-> {
            Wizard returnedWizard = this.wizardService.findById(9);
        });

        //then
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
                        .hasMessage("Could not find wizard with Id 9 :(");

        verify(this.wizardRepository, times(1)).findById(9);
    }

    @Test
    void testSaveSuccess() {
        //given
        Wizard newWizard = new Wizard();
        newWizard.setName("Hermione Granger");
        newWizard.setId(4);

        given(this.wizardRepository.save(newWizard)).willReturn(newWizard);

        //when
        Wizard savedWizard = this.wizardService.save(newWizard);

        //then
        assertThat(savedWizard.getId()).isEqualTo(4);
        assertThat(savedWizard.getName()).isEqualTo("Hermione Granger");

        verify(this.wizardRepository, times(1)).save(newWizard);
    }

    @Test
    void testUpdateSuccess() {
        //given simulate saved wizard in db and frontend request with new updated data
        Wizard oldWizard = new Wizard();
        oldWizard.setId(2);
        oldWizard.setName("Harry Potter");


        Wizard update = new Wizard();
        update.setName("New Name");

        //define behavior that should happen (first find then update)
        given(this.wizardRepository.findById(2)).willReturn(Optional.of(oldWizard));
        given(this.wizardRepository.save(oldWizard)).willReturn(oldWizard);

        //when
        Wizard updatedWizard = this.wizardService.update(2, update);

        //then

        assertThat(updatedWizard.getId()).isEqualTo(2);
        assertThat(updatedWizard.getName()).isEqualTo(update.getName());

        verify(this.wizardRepository, times(1)).findById(2);
        verify(this.wizardRepository, times(1)).save(oldWizard);

    }

    @Test
    void testUpdateNotFound() {
        //given
        Wizard update = new Wizard();
        update.setName("New Name");

        //define behavior that should happen (find and return empty optional)
        given(this.wizardRepository.findById(2)).willReturn(Optional.empty());

        //when
        assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.update(2, update);
        });

        //then
        verify(this.wizardRepository, times(1)).findById(2);
    }

    @Test
    void testDeleteSuccess() {
        //given
        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry Potter");
        given(this.wizardRepository.findById(2)).willReturn(Optional.of(w));
        doNothing().when(this.wizardRepository).deleteById(2);

        //when
        this.wizardService.delete(2);

        //then
        verify(this.wizardRepository, times(1)).deleteById(2);
    }

    @Test
    void testDeleteNotFound() {
        //given
        given(this.wizardRepository.findById(2)).willReturn(Optional.empty());

        //when
        assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.delete(2);
        });

        //then
        verify(this.wizardRepository, times(1)).findById(2);
    }

}