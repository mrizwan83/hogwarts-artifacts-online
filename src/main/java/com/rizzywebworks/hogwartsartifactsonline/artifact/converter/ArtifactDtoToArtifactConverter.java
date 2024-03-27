package com.rizzywebworks.hogwartsartifactsonline.artifact.converter;

import com.rizzywebworks.hogwartsartifactsonline.artifact.Artifact;
import com.rizzywebworks.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactDtoToArtifactConverter implements Converter<ArtifactDto, Artifact> {



    @Override
    public Artifact convert(ArtifactDto source) {
        Artifact artifact = new Artifact();
        //for records the getters are just the .field
        artifact.setId(source.id());
        artifact.setName(source.name());
        artifact.setDescription(source.description());
        artifact.setImageUrl(source.imageUrl());

        return artifact;
    }
}
