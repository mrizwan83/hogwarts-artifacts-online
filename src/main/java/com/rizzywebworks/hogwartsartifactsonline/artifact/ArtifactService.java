package com.rizzywebworks.hogwartsartifactsonline.artifact;

import com.rizzywebworks.hogwartsartifactsonline.artifact.utils.IdWorker;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ArtifactService {

    private final ArtifactRepository artifactRepository;

    private final IdWorker idWorker;

    public ArtifactService(ArtifactRepository artifactRepository, IdWorker idWorker) {

        this.artifactRepository = artifactRepository;
        this.idWorker = idWorker;
    }

    public Artifact findById(String artifactId) {

        return this.artifactRepository.findById(artifactId)
                .orElseThrow(()-> new ArtifactNotFoundException(artifactId));
    }

    public List<Artifact> findAll() {
        return this.artifactRepository.findAll();
    }

    public Artifact save(Artifact newArtifact) {
        // convert long to string
        newArtifact.setId(idWorker.nextId() + "");
        return this.artifactRepository.save(newArtifact);
    }
// we will use a fluent interface, which is an object-oriented API whose design relies extensively on method chaining.
    // Its goal is to increase code legibility.

    public Artifact update(String artifactId, Artifact artifact) {
        return this.artifactRepository.findById(artifactId)
                .map(oldArtifact -> {
                    oldArtifact.setName(artifact.getName());
                    oldArtifact.setDescription(artifact.getDescription());
                    oldArtifact.setImageUrl(artifact.getImageUrl());
                    return this.artifactRepository.save(oldArtifact);
                })
                .orElseThrow(()-> new ArtifactNotFoundException(artifactId));
    }

    public void delete(String artifactId) {
        this.artifactRepository.findById(artifactId)
                .orElseThrow(() -> new ArtifactNotFoundException(artifactId));
        this.artifactRepository.deleteById(artifactId);
    }

}
