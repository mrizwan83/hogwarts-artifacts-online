package com.rizzywebworks.hogwartsartifactsonline.wizard;

import com.rizzywebworks.hogwartsartifactsonline.artifact.Artifact;
import com.rizzywebworks.hogwartsartifactsonline.artifact.ArtifactRepository;
import com.rizzywebworks.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WizardService {

    private final WizardRepository wizardRepository;

    private final ArtifactRepository artifactRepository;

    public WizardService(WizardRepository wizardRepository, ArtifactRepository artifactRepository) {
        this.wizardRepository = wizardRepository;
        this.artifactRepository = artifactRepository;
    }

    // find by id
    public Wizard findById(Integer wizardId) {
        return this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
    }

    public List<Wizard> findAll() {
        return this.wizardRepository.findAll();
    }

    public Wizard save(Wizard newWizard) {
        return this.wizardRepository.save(newWizard);
    }

    public Wizard update(Integer wizardId, Wizard update) {
        return this.wizardRepository.findById(wizardId)
                .map(oldWizard -> {
                    oldWizard.setName(update.getName());
                    return this.wizardRepository.save(oldWizard);
                }).orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
    }

    public void delete(Integer wizardId) {
        Wizard wizardToBeDeleted= this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));

        // before deletion, we will un-assign this wizard's owned artifacts.
        wizardToBeDeleted.removeAllArtifacts();
        this.wizardRepository.deleteById(wizardId);
    }

    public void assignArtifact(Integer wizardId, String artifactId) {
        // find the artifact by id from DB
        Artifact artifactToBeAssigned = this.artifactRepository.findById(artifactId).orElseThrow(()-> new ObjectNotFoundException("artifact", artifactId));
        // find the wiard by id from DB
        Wizard wizard = this.wizardRepository.findById(wizardId).orElseThrow(()-> new ObjectNotFoundException("wizard", wizardId));


        // artifact assignment
            // remove artifact from old owner if it has owner
        if (artifactToBeAssigned.getOwner() != null){
            artifactToBeAssigned.getOwner().removeArtifact(artifactToBeAssigned);
        }
            // assign to new wizard
        wizard.addArtifact(artifactToBeAssigned);
    }
}
